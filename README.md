
# react-native-trueconf-sdk

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


## TODO

### iOS
- [ ] Fix iOS crash when mute camera
- [ ] Fix iOS connect on real device
- [ ] Check that mute mic and camera works on iOS and it's confirmed in Privacy section on top of system screen

### Android
- [ ] Add support for Android
- [ ] Add support to Android 14. Crashes on boot with error https://stackoverflow.com/questions/77235063/one-of-receiver-exported-or-receiver-not-exported-should-be-specified-when-a-rec
- [ ] Check that example works for Android

### General
- [ ] Add react-navigation to example and more examples from TrueConf SDK Docs
- [ ] Describe which permissions are needed for Android and iOS and where to put them (like here: https://github.com/zoontek/react-native-permissions?tab=readme-ov-file#ios)

## Notes

- Extensive guide on Android Fragments in React Native: https://stefan-majiros.com/blog/native-android-fragments-in-react-native/
- Android docs: https://developer.android.com/guide/fragments
- RN docs: https://reactnative.dev/docs/native-components-android?android-language=java#integration-with-an-android-fragment-example
