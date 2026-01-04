# Contributing to Icon Pack Saver for One UI

Thank you for your interest in contributing! This document provides guidelines for contributing to the project.

## How to Contribute

### Reporting Bugs

If you find a bug, please open an issue with:
- A clear description of the problem
- Steps to reproduce
- Expected vs actual behavior
- Device and Android version
- Screenshots if applicable

### Suggesting Features

Feature requests are welcome! Please:
- Check if the feature has already been requested
- Provide a clear use case
- Explain how it benefits users
- Consider implementation complexity

### Code Contributions

1. **Fork the Repository**
   ```bash
   git clone https://github.com/YOUR-USERNAME/Icon-pack-saver-for-one-UI.git
   ```

2. **Create a Branch**
   ```bash
   git checkout -b feature/your-feature-name
   ```

3. **Make Your Changes**
   - Follow the existing code style
   - Add comments for complex logic
   - Test your changes thoroughly

4. **Commit Your Changes**
   ```bash
   git commit -m "Add: Brief description of your changes"
   ```

5. **Push to Your Fork**
   ```bash
   git push origin feature/your-feature-name
   ```

6. **Open a Pull Request**
   - Describe your changes clearly
   - Reference any related issues
   - Include screenshots for UI changes

## Development Guidelines

### Code Style

- Follow Java naming conventions
- Use meaningful variable and method names
- Keep methods focused and concise
- Add JavaDoc comments for public methods
- Limit line length to 120 characters

### Testing

Before submitting:
- Test on a physical Samsung device if possible
- Test with different Android versions (API 28+)
- Verify all permissions work correctly
- Test icon pack generation end-to-end

### Commit Messages

Use clear, descriptive commit messages:
- `Add:` for new features
- `Fix:` for bug fixes
- `Update:` for updates to existing features
- `Refactor:` for code refactoring
- `Docs:` for documentation changes

Example:
```
Add: Icon preview functionality in main activity
Fix: Crash when selecting large images
Update: Improve error messages for failed builds
```

## Pull Request Process

1. Update the README.md if you add new features
2. Update DEVELOPMENT.md for technical changes
3. Ensure the app builds without errors
4. Test your changes thoroughly
5. Describe your changes in the PR description
6. Link any related issues

## Code Review

All contributions will be reviewed. We'll check for:
- Code quality and style
- Functionality and testing
- Documentation updates
- Compatibility with existing features

## Questions?

Feel free to open an issue for questions or clarifications.

## License

By contributing, you agree that your contributions will be licensed under the same license as the project.
