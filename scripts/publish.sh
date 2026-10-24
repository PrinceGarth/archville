#!/usr/bin/env bash
# Build every app, regenerate the signed F-Droid index, and push the result to
# the gh-pages branch (served by GitHub Pages at https://princegarth.github.io/archville/).
#
# Runs from a maintainer machine: the signing keys never leave secrets/.
# Needs: secrets/{archville-apps.p12,archville-repo.p12,passwords.env},
#        ANDROID_HOME, JDK 17+, fdroidserver (`fdroid` on PATH, or set FDROID=/path/to/fdroid).
# Pass --no-push to stop after committing to the local gh-pages worktree.
set -euo pipefail

root="$(cd "$(dirname "$0")/.." && pwd)"
cd "$root"
pages="$root/pages"
fdroid="${FDROID:-fdroid}"
push=1
[ "${1:-}" = "--no-push" ] && push=0

set -a; . secrets/passwords.env; set +a
export APK_KEYSTORE="$root/secrets/archville-apps.p12"

# gh-pages worktree holds what is currently published
if [ ! -d "$pages" ]; then
	if git fetch origin gh-pages 2>/dev/null; then
		git worktree add -B gh-pages "$pages" origin/gh-pages
	else
		git worktree add --orphan -b gh-pages "$pages"
	fi
fi

# Start from the published state so existing APKs, archive and dates carry over
mkdir -p fdroid/repo fdroid/archive fdroid/tmp
[ -d "$pages/repo" ] && rsync -a "$pages/repo/" fdroid/repo/
[ -d "$pages/archive" ] && rsync -a "$pages/archive/" fdroid/archive/
[ -f "$pages/cache/apkcache.json" ] && cp "$pages/cache/apkcache.json" fdroid/tmp/apkcache.json

scripts/build-apps.sh

install -m 600 secrets/archville-repo.p12 fdroid/keystore.p12
(cd fdroid && "$fdroid" update --pretty)

rsync -a --delete fdroid/repo/ "$pages/repo/"
rsync -a --delete fdroid/archive/ "$pages/archive/"
mkdir -p "$pages/cache"
cp fdroid/tmp/apkcache.json "$pages/cache/apkcache.json"
touch "$pages/.nojekyll"
cat > "$pages/index.html" <<'EOF'
<!doctype html>
<meta charset="utf-8">
<title>F-Droid Archville</title>
<meta http-equiv="refresh" content="0; url=repo/">
<a href="repo/">F-Droid Archville repository</a>
EOF

git -C "$pages" add -A
if git -C "$pages" diff --cached --quiet; then
	echo "Nothing changed; gh-pages not updated."
	exit 0
fi
git -C "$pages" commit -q -m "Publish repo from $(git rev-parse --short HEAD)"
if [ "$push" = 1 ]; then
	git -C "$pages" push origin gh-pages
else
	echo "Committed to local gh-pages; not pushed (--no-push)."
fi
