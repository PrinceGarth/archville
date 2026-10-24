#!/usr/bin/env bash
# Build every app under apps/ and drop a signed APK into fdroid/repo/.
#
# An APK whose <applicationId>_<versionCode>.apk is already in fdroid/repo/ is
# left alone: published files are never replaced, so bump versionCode to ship.
#
# Needs: ANDROID_HOME, a JDK 17+ on JAVA_HOME/PATH, and
#   APK_KEYSTORE        path to the PKCS12 app-signing keystore
#   APK_KEYSTORE_PASS   its password
#   APK_KEY_ALIAS       key alias (default: archville-apps)
# Optional: only build the apps named as arguments (directory names under apps/).
set -euo pipefail

root="$(cd "$(dirname "$0")/.." && pwd)"
repo="$root/fdroid/repo"
mkdir -p "$repo"

: "${ANDROID_HOME:?set ANDROID_HOME}"
: "${APK_KEYSTORE:?set APK_KEYSTORE}"
: "${APK_KEYSTORE_PASS:?set APK_KEYSTORE_PASS}"
alias="${APK_KEY_ALIAS:-archville-apps}"

build_tools="$(ls -d "$ANDROID_HOME"/build-tools/* | sort -V | tail -1)"
aapt2="$build_tools/aapt2"
apksigner="$build_tools/apksigner"

if [ $# -gt 0 ]; then apps=("$@"); else apps=($(ls "$root/apps")); fi

for app in "${apps[@]}"; do
	dir="$root/apps/$app"
	echo "==> $app"
	(cd "$dir" && ./gradlew --no-daemon --console=plain assembleRelease)

	# app/ module for Android Studio projects, root module for converted Ant projects
	shopt -s nullglob
	unsigned=("$dir"/{app/,}build/outputs/apk/release/*-release-unsigned.apk)
	shopt -u nullglob
	unsigned="${unsigned[0]:?no release APK found for $app}"
	badging="$("$aapt2" dump badging "$unsigned")"
	badging="${badging%%$'\n'*}"
	app_id="$(sed -n "s/.*package: name='\([^']*\)'.*/\1/p" <<<"$badging")"
	version_code="$(sed -n "s/.*versionCode='\([^']*\)'.*/\1/p" <<<"$badging")"
	out="$repo/${app_id}_${version_code}.apk"

	if [ -e "$out" ]; then
		echo "    $(basename "$out") already published, skipping"
		continue
	fi

	"$apksigner" sign --ks "$APK_KEYSTORE" --ks-pass env:APK_KEYSTORE_PASS \
		--ks-key-alias "$alias" --v4-signing-enabled false --out "$out" "$unsigned"
	"$apksigner" verify "$out"
	echo "    wrote $(basename "$out")"
done
