package com.kesha_antonov.rn_trueconf_react_sdk;

import android.util.Log;

import com.facebook.react.ReactActivity;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.SimpleViewManager;
import com.kesha_antonov.rn_trueconf_react_sdk.TrueConfSDKViewManager;
import com.facebook.react.uimanager.annotations.ReactProp;
import javax.annotation.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.vc.TrueConfSDK;

public class TrueConfSDKViewManager extends SimpleViewManager<TrueConfSDKView> {

  public static final String REACT_CLASS = "RCTTrueConfSDKView";
  public static final String TAG = "RCTTrueConfSDKView";
  public static final int COMMAND_INIT_SDK = 0;

  ReactApplicationContext mCallerContext;
  private ThemedReactContext reactContext;
  private TrueConfSDKView view;

  public TrueConfSDKViewManager(ReactApplicationContext reactContext) {
    mCallerContext = reactContext;
  }

  @Override
  public String getName() {
    return REACT_CLASS;
  }

  @Override
  public TrueConfSDKView createViewInstance(ThemedReactContext reactContext) {
    this.reactContext = reactContext;

    view = new TrueConfSDKView(mCallerContext, this);
    return view;
  }



  @ReactProp(name = "server")
  public void setServer(TrueConfSDKView view, @Nullable String server) {
    Log.d(REACT_CLASS, "setServer: " + server.toString());
    if (server == null) { return; }
    view.setServer(server);
  }

  @Nullable
  @Override
  public Map<String, Integer> getCommandsMap() {
      return MapBuilder.of(
              "initSdk", COMMAND_INIT_SDK
      );

  }

  @Override
  public void receiveCommand(TrueConfSDKView view, int commandId, @Nullable ReadableArray args) {
      Log.d(TAG, "receiveCommand: " + commandId);
      switch (commandId) {
          case COMMAND_INIT_SDK:
              initSdk();
              break;
      }
  }

  private void initSdk() {
    Log.d(TAG, "initSdk");

    TrueConfSDK.getInstance().start(reactContext.getApplicationContext(), "ru10.trueconf.net", true);

    TrueConfSDK.getInstance().setPlaceCallFragment(new MyFragment());
    TrueConfSDK.getInstance().setReceiveCallFragment(new MyFragment());
  }
}
