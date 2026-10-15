# Archive architecture
- Keep live playback in MainActivity with Media3 and existing source fallback; this preserves previous live-channel behavior.
- TG Video builds documented embed URLs in TgVideoActivity and opens BrowserActivity; no undocumented stream extraction or catalogue API is assumed.
- Keep minimum Android support configured in Gradle and both launcher categories in the manifest; device and future-release compatibility require actual testing.
- Use one bitmap mark for the intro, launcher and banner so branding stays consistent.
