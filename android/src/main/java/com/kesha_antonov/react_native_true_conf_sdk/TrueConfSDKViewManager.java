package com.kesha_antonov.react_native_true_conf_sdk;

import android.view.Choreographer;
import android.view.ViewGroup;
import android.view.View;
import android.view.WindowManager;
import android.view.Gravity;
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
import com.facebook.react.uimanager.events.RCTEventEmitter;

import java.util.ArrayList;
import java.util.Map;

import com.trueconf.sdk.TrueConfSDK;
import com.trueconf.sdk.TrueConfSDK;
import com.trueconf.sdk.data.TCSDKExtraButton;
import com.trueconf.sdk.interfaces.TrueConfListener;
import com.vc.data.contacts.PeerDescription;
import com.vc.data.enums.ConnectionEvents;
import com.vc.data.enums.PeerStatus;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.UiThreadUtil;
import com.facebook.react.bridge.WritableMap;
import androidx.fragment.app.FragmentResultListener;
import android.os.Bundle;

import android.util.Log;

public class TrueConfSDKViewManager extends ViewGroupManager<FrameLayout>
    implements TrueConfListener.LoginEventsCallback, TrueConfListener.ConferenceEventsCallback,
    TrueConfListener.ServerStatusEventsCallback, TrueConfListener.ChatEventsCallback,
    TrueConfListener.UserStatusEventsCallback {

  public static final String REACT_CLASS = "TrueConfSDKViewManager";
  public static final String TAG = "TrueConfSDKViewManager";
  int savedRNViewId;
  // COMMANDS
  public final int COMMAND_CREATE = 1;
  public final int COMMAND_INIT_SDK = 2;
  public final int COMMAND_LOGIN = 3;
  public final int COMMAND_JOIN_CONF = 4;
  // PROPS
  private int propWidth;
  private int propHeight;

  private String server = "";
  private boolean isMuted = false;
  private boolean isCameraOn = true;

  ReactApplicationContext reactContext;

  // EVENTS
  private final static String CONNECTED = "isConnected";
  private final static String SERVER_NAME = "serverName";
  private final static String SERVER_PORT = "serverPort";
  private final static String IS_LOGGED_IN = "isLoggedIn";
  private final static String USER_ID = "userId";
  private final static String USER_NAME = "userName";
  private final static String USER_STATUS = "state";
  private final static String FROM_USER_ID = "fromUserId";
  private final static String FROM_USER_NAME = "fromUserName";
  private final static String MESSAGE = "message";
  private final static String TO_USER_ID = "toUserId";
  private final static String ON_SERVER_STATUS = "onServerStatus";
  private final static String ON_LOGIN = "onLogin";
  private final static String ON_LOGOUT = "onLogout";
  private final static String ON_SERVER_STATE_CHANGED = "onServerStateChanged";
  private final static String ON_CONFERENCE_START = "onConferenceStart";
  private final static String ON_CONFERENCE_END = "onConferenceEnd";
  private final static String ON_INVITE = "onInvite";
  private final static String ON_RECORD_REQUEST = "onRecordRequest";
  private final static String ON_ACCEPT = "onAccept";
  private final static String ON_REJECT = "onReject";
  private final static String ON_REJECT_TIMEOUT = "onRejectTimeout";
  private final static String ON_USER_STATUS_UPDATE = "onUserStatusUpdate";
  private final static String ON_CHAT_MESSAGE_RECEIVED = "onChatMessageReceived";
  private final static String ON_EXTRA_BUTTON_PRESSED = "onExtraButtonPressed";
  private final static String ON_PRESS_BUTTON = "onPressButton";

  @Override
  public void onServerStatus(final boolean connected, final String serverName, final int serverPort) {
    WritableMap params = Arguments.createMap();
    params.putBoolean(CONNECTED, connected);
    params.putString(SERVER_NAME, serverName);
    params.putInt(SERVER_PORT, serverPort);
    emitMessageToRN(ON_SERVER_STATUS, params);
  }

  @Override
  public void onLogin(final boolean isLoggedIn, final String userId) {
    WritableMap params = Arguments.createMap();
    params.putBoolean(IS_LOGGED_IN, isLoggedIn);
    params.putString(USER_ID, userId);
    emitMessageToRN(ON_LOGIN, params);
  }

  @Override
  public void onLogout() {
    emitMessageToRN(ON_LOGOUT, null);
  }

  @Override
  public void onStateChanged(final ConnectionEvents connectionEvents, final int i) {
    emitMessageToRN(ON_SERVER_STATE_CHANGED, null);
  }

  @Override
  public void onConferenceStart() {
    UiThreadUtil.runOnUiThread(new Runnable() {
      @Override
      public void run() {
        emitMessageToRN(ON_CONFERENCE_START, null);
      }
    });
  }

  @Override
  public void onConferenceEnd() {
    emitMessageToRN(ON_CONFERENCE_END, null);
  }

  @Override
  public void onInvite(final String userId, final String userName) {
    WritableMap params = Arguments.createMap();
    params.putString(USER_ID, userId);
    params.putString(USER_NAME, userName);
    emitMessageToRN(ON_INVITE, params);
  }

  @Override
  public void onRecordRequest(final String userId, final String userName) {
    WritableMap params = Arguments.createMap();
    params.putString(USER_ID, userId);
    params.putString(USER_NAME, userName);
    emitMessageToRN(ON_RECORD_REQUEST, params);
  }

  @Override
  public void onAccept(final String userId, final String userName) {
    WritableMap params = Arguments.createMap();
    params.putString(USER_ID, userId);
    params.putString(USER_NAME, userName);
    emitMessageToRN(ON_ACCEPT, params);
  }

  @Override
  public void onReject(final String userId, final String userName) {
    WritableMap params = Arguments.createMap();
    params.putString(USER_ID, userId);
    params.putString(USER_NAME, userName);
    emitMessageToRN(ON_REJECT, params);
  }

  @Override
  public void onRejectTimeout(final String userId, final String userName) {
    WritableMap params = Arguments.createMap();
    params.putString(USER_ID, userId);
    params.putString(USER_NAME, userName);
    emitMessageToRN(ON_REJECT_TIMEOUT, params);
  }

  @Override
  public void onUserStatusUpdate(final String userId, final PeerStatus state) {
    WritableMap params = Arguments.createMap();
    params.putString(USER_ID, userId);
    params.putInt(USER_STATUS, getUnifiedUserStatus(state));
    emitMessageToRN(ON_USER_STATUS_UPDATE, params);
  }

  @Override
  public void onContactListUpdate() {
    var peerList = TrueConfSDK.getInstance().getUsers();
    for (PeerDescription p : peerList) {
      WritableMap params = Arguments.createMap();
      params.putString(USER_ID, p.getId());
      params.putInt(USER_STATUS, getUnifiedUserStatus(p.getPeerStatusInfo().getPeerStatus()));
      emitMessageToRN(ON_USER_STATUS_UPDATE, params);
    }
  }

  @Override
  public void onChatMessageReceived(final String fromUserID, final String fromUserName, final String message,
      final String toUserID) {
    WritableMap params = Arguments.createMap();
    params.putString(FROM_USER_ID, fromUserID);
    params.putString(FROM_USER_NAME, fromUserName);
    params.putString(MESSAGE, message);
    params.putString(TO_USER_ID, toUserID);
    emitMessageToRN(ON_CHAT_MESSAGE_RECEIVED, params);
  }

  private int getUnifiedUserStatus(PeerStatus state) {
    switch (state.ordinal()) {
      case 0:
        return -127;
      case 1:
        return -1;
      case 2:
        return 0;
      case 3:
        return 1;
      case 4:
        return 2;
      case 5:
        return 3;
      case 6:
        return 4;
      case 7:
        return 5;
    }
    return -127;
  }

  public void onPressButton(String kind) {
    Log.d(TAG, "onPressButton");
    WritableMap params = Arguments.createMap();
    params.putString("kind", kind);
    emitMessageToRN(ON_PRESS_BUTTON, params);
  }

  public Map getExportedCustomBubblingEventTypeConstants() {
    return MapBuilder.builder()
      .put(ON_SERVER_STATUS, MapBuilder.of("phasedRegistrationNames", MapBuilder.of("bubbled", ON_SERVER_STATUS)))
      .put(ON_SERVER_STATUS, MapBuilder.of("phasedRegistrationNames", MapBuilder.of("bubbled", ON_SERVER_STATUS)))
      .put(ON_LOGIN, MapBuilder.of("phasedRegistrationNames", MapBuilder.of("bubbled", ON_LOGIN)))
      .put(ON_LOGOUT, MapBuilder.of("phasedRegistrationNames", MapBuilder.of("bubbled", ON_LOGOUT)))
      .put(ON_SERVER_STATE_CHANGED, MapBuilder.of("phasedRegistrationNames", MapBuilder.of("bubbled", ON_SERVER_STATE_CHANGED)))
      .put(ON_CONFERENCE_START, MapBuilder.of("phasedRegistrationNames", MapBuilder.of("bubbled", ON_CONFERENCE_START)))
      .put(ON_CONFERENCE_END, MapBuilder.of("phasedRegistrationNames", MapBuilder.of("bubbled", ON_CONFERENCE_END)))
      .put(ON_INVITE, MapBuilder.of("phasedRegistrationNames", MapBuilder.of("bubbled", ON_INVITE)))
      .put(ON_RECORD_REQUEST, MapBuilder.of("phasedRegistrationNames", MapBuilder.of("bubbled", ON_RECORD_REQUEST)))
      .put(ON_ACCEPT, MapBuilder.of("phasedRegistrationNames", MapBuilder.of("bubbled", ON_ACCEPT)))
      .put(ON_REJECT, MapBuilder.of("phasedRegistrationNames", MapBuilder.of("bubbled", ON_REJECT)))
      .put(ON_REJECT_TIMEOUT, MapBuilder.of("phasedRegistrationNames", MapBuilder.of("bubbled", ON_REJECT_TIMEOUT)))
      .put(ON_USER_STATUS_UPDATE, MapBuilder.of("phasedRegistrationNames", MapBuilder.of("bubbled", ON_USER_STATUS_UPDATE)))
      .put(ON_CHAT_MESSAGE_RECEIVED, MapBuilder.of("phasedRegistrationNames", MapBuilder.of("bubbled", ON_CHAT_MESSAGE_RECEIVED)))
      .put(ON_EXTRA_BUTTON_PRESSED, MapBuilder.of("phasedRegistrationNames", MapBuilder.of("bubbled", ON_EXTRA_BUTTON_PRESSED)))
      .put(ON_PRESS_BUTTON, MapBuilder.of("phasedRegistrationNames", MapBuilder.of("bubbled", ON_PRESS_BUTTON)))
      .build();
  }

  private void emitMessageToRN(String eventName, @Nullable WritableMap params) {
    // reactContext
    //     .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
    //     .emit(eventName, params);

    Log.d(TAG, "emitMessageToRN: " + eventName);

    reactContext
      .getJSModule(RCTEventEmitter.class)
      .receiveEvent(savedRNViewId, eventName, params);
  }

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
      "initSdk", COMMAND_INIT_SDK,
      "login", COMMAND_LOGIN,
      "joinConf", COMMAND_JOIN_CONF
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

      int commandIdInt = Integer.parseInt(commandId);

      switch (commandIdInt) {
        case COMMAND_CREATE: {
          int reactNativeViewId = args.getInt(0);
          savedRNViewId = reactNativeViewId;
          // NOT USED FOR NOW
          // createFragment(root, reactNativeViewId);
          break;
        }
        case COMMAND_INIT_SDK: {
          initSdk();
          break;
        }
        case COMMAND_LOGIN: {
          String user = args.getString(0);
          String pwd = args.getString(1);
          boolean encryptPassword = args.getBoolean(2);
          boolean enableAutoLogin = args.getBoolean(3);
          boolean result = TrueConfSDK.getInstance().loginAs(user, pwd, encryptPassword, enableAutoLogin);
          break;
        }
        case COMMAND_JOIN_CONF: {
          String confId = args.getString(0);
          boolean result = TrueConfSDK.getInstance().joinConf(confId);
          break;
        }
        default: {}
      }
  }

  // --- PROPS ---

  @ReactPropGroup(names = {"width", "height"}, customType = "Style")
  public void setStyle(FrameLayout view, int index, @Nullable Integer value) {
    if (index == 0) {
      propWidth = value != null ? value : 0;
    }

    if (index == 1) {
      propHeight = value != null ? value : 0;
    }
  }

  @ReactProp(name = "server")
  public void setSrc(FrameLayout view, @Nullable String value) {
    server = value != null ? value : "";
  }

  @ReactProp(name = "isMuted")
  public void setIsMuted(FrameLayout view, @Nullable Boolean value) {
    isMuted = value != null ? value : false;
  }

  @ReactProp(name = "isCameraOn")
  public void setIsCameraOn(FrameLayout view, @Nullable Boolean value) {
    isCameraOn = value != null ? value : true;
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
    // propWidth and propHeight coming from react-native props
    int width = propWidth;
    int height = propHeight;

    if (width > 0 && height > 0) {
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

  private void initCustomViews () {
    Log.d(TAG, "initCustomViews");

    // final float scale = reactContext.getResources().getDisplayMetrics().density;
    // int height = (int) (400 * scale + 0.5f);
    WindowManager.LayoutParams params = new WindowManager.LayoutParams();
    params.width = WindowManager.LayoutParams.MATCH_PARENT;
    params.height = WindowManager.LayoutParams.MATCH_PARENT;
    params.gravity = Gravity.TOP;
    params.y = 0;
    TrueConfSDK.getInstance().setCallLayoutParams(params);

    TrueConfSDK.getInstance().setConferenceFragment(
      new ConferenceFragmentCast( R.layout.fragment_conference_cast, this )
    );

    // FragmentActivity activity = (FragmentActivity) reactContext.getCurrentActivity();
    // activity.getSupportFragmentManager().setFragmentResultListener("requestKey",
    //     activity, new FragmentResultListener() {
    //   @Override
    //   public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
    //     // We use a String here, but any type that can be put in a Bundle is supported.
    //     String result = bundle.getString("bundleKey");
    //     Log.d(TAG, "onFragmentResult result: " + result);
    //     // Do something with the result.
    //   }
    // });

    // TODO
    // TrueConfSDK.getInstance().setPlaceCallFragment(new PlaceCallFragmentCast(R.layout.fragment_conference_cast));
    // TrueConfSDK.getInstance().setReceiveCallFragment(new IncomingCallFragmentCast(R.layout.fragment_conference_cast));
  }

  private void initEvents () {
    Log.d(TAG, "initEvents");

    TrueConfSDK.getInstance().addTrueconfListener(this);
  }

  private void initSdk() {
    Log.d(TAG, "initSdk server: " + server + " isMuted: " + isMuted + " isCameraOn: " + isCameraOn);

    initCustomViews();
    initEvents();

    TrueConfSDK.getInstance().start(server, true);

    TrueConfSDK.getInstance().muteMicrophone(isMuted);
    TrueConfSDK.getInstance().muteCamera(isCameraOn);
  }

  //

  // @ReactMethod
  // public void start(String serverList) {
  //     if(serverList != null && !serverList.isEmpty()) {
  //         TrueConfSDK.getInstance().start(serverList, true);
  //     } else {
  //         TrueConfSDK.getInstance().start(true);
  //     }
  //     TrueConfSDK.getInstance().addTrueconfListener(this);
  // }

  // @ReactMethod
  // public void stop() {
  //     TrueConfSDK.getInstance().stop();
  //     TrueConfSDK.getInstance().removeTrueconfListener(this);
  // }

  // @ReactMethod
  // public void addExtraButton(String title, Promise promise) {
  //     try {
  //         ArrayList<TCSDKExtraButton> buttons = new ArrayList<>();
  //         View.OnClickListener onClickListener = btn -> {
  //             UiThreadUtil.runOnUiThread(new Runnable() {
  //                 @Override
  //                 public void run() {
  //                     RNTrueConfSdkModule.this.emitMessageToRN(context, ON_EXTRA_BUTTON_PRESSED, null);
  //                 }
  //             });
  //         };
  //         buttons.add(new TCSDKExtraButton(title, onClickListener));
  //         TrueConfSDK.getInstance().setNewExtraButtons(buttons);
  //         promise.resolve(true);
  //     } catch(Exception e) {
  //         promise.reject("failed to set extra button: " + e.getMessage());
  //     }
  // }

  // @ReactMethod
  // public void showAlertPage(String text) {
  //     try {
  //         Intent intent = new Intent(context, DialogActivity.class);
  //         intent.putExtra("ALERT_TEXT", text);
  //         intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
  //         context.startActivity(intent);
  //     } catch (Exception e) {
  //         Log.e(getClass().getName(), "failed to display alert activity: " + e.getMessage());
  //     }
  // }

  // @ReactMethod
  // public void logout(Promise promise) {
  //     boolean result = TrueConfSDK.getInstance().logout();
  //     promise.resolve(result);
  // }

  // @ReactMethod
  // public void callTo(String userId, Promise promise) {
  //     boolean result = TrueConfSDK.getInstance().callTo(userId);
  //     promise.resolve(result);
  // }

  // @ReactMethod
  // public void hangup(boolean forAll, Promise promise) {
  //     boolean result = TrueConfSDK.getInstance().hangup(forAll);
  //     promise.resolve(result);
  // }

  // @ReactMethod
  // public void acceptCall(boolean accept, Promise promise) {
  //     boolean result = TrueConfSDK.getInstance().acceptCall(accept);
  //     promise.resolve(result);
  // }

  // @ReactMethod
  // public void acceptRecord(boolean accept, String userID) {
  //     TrueConfSDK.getInstance().acceptRecord(accept, userID);
  // }

  // @ReactMethod
  // public void sendChatMessage(String userID, String message, Promise promise) {
  //     TrueConfSDK.getInstance().sendChatMessage(userID, message);
  // }

  // @ReactMethod
  // public void parseProtocolLink(String cmd) {
  //     TrueConfSDK.getInstance().parseProtocolLink(context, cmd);
  // }

  // @ReactMethod
  // public void scheduleLoginAs(String login, String pwd, boolean encryptPassword, String callToUser, boolean autoClose, boolean loginTemp, boolean loginForce, String domain, String serversList, boolean isPublic) {
  //     TrueConfSDK.getInstance().scheduleLoginAs(login, pwd, encryptPassword, callToUser, autoClose, loginTemp, loginForce, domain, serversList, isPublic);
  // }

  // @ReactMethod
  // public void muteMicrophone(boolean mute) {
  //     TrueConfSDK.getInstance().muteMicrophone(mute);
  // }

  // @ReactMethod
  // public void muteCamera(boolean mute) {
  //     TrueConfSDK.getInstance().muteCamera(mute);
  // }

  // @ReactMethod
  // public void getMyId(Promise promise) {
  //     String myId = TrueConfSDK.getInstance().getMyId();
  //     promise.resolve(myId);
  // }

  // @ReactMethod
  // public void getMyName(Promise promise) {
  //     String myName = TrueConfSDK.getInstance().getMyName();
  //     promise.resolve(myName);
  // }

  // @ReactMethod
  // public void isStarted(Promise promise) {
  //     boolean result = TrueConfSDK.getInstance().isStarted();
  //     promise.resolve(result);
  // }

  // @ReactMethod
  // public void isConnectedToServer(Promise promise) {
  //     boolean result = TrueConfSDK.getInstance().isConnectedToServer();
  //     promise.resolve(result);
  // }

  // @ReactMethod
  // public void isLoggedIn(Promise promise) {
  //     boolean result = TrueConfSDK.getInstance().isLoggedIn();
  //     promise.resolve(result);
  // }

  // @ReactMethod
  // public void isInConference(Promise promise) {
  //     boolean result = TrueConfSDK.getInstance().isInConference();
  //     promise.resolve(result);
  // }

  // @ReactMethod
  // public void getUserStatus(String user, Promise promise) {
  //     int userStatus = getUnifiedUserStatus(TrueConfSDK.getInstance().getUserStatus(user));
  //     promise.resolve(userStatus);
  //     UiThreadUtil.runOnUiThread(new Runnable() {
  //         @Override
  //         public void run() {
  //             WritableMap params = Arguments.createMap();
  //             params.putString(USER_ID, user);
  //             params.putInt(USER_STATUS, userStatus);
  //             emitMessageToRN(context, ON_USER_STATUS_UPDATE, params);
  //         }
  //     });
  // }

  // @ReactMethod
  // public void microphoneMuted(Promise promise) {
  //     boolean result = TrueConfSDK.getInstance().isMicrophoneMuted();
  //     promise.resolve(result);
  // }

  // @ReactMethod
  // public void cameraMuted(Promise promise) {
  //     boolean result = TrueConfSDK.getInstance().isCameraMuted();
  //     promise.resolve(result);
  // }
}
