
# react-native-trueconf-sdk

## Preview

**iOS preview**

TODO

**Android preview**

[Android Preview](https://github.com/kesha-antonov/react-native-trueconf-sdk/assets/11584712/42e03f89-d5d6-4506-b18b-661656cb51a8)

## Getting started

`$ npm install react-native-trueconf-sdk --save`

or

`$ yarn add react-native-trueconf-sdk`

Then

`$ cd ios && pod install`

### Extra steps for Android

1. In `MainApplication.kt` add the following code:

```kotlin
import com.trueconf.sdk.TrueConfSDK

...

  override fun onCreate() {
    ...

    TrueConfSDK.getInstance().registerApp(this)
    TrueConfSDK.getInstance().setFallbackActivity(MainActivity::class.java)
  }

```

2. In `build.gradle` add the following code with your TrueConf credentials:

```gradle
buildscript {
    ext {
        ...
        trueConfSDKRepositoryUsername = "trueconfUsername"
        trueConfSDKRepositoryPassword = "trueconfPassword"
        ...
    }
    ...
}

apply from: file("../node_modules/react-native-trueconf-sdk/android/shared.gradle")
```

## Usage

See `example` folder for usage example.

### Methods

#### `showCallWindow` (Android only)

Brings up the TrueConf call screen.

```javascript
trueConfRef.current?.showCallWindow()
```

### Props

#### `isFadeTransitionEnabled` (Android only)

Enables or disables fade transition when call window hides.

To use fade out animation this code to `MainActivity.kt`:

```kotlin
class MainActivity : ReactActivity() {

  ...

  override fun onNewIntent(intent: Intent) {
    super.onNewIntent(intent)

    val isTrueConfSdkHideCallWindow:Boolean = intent.getBooleanExtra("isTrueConfSdkHideCallWindow", false)
    if (isTrueConfSdkHideCallWindow)
      overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
  }
}
```

## TODO

### Android
- [ ] (planned: v2) Allow customize buttons colors and icons
- [ ] (planned: v3) Use modern event emitter: https://github.com/facebook/react-native/blob/cb2b265c20f0622dec37c8b95c0380f78cb0877b/packages/react-native/ReactAndroid/src/main/java/com/facebook/react/uimanager/events/RCTModernEventEmitter.java#L22
- [ ] (planned: v3) Add slide up/down transition https://developer.android.com/guide/fragments/animate
- [ ] (planned: sdk issue) Add support to Android 14. Crashes on boot with error https://stackoverflow.com/questions/77235063/one-of-receiver-exported-or-receiver-not-exported-should-be-specified-when-a-rec (Support will be added to major update of TCSDK in summer of 2024)

### iOS
- [ ] Fix iOS crash when mute camera
- [ ] Fix iOS connect on real device
- [ ] Check that mute mic and camera works on iOS and it's confirmed in Privacy section on top of system screen

### General
- [ ] Describe which permissions are needed for iOS and where to put them (like here: https://github.com/zoontek/react-native-permissions?tab=readme-ov-file#ios)
- [ ] (planned: v3) Add react-navigation to example and more examples from TrueConf SDK Docs

## Notes

- Extensive guide on Android Fragments in React Native: https://stefan-majiros.com/blog/native-android-fragments-in-react-native/
- Android docs: https://developer.android.com/guide/fragments
- RN docs: https://reactnative.dev/docs/native-components-android?android-language=java#integration-with-an-android-fragment-example

- [Android] Rounded borders can't be applied to SelfView for now. Support will be added to major update of TCSDK in summer of 2024
