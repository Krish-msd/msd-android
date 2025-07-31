# MySillyDreams Android App

A beautiful Android application with Google OAuth authentication, featuring modern neumorphic design and secure credential management.

## 🚀 Features

### 🎨 Design & UI
- **Neumorphic Design**: Beautiful neumorphic components matching the web version
- **Material Design 3**: Modern Material Design components and theming
- **Responsive Layout**: Optimized for all screen sizes and orientations
- **Smooth Animations**: Elegant transitions and loading states

### 🔐 Authentication
- **Google OAuth**: Secure authentication using Google's Credential Manager API
- **Firebase Integration**: Industry-standard authentication backend
- **Secure Configuration**: Environment-based credential management
- **Cross-Device Support**: Works on emulators and physical devices

### 📱 Architecture
- **MVVM Pattern**: Clean separation of concerns with reactive state management
- **Jetpack Compose**: Modern declarative UI framework
- **StateFlow**: Reactive authentication state management
- **Error Handling**: Comprehensive error handling and user feedback

## 🛠️ Tech Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM with StateFlow
- **Authentication**: Firebase Auth + Credential Manager
- **Security**: Environment variables with BuildConfig
- **Design**: Material Design 3 with neumorphic components

## 📋 Project Structure

```
app/src/main/java/com/mysillydeams/app/
├── auth/                   # Authentication logic
│   ├── AuthRepository.kt   # Authentication data layer
│   ├── AuthViewModel.kt    # Authentication view model
│   └── AuthState.kt        # Authentication state definitions
├── config/                 # Configuration management
│   └── AppConfig.kt        # Secure configuration access
├── ui/                     # User interface
│   ├── components/         # Reusable UI components
│   ├── screens/           # App screens (LoginScreen, HomeScreen)
│   └── theme/             # Design system
└── MainActivity.kt         # Main activity
```

## Design Features

### Neumorphic Components
- **NeumorphicBox**: Container with soft shadows and highlights
- **NeumorphicButton**: Interactive buttons with press states
- **GradientBackground**: Animated gradient backgrounds

### Animations
- **Logo Animation**: Bouncy scale and rotation animations
- **Content Fade-in**: Staggered content appearance
- **Background Elements**: Floating animated elements
- **Loading States**: Smooth loading indicators

### Color Scheme
- **Brand Colors**: Purple, Pink, and Indigo gradients
- **Neumorphic Shadows**: Soft shadows for depth
- **Theme Support**: Automatic dark/light mode switching

## 🚀 Setup Instructions

### Prerequisites
- **Android Studio**: Arctic Fox or later
- **JDK**: 11 or later
- **Android SDK**: API level 24 (Android 7.0) or higher
- **Google Play Services**: Required for authentication

### 1. Clone and Setup
```bash
git clone <repository-url>
cd MySillyDreams/frontend/mobile
```

### 2. Configure Environment Variables
Create a `.env` file in the project root:

```env
# Google OAuth Configuration
GOOGLE_WEB_CLIENT_ID=your-web-client-id-here

# Firebase Configuration
FIREBASE_PROJECT_ID=your-project-id
FIREBASE_PROJECT_NUMBER=your-project-number
FIREBASE_API_KEY=your-api-key

# App Configuration
APP_PACKAGE_NAME=com.mysillydreams
```

**Important**: The `.env` file is excluded from version control. You must create this file with your Firebase configuration values.

### 3. Firebase Setup
1. **Create Firebase Project**: Go to [Firebase Console](https://console.firebase.google.com/)
2. **Add Android App**: Use package name `com.mysillydreams`
3. **Download google-services.json**: Place in `app/` directory
4. **Enable Authentication**: Enable Google sign-in method
5. **Add SHA-1 Fingerprint**: Add your debug/release SHA-1 fingerprints

### 4. Build and Run
```bash
# Debug build
./gradlew assembleDebug

# Install on connected device
./gradlew installDebug

# Generate SHA-1 fingerprint
./gradlew signingReport
```

## 🔐 Security Features

- **Environment Variables**: Sensitive data stored securely outside source code
- **BuildConfig Integration**: Runtime access to configuration values
- **Credential Manager**: Modern Android authentication API
- **Firebase Security**: Industry-standard authentication backend
- **Git Security**: Sensitive files excluded from version control

## 🧪 Testing

### Debug Testing
```bash
# Generate debug SHA-1 fingerprint
./gradlew signingReport

# Install debug APK
./gradlew installDebug

# View logs
adb logcat | grep MySillyDreams
```

### Supported Devices
- **Minimum SDK**: API 24 (Android 7.0)
- **Target SDK**: API 34 (Android 14)
- **Architecture**: ARM64, ARM, x86, x86_64
- **Screen Sizes**: Phone, Tablet, Foldable

## 🚨 Troubleshooting

### Common Issues
1. **Build Errors**: Ensure all dependencies are synced
2. **Authentication Fails**: Check SHA-1 fingerprint in Firebase Console
3. **Configuration Missing**: Verify `.env` file exists with correct values
4. **Google Play Services**: Ensure updated on test device

### Debug Commands
```bash
# Clean build
./gradlew clean assembleDebug

# Check configuration
./gradlew processDebugGoogleServices

# View detailed logs
adb logcat | grep MySillyDreams
```

## 📄 License

This project is part of the MySillyDreams platform.

---

**Note**: This app requires proper Firebase configuration to function. Ensure you have completed the Firebase setup steps before building and testing.
