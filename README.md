# F-Droid Archville

<img src="fdroid/icon.png" alt="" width="96" align="right">

A third-party [F-Droid](https://f-droid.org) repository of apps from the
official **F-Droid Archive** that no longer install or run on current phones,
rebuilt from their original source with modern Android compatibility.

## Add the repo

In F-Droid (or Droid-ify, Neo Store, …): **Settings → Repositories → +**, then
paste:

```
https://princegarth.github.io/archville/repo?fingerprint=BD310DF3D761C87BF50FBD42A488594DDD8CBE9A0BE0D5A68D1D6649B65F8834
```

Or open <https://princegarth.github.io/archville/repo/> on your phone for a
QR code and one-tap link.

Repo signing key fingerprint (SHA-256):
`BD 31 0D F3 D7 61 C8 7B F5 0F BD 42 A4 88 59 4D DD 8C BE 9A 0B E0 D5 A6 8D 1D 66 49 B6 5F 88 34`

## Apps

| App | Archived version | Archville version | Why it broke | Source |
|---|---|---|---|---|
| **Space Trader** (`com.brucelet.spacetrader`) | 1.3.5 (targetSdk 23) | 1.3.5-archville1 (targetSdk 36) | Android 15+ refuses to install apps targeting below API 24 (`INSTALL_FAILED_DEPRECATED_SDK_VERSION`) | [apps/com.brucelet.spacetrader](apps/com.brucelet.spacetrader) |

### Space Trader: what changed

Gameplay is untouched. The build and platform glue were brought forward:

- Gradle 9.8 / AGP 9.4, compile + target SDK 36, minSdk 21
- Support Library 23 → AndroidX AppCompat
- Edge-to-edge window insets (enforced from Android 15)
- Predictive back gesture (Android 16 no longer calls `onBackPressed()`)
- Fixed a crash when switching screens: AndroidX Fragments briefly attach two
  screens at once, and the old `ScrollView` container only accepts one
- Removed a private fork of AppCompat's `ListPopupWindow` that relied on
  internals AndroidX made private

Tested on a Pixel 8 Pro running Android 17. The first commit touching
`apps/com.brucelet.spacetrader` is the archived source unmodified; every later
commit there is an Archville change.

## Compatibility policy

Each app ships **one universal APK** (it covers every CPU architecture, since
the apps so far are pure Java). Archville builds need Android 5.0+ (API 21).
Older phones should keep using the original APK from the official
[F-Droid Archive](https://f-droid.org/en/docs/Archive/) repo. F-Droid clients
hide APKs a device can't run, so the two repos don't collide.

Archville APKs are signed with the Archville key, not the original developer's.
Android won't update across signing keys: uninstall the old app first.

## Repository layout

```
apps/<applicationId>/      app source: commit 1 = archived original, later commits = fixes
fdroid/config.yml          fdroidserver config (passwords come from the environment)
fdroid/metadata/           store listing text and screenshots per app
scripts/build-apps.sh      build + sign every app into fdroid/repo/
scripts/publish.sh         build, index, and push the repo to the gh-pages branch
```

The published repository lives on the `gh-pages` branch and is served by
GitHub Pages.

## Adding an app

1. Pick an app from the F-Droid Archive and download its
   `<applicationId>_<versionCode>_src.tar.gz` from `https://f-droid.org/archive/`.
2. Commit it unmodified under `apps/<applicationId>/`.
3. Modernise the build in a separate commit, and test it on a recent phone.
4. Use `versionCode = <original versionCode> * 1000 + <Archville revision>`.
5. Add `fdroid/metadata/<applicationId>.yml`, then run `scripts/publish.sh`.

## Publishing (maintainer)

Signing keys live only in `secrets/` on the maintainer's machine (gitignored):
`archville-apps.p12` (app key), `archville-repo.p12` (repo index key) and
`passwords.env`. Keep an offline backup. Losing the repo key forces every user
to re-add the repo under a new fingerprint. Losing the app key means installed
apps can never be updated.

```sh
pipx install fdroidserver==2.4.5
export JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64 ANDROID_HOME=~/Android
scripts/publish.sh            # builds new versions, re-indexes, pushes gh-pages
```

## Licenses

Each app keeps its upstream license; see the `LICENSE` file in its
directory. Space Trader is GPL-3.0-or-later (© 2014–2016 Russell Wolf, based on
code by Pieter Spronck).
