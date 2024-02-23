
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
    TrueConfSDK.getInstance().setFallbackActivity(MainActivity.class)
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
- [ ] Check that example works for Android

### General
- [ ] Add react-navigation to example and more examples from TrueConf SDK Docs
