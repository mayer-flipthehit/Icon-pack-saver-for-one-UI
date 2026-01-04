#!/bin/bash

# Icon Pack Saver - Project Setup Script
# This script helps initialize the Android project

echo "Setting up Icon Pack Saver Android Project..."

# Check if gradle is installed
if ! command -v gradle &> /dev/null; then
    echo "Error: Gradle is not installed. Please install Gradle first."
    echo "Visit: https://gradle.org/install/"
    exit 1
fi

# Generate gradle wrapper if it doesn't exist
if [ ! -f "gradle/wrapper/gradle-wrapper.jar" ]; then
    echo "Generating Gradle Wrapper..."
    gradle wrapper --gradle-version 8.0
fi

echo "Project setup complete!"
echo "You can now build the project with: ./gradlew build"
echo "Or open it in Android Studio"
