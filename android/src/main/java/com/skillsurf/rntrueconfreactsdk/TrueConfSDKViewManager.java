package com.skillsurf.rntrueconfreactsdk;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;

//import android.support.v4.app.Fragment;
//import androidx.legacy.app.Fragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.SimpleViewManager;
import com.google.android.gms.maps.MapFragment;
import com.pmt.mobiapp.MainActivity;
import com.pmt.mobiapp.MainApplication;
import com.pmt.mobiapp.R;
import com.skillsurf.rntrueconfreactsdk.TrueConfSDKViewManager;
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

  private ThemedReactContext reactContext;
  private TrueConfSDKView view;

  @Override
  public String getName() {
    return REACT_CLASS;
  }

  @Override
  public TrueConfSDKView createViewInstance(ThemedReactContext reactContext) {
    this.reactContext = reactContext;

    view = new TrueConfSDKView(reactContext, this);
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
