# Icon Pack Builder for Samsung One UI

Purpose: Android app to capture the current icons from your Samsung Galaxy device, assemble them into a One UI–compatible icon pack, and generate an installable APK you can move to another phone. Generated APKs must be signed before installation.

## Key Features
- Capture current icons: pull installed app list and resolve their active launcher icons as the default set for the pack.
- Custom icon management: import PNG/SVG icons per app, bulk import, preview with masking and shape options.
- Pack configuration: set pack name, version, author, adaptive icon settings, and One UI-specific metadata (theme and mask options).
- APK generation: build One UI-compatible icon pack APK on device; produce unsigned APK and prompt user to sign before install.
- Validation: highlight missing icons, invalid sizes, and manifest issues before build.
- Theming helpers: optional background shapes, foreground scaling, and auto-contrast for legibility.
- Export/import projects: save and reload pack projects for later edits (JSON + assets bundle).

## User Flow (Step-by-Step)
1) Start new pack: user enters pack name, version, author, and optional theme color.
2) Auto-capture icons: app scans installed packages, grabs their current launcher icons, and seeds the project with those assets.
3) Add or adjust: search installed apps or add custom package names; override captured icons by importing/drawing replacements.
4) Configure pack: choose adaptive options, rounding, background, and One UI compatibility toggles.
5) Validate: run checks for missing icons, size mismatches, and manifest completeness; show fix suggestions.
6) Build: generate unsigned APK using a background build service; on success, show APK path and remind to sign.
7) Sign and install elsewhere: user signs APK (in-app helper or external tool), then installs on the new phone.
8) Manage projects: reopen saved projects to refresh captures, update icons, and rebuild.

## Technical Details
- Language/Stack: Kotlin, Jetpack (ViewModel, LiveData/Flow), Material 3 UI.
- Storage: Scoped storage via MediaStore and app-private directories; use SAF picker for imports; no broad storage permission.
- Data model: project metadata (name, version, author), icon entries (packageName, label, iconUri, variants), build settings (mask, background, scaling).
- Build pipeline: generate resources (res/drawable, xml), manifest, and adaptive icon XML; invoke on-device Gradle-like packaging via AAPT/BundleTool wrapper service.
- One UI compatibility: ensure correct intent filters and metadata in manifest; adaptive icon XML with foreground/background layers; respect One UI masking rules.
- Performance: background coroutine dispatcher for IO and bitmap ops; cache decoded bitmaps; downscale on import; incremental rebuild when only icons change.
- Security: validate URIs before access; sanitize file names; verify MIME types; keep build artifacts in app-private storage until user exports; no embedded signing keys.
- UX: Material 3, dark/light themes, accessibility labels, large tap targets; progress and error surfaces during build.

## Modules (proposed)
- app: UI screens, navigation, theming (Jetpack Compose).
- domain: use cases for project CRUD, validation, and build orchestration.
- data: repositories for icons/projects, MediaStore and SAF access, cache.
- buildservice: APK generation, resource packaging, manifest creation.

## Current Scaffold
- Kotlin + Compose Material 3, targetSdk 34, minSdk 29.
- Multi-module Gradle (app, domain, data, buildservice) with placeholders for capture/build logic.
- Gradle wrapper included (Gradle 8.6, AGP 8.4.1, Kotlin 1.9.24).

## Build/Run
1) Install Android SDK 34 and a JDK 17.
2) From the project root run `./gradlew assembleDebug` (or `gradlew.bat assembleDebug` on Windows).
3) Install the debug APK on device: `adb install app/build/outputs/apk/debug/app-debug.apk`.
4) The generated icon pack APK will be unsigned; sign before installing on another device.

## Storage Model
- App-private dir: working project files, decoded bitmaps cache, build outputs (unsigned APK).
- MediaStore/SAF: user-selected imports and optional exports; persist permissions via takePersistableUriPermission.

## Performance Optimizations
- Decode and downscale icons off main thread; reuse bitmaps; LRU cache previews.
- Diff-based updates in RecyclerViews; suspend functions with Flow for async updates.
- Parallel resource generation for large packs; throttle UI recomposition where possible.

## Security and Privacy
- Never store signing keys; require user-provided signing step.
- Input validation for file names, package names, and MIME types.
- Restrict file operations to granted URIs and app-private dirs.
- Clear temporary files after build and when projects are deleted.

## Build and Signing Notes
- Output APK is unsigned; user must sign before installing on device (e.g., via apksigner or an in-app signing helper with user-provided key).
- Target SDK: 34; min SDK: 29 (for scoped storage); enable Play Feature Delivery not required.
- Use release and debug build variants; disable debuggable for exported builds.

## Roadmap
- In-app signing helper (key import from user-provided keystore).
- Icon editor (crop, pad, background shapes).
- Cloud backup of projects (user opted, encrypted at rest).
- Automated tests: unit tests for validation/build steps; instrumented tests for project CRUD.
