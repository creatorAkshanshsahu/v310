# AK TV 3.10 — Android source update

This ZIP contains source, not an installable APK. Minimum Android version remains 9 (API 28). Android TV and phone launchers remain enabled; landscape layout is retained.

## Changes
- TG Video replaces Net77/AethoFlix and the old movie/series website row.
- TG Video accepts TMDB ID, movie or TV episode, season/episode, preferred audio language and autoplay-next preference. Uses documented /embed/movie/{id} and /embed/tv/{id}/{season}/{episode} pages in the bundled browser. This is an embedded web player, not a native TG Video extraction engine or a searchable title catalogue.
- Smaller-screen layout, touch playback actions, more restrained focus scaling, cancellable transitions and a shorter matching-logo intro.
- New launcher icon and TV banner.
- Previous Server 3-first live-channel resolver, native Media3 playback and stream fallbacks are preserved unchanged.

## Build APKs
Use the included GitHub Actions workflow: upload source to a repository, open Actions → Build APK → Run workflow, then download AK-TV-apks. It builds separate arm32 and arm64 debug APKs using JDK 17 and Gradle 8.9. Debug APKs are for sideload testing, not production signing. Existing installations must share the signing key to update in place.

## Verification and limitations
Source/XML and ZIP structure were checked. No Android SDK/emulator is available here: compilation, phone/TV remote navigation and playback are NOT device-verified. Android 9 minimum compatibility is configured, not guaranteed on every device; future Android releases cannot be guaranteed. targetSdk stays at the previous API 33 to avoid claiming untested platform migrations. Android 16 KB page-size compatibility of the bundled GeckoView 123 native libraries is unverified; upgrade and test dependencies before store distribution.

TG Video does not document a catalogue/search API or a universal autoplay query parameter. Use its built-in player controls to begin playback if needed. Audio tracks, subtitles and availability depend on upstream providers. Live streams can expire or be geo-blocked; this update cannot guarantee every channel works. The existing bundled playlist contains expiring tokens and the Server 3 resolver depends on the upstream site's deployment.
