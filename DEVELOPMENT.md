# Development Guide

## Project Structure

```
Icon-pack-saver-for-one-UI/
├── app/
│   ├── build.gradle                    # App-level build configuration
│   ├── proguard-rules.pro             # ProGuard rules
│   └── src/
│       └── main/
│           ├── AndroidManifest.xml    # App manifest with permissions
│           ├── java/com/iconpacksaver/oneui/
│           │   ├── MainActivity.java          # Main UI activity
│           │   ├── IconPickerActivity.java    # Icon picker placeholder
│           │   ├── AppIconInfo.java           # Data model for app icons
│           │   ├── AppIconAdapter.java        # RecyclerView adapter
│           │   └── IconPackBuilder.java       # APK generation logic
│           └── res/
│               ├── layout/            # XML layouts
│               ├── values/            # Strings, colors, themes
│               ├── drawable/          # Vector drawables
│               ├── mipmap-*/          # Launcher icons
│               └── xml/               # File provider paths
├── build.gradle                        # Project-level build configuration
├── settings.gradle                     # Project settings
├── gradle.properties                   # Gradle properties
└── README.md                          # User documentation
```

## Key Components

### MainActivity.java

The main activity handles:
- UI interaction for adding/managing apps
- Image selection for icons
- Permission management
- Building icon packs

### IconPackBuilder.java

Responsible for:
- Creating icon pack directory structure
- Generating XML files (appfilter.xml, drawable.xml)
- Creating AndroidManifest.xml for the icon pack
- Zipping everything into an APK file

### AppIconAdapter.java

RecyclerView adapter that:
- Displays the list of added apps
- Handles item deletion
- Shows app icons and metadata

## Building an Icon Pack

The app creates icon packs with the following structure:

```
IconPack.apk
├── AndroidManifest.xml
└── res/
    ├── drawable/
    │   ├── ic_launcher.png
    │   ├── icon_0.png
    │   ├── icon_1.png
    │   └── ...
    ├── xml/
    │   ├── appfilter.xml      # Maps apps to icons
    │   └── drawable.xml       # List of drawables
    └── values/
        └── strings.xml        # App name
```

### appfilter.xml Format

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <item component="ComponentInfo{com.example.app/.MainActivity}" drawable="icon_0" />
    <item component="ComponentInfo{com.another.app/.Activity}" drawable="icon_1" />
</resources>
```

### drawable.xml Format

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <version>1</version>
    <item drawable="icon_0" />
    <item drawable="icon_1" />
</resources>
```

## Development Setup

### Prerequisites

- JDK 11 or higher
- Android SDK (API 28+)
- Android Studio Arctic Fox or newer (recommended)
- Gradle 8.0+

### First-Time Setup

1. Clone the repository
2. Open in Android Studio
3. Let Gradle sync complete
4. Configure an Android Virtual Device (AVD) or connect a physical device

### Building

#### Debug Build
```bash
./gradlew assembleDebug
```

#### Release Build
```bash
./gradlew assembleRelease
```

### Running Tests

Currently, the project doesn't have automated tests. This is an area for future improvement.

## Known Limitations

1. **APK Signing**: The generated icon pack files are unsigned ZIP archives with an `.apk` extension. They need proper signing to be installable:
   - Use Android Studio's APK Analyzer
   - Or use `apksigner` tool from Android SDK

2. **Component Names**: Users need to know the exact component name (package + activity) for each app they want to customize.

3. **One UI Compatibility**: The generated icon packs follow standard Android icon pack conventions. Full One UI launcher integration may require additional metadata.

## Future Enhancements

- [ ] Add app picker to automatically detect installed apps
- [ ] Implement proper APK signing within the app
- [ ] Add icon pack preview functionality
- [ ] Support for adaptive icons
- [ ] Backup/restore icon pack configurations
- [ ] Import/export functionality
- [ ] More icon pack metadata (author, description, etc.)
- [ ] Support for themed icons
- [ ] Batch icon import

## Contributing

When contributing to this project:

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test on a physical Samsung device if possible
5. Submit a pull request

### Code Style

- Follow standard Java conventions
- Use meaningful variable names
- Add comments for complex logic
- Keep methods focused and concise

## Troubleshooting

### Gradle Sync Issues

If Gradle sync fails:
1. Check your internet connection
2. Verify Android SDK is properly installed
3. Try: File > Invalidate Caches / Restart
4. Delete `.gradle` folder and sync again

### Build Errors

Common issues:
- **Missing SDK**: Install required SDK version via SDK Manager
- **Wrong Java version**: Use JDK 11 or higher
- **Dependency issues**: Clear Gradle cache: `./gradlew clean`

### Runtime Issues

- **Permission denied**: Ensure storage permissions are granted
- **Build fails**: Check if output directory is writable
- **Icon not showing**: Verify image format is PNG or JPG

## License

This project is open source and available for personal and educational use.
