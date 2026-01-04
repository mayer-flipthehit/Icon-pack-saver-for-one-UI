# Icon Pack Saver for One UI

An Android application designed for Samsung Galaxy One UI devices that allows users to create custom icon packs. Save your custom icons and build them into an installable icon pack APK.

## Features

- 📱 **Custom Icon Management**: Add and manage custom icons for your favorite apps
- 🎨 **Icon Pack Creation**: Build complete icon packs with custom launcher icons
- 📦 **APK Generation**: Create installable icon pack files
- 🎯 **One UI Compatible**: Designed specifically for Samsung Galaxy One UI devices
- 💾 **Easy Save & Build**: Simple interface to save icons and compile them into a pack

## How It Works

1. **Set Pack Details**: Enter your icon pack name and package identifier
2. **Upload Launcher Icon**: Choose a custom icon for your icon pack app
3. **Add Apps**: For each app you want to customize:
   - Enter the app name
   - Enter the component name (e.g., `com.example.app/.MainActivity`)
   - Select a custom icon image
4. **Build**: Tap "Build Icon Pack" to generate your custom icon pack
5. **Install**: The generated icon pack file will be saved to your Downloads folder

## Requirements

- Android 9.0 (API 28) or higher
- Samsung Galaxy device with One UI
- Storage permissions for saving icon packs

## Installation

1. Clone this repository
2. Open in Android Studio
3. Build and install on your device

```bash
git clone https://github.com/mayer-flipthehit/Icon-pack-saver-for-one-UI.git
cd Icon-pack-saver-for-one-UI
./gradlew assembleDebug
```

## Usage

### Building Your First Icon Pack

1. Open the app on your Samsung Galaxy device
2. Grant storage permissions when prompted
3. Enter a name for your icon pack (e.g., "My Custom Icons")
4. Enter a unique package name (e.g., "com.myicons.pack")
5. Optionally upload a launcher icon for the pack
6. Add apps by clicking "Add App":
   - Fill in the app name
   - Fill in the component name (format: `package.name/activity`)
   - Select an icon image from your gallery
7. Tap "Build Icon Pack" when ready
8. Find your generated icon pack in `Downloads/IconPacks/`

### Finding Component Names

To find an app's component name:
- Use apps like "Activity Launcher" or "Component Info" from Play Store
- Component names follow format: `com.package.name/.ActivityName`

## Technical Details

The app generates icon pack resources including:
- `appfilter.xml` - Maps apps to custom icons
- `drawable.xml` - Lists available icons
- `AndroidManifest.xml` - Package metadata
- Icon image files in PNG format

## Permissions

- **READ_EXTERNAL_STORAGE**: To access images for icons
- **WRITE_EXTERNAL_STORAGE**: To save generated icon packs (Android 9-12)
- **MANAGE_EXTERNAL_STORAGE**: To save files on Android 13+ (optional)
- **REQUEST_INSTALL_PACKAGES**: To install generated icon packs

## Note

The generated file contains icon pack resources. To use it as a fully functional icon pack on One UI, the APK needs to be properly signed using Android development tools or keystore.

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## License

This project is open source and available for personal and educational use.

## Support

For issues, questions, or feature requests, please open an issue on GitHub.
