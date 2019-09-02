
# react-native-trueconf-react-sdk

## Getting started

`$ npm install react-native-trueconf-react-sdk --save`

### Mostly automatic installation

`$ react-native link react-native-trueconf-react-sdk`

### Manual installation


#### iOS

1. In XCode, in the project navigator, right click `Libraries` ➜ `Add Files to [your project's name]`
2. Go to `node_modules` ➜ `react-native-trueconf-react-sdk` and add `RNTrueconfReactSdk.xcodeproj`
3. In XCode, in the project navigator, select your project. Add `libRNTrueconfReactSdk.a` to your project's `Build Phases` ➜ `Link Binary With Libraries`
4. Run your project (`Cmd+R`)<

#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `import com.reactlibrary.RNTrueconfReactSdkPackage;` to the imports at the top of the file
  - Add `new RNTrueconfReactSdkPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-trueconf-react-sdk'
  	project(':react-native-trueconf-react-sdk').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-trueconf-react-sdk/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':react-native-trueconf-react-sdk')
  	```

#### Windows
[Read it! :D](https://github.com/ReactWindows/react-native)

1. In Visual Studio add the `RNTrueconfReactSdk.sln` in `node_modules/react-native-trueconf-react-sdk/windows/RNTrueconfReactSdk.sln` folder to their solution, reference from their app.
2. Open up your `MainPage.cs` app
  - Add `using Trueconf.React.Sdk.RNTrueconfReactSdk;` to the usings at the top of the file
  - Add `new RNTrueconfReactSdkPackage()` to the `List<IReactPackage>` returned by the `Packages` method


## Usage
```javascript
import RNTrueconfReactSdk from 'react-native-trueconf-react-sdk';

// TODO: What to do with the module?
RNTrueconfReactSdk;
```
  