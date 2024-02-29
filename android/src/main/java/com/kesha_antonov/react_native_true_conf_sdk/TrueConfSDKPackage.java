package com.kesha_antonov.react_native_true_conf_sdk;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;

import com.kesha_antonov.react_native_true_conf_sdk.TrueConfSDKViewManager;

public class TrueConfSDKPackage implements ReactPackage {

  @Override
  public List<NativeModule> createNativeModules(ReactApplicationContext reactContext) {
    return new ArrayList<>();
  }

  @Override
  public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {
    return Arrays.<ViewManager>asList(
      new TrueConfSDKViewManager(reactContext)
    );
  }

}
