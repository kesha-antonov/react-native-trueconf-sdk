package com.kesha_antonov.react_native_true_conf_sdk;

import android.view.Choreographer;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.uimanager.annotations.ReactPropGroup;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.ThemedReactContext;
import android.view.ViewGroup;

import java.util.Map;

import com.trueconf.sdk.TrueConfSDK;
import com.kesha_antonov.react_native_true_conf_sdk.MyFragment;

import android.util.Log;

public class TrueConfSDKViewManager extends ViewGroupManager<FrameLayout> {

  public static final String REACT_CLASS = "TrueConfSDKViewManager";
  public static final String TAG = "TrueConfSDKViewManager";
  // COMMANDS
  public final int COMMAND_CREATE = 1;
  public final int COMMAND_INIT_SDK = 2;
  // PROPS
  private int propWidth;
  private int propHeight;

  ReactApplicationContext reactContext;

  public TrueConfSDKViewManager(ReactApplicationContext reactContext) {
    this.reactContext = reactContext;
  }

  @Override
  public String getName() {
    return REACT_CLASS;
  }

  /**
   * Return a FrameLayout which will later hold the Fragment
   */
  @Override
  public FrameLayout createViewInstance(ThemedReactContext reactContext) {
    return new FrameLayout(reactContext);
  }

  @Nullable
  @Override
  public Map<String, Integer> getCommandsMap() {
    return MapBuilder.of(
      "create", COMMAND_CREATE,
      "initSdk", COMMAND_INIT_SDK
    );
  }

  /**
   * Handle command (called from JS)
   */
  @Override
  public void receiveCommand(
    @NonNull FrameLayout root,
    String commandId,
    @Nullable ReadableArray args
  ) {
      Log.d(TAG, "receiveCommand: " + commandId);

      super.receiveCommand(root, commandId, args);
      int reactNativeViewId = args.getInt(0);
      int commandIdInt = Integer.parseInt(commandId);

      switch (commandIdInt) {
        case COMMAND_CREATE:
          createFragment(root, reactNativeViewId);
          break;
        case COMMAND_INIT_SDK:
          initSdk();
          break;
        default: {}
      }
  }

  @ReactPropGroup(names = {"width", "height"}, customType = "Style")
  public void setStyle(FrameLayout view, int index, @Nullable Integer value) {
    if (index == 0) {
      propWidth = value != null ? value : 0;
    }

    if (index == 1) {
      propHeight = value != null ? value : 0;
    }
  }

  /**
   * Replace your React Native view with a custom fragment
   */
  public void createFragment(FrameLayout root, int reactNativeViewId) {
    ViewGroup parentView = (ViewGroup) root.findViewById(reactNativeViewId);
    setupLayout(parentView);

    final MyFragment myFragment = new MyFragment();
    FragmentActivity activity = (FragmentActivity) reactContext.getCurrentActivity();
    activity.getSupportFragmentManager()
      .beginTransaction()
      .replace(reactNativeViewId, myFragment, String.valueOf(reactNativeViewId))
      .commit();
  }

  public void setupLayout(View view) {
    Choreographer.getInstance().postFrameCallback(new Choreographer.FrameCallback() {
      @Override
      public void doFrame(long frameTimeNanos) {
        manuallyLayoutChildren(view);
        view.getViewTreeObserver().dispatchOnGlobalLayout();
        Choreographer.getInstance().postFrameCallback(this);
      }
    });
  }

  /**
   * Layout all children properly
   */
  public void manuallyLayoutChildren(View view) {
    if (propWidth > 0 && propHeight > 0) {
      // propWidth and propHeight coming from react-native props
      int width = propWidth;
      int height = propHeight;

      view.measure(
          View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
          View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY));

      view.layout(0, 0, width, height);
    } else {
      ViewGroup parentView = (ViewGroup) view.getParent();

      for (int i = 0; i < parentView.getChildCount(); i++) {
        View child = parentView.getChildAt(i);
        child.measure(
            View.MeasureSpec.makeMeasureSpec(parentView.getMeasuredWidth(), View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(parentView.getMeasuredHeight(), View.MeasureSpec.EXACTLY));

        child.layout(0, 0, child.getMeasuredWidth(), child.getMeasuredHeight());
      }
    }
  }


  // @Override
  // public TrueConfSDKView createViewInstance(ThemedReactContext reactContext) {
  //   return new TrueConfSDKView(reactContext, this);
  // }



  // @ReactProp(name = "server")
  // public void setServer(TrueConfSDKView view, @Nullable String server) {
  //   Log.d(REACT_CLASS, "setServer: " + server.toString());
  //   if (server == null) { return; }
  //   view.setServer(server);
  // }

  private void initSdk() {
    Log.d(TAG, "initSdk");

    // TrueConfSDK.getInstance().start(reactContext.getApplicationContext(), "ru10.trueconf.net", true);

    // TrueConfSDK.getInstance().setPlaceCallFragment(new MyFragment());
    // TrueConfSDK.getInstance().setReceiveCallFragment(new MyFragment());
  }
}
