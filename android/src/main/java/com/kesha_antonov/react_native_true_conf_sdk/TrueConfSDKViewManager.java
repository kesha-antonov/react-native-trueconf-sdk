package com.kesha_antonov.react_native_true_conf_sdk;

import android.app.Activity;
import android.content.Intent;
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

import java.util.Map;

import com.facebook.react.uimanager.events.RCTEventEmitter;
// TODO: USE IT
// https://github.com/facebook/react-native/blob/cb2b265c20f0622dec37c8b95c0380f78cb0877b/packages/react-native/ReactAndroid/src/main/java/com/facebook/react/uimanager/events/RCTModernEventEmitter.java#L22
// import com.facebook.react.uimanager.events.RCTModernEventEmitter;
import com.trueconf.sdk.TrueConfSDK;
import com.trueconf.sdk.interfaces.TrueConfListener;
import com.trueconf.sdk.gui.activities.CallCast;
import com.vc.data.contacts.PeerDescription;
import com.vc.data.enums.ConnectionEvents;
import com.vc.data.enums.PeerStatus;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;

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
  public final int COMMAND_SHOW_CALL_WINDOW = 5;
  public final int COMMAND_STOP_SDK = 6;
  public final int COMMAND_LOGOUT = 7;
  public final int COMMAND_CALL_TO = 8;
  public final int COMMAND_HANGUP = 9;
  public final int COMMAND_ACCEPT_CALL = 10;
  public final int COMMAND_ACCEPT_RECORD = 11;
  public final int COMMAND_MUTE = 12;
  public final int COMMAND_MUTE_CAMERA = 13;
  public final int COMMAND_MUTE_AUDIO = 14;
  public final int COMMAND_SEND_CHAT_MESSAGE = 15;
  public final int COMMAND_PARSE_PROTOCOL_LINK = 16;
  public final int COMMAND_SCHEDULE_LOGIN_AS = 17;
  // PROPS
  private int propWidth;
  private int propHeight;

  private String server = "";
  public boolean isMicMuted = false;
  public boolean isCameraMuted = false;
  public boolean isAudioMuted = false;

  public boolean isInConference = false;
  ConferenceFragmentCast conferenceFragmentCast;

  public boolean isFadeTransitionEnabled = false;

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
  public void onServerStatus(final boolean isConnected, final String serverName, final int serverPort) {
    WritableMap params = Arguments.createMap();
    params.putBoolean(CONNECTED, isConnected);
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
    isInConference = true;
    conferenceFragmentCast.updateChatButtonVisibility();
    muteAudio(); // TEMPORARILY HERE SINCE BEFORE JOIN CONF WE CAN'T SET DEFAULT isAudioMuted ON TCSDK

    emitMessageToRN(ON_CONFERENCE_START, null);
  }

  @Override
  public void onConferenceEnd() {
    isInConference = false;
    conferenceFragmentCast.updateChatButtonVisibility();

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

  // BRINGS APP'S ACTIVITY TO FRONT
  private void hideCallWindow () {
    Activity activity = reactContext.getCurrentActivity();
    Intent intent = new Intent(reactContext, activity.getClass());
    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
    intent.putExtra("isTrueConfSdkHideCallWindow", true);

    activity.startActivity(intent);
  }

  // BRINGS LIB'S ACTIVITY TO FRONT
  private void showCallWindow () {
    // UPDATE DEFAULT VALUES SINCE WHEN WE BRING ACTIVITY TO TOP
    // ConferenceFragmentCast WILL BE RE-CREATED
    TrueConfSDK.getInstance().setDefaultAudioEnabled(!isMicMuted);
    TrueConfSDK.getInstance().setDefaultCameraEnabled(!isCameraMuted);

    Activity activity = reactContext.getCurrentActivity();
    Intent intent = new Intent(reactContext, CallCast.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

    activity.startActivity(intent);
    if (isFadeTransitionEnabled) {
      activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
  }

  public void onPressButton(String kind, @Nullable WritableMap params) {
    Log.d(TAG, "onPressButton kind: " + kind);
    if (params == null) {
      params = Arguments.createMap();
    }

    switch (kind) {
      case "chat":
        hideCallWindow();
        break;
      case "hangup":
        hideCallWindow();
        new Thread(() -> {
          try {
            // SET AT LEAST 400ms DELAY TO LET CALL WINDOW HIDE
            // OTHERWISE YOU'LL SEE WHITE FLICKER (FRAGMENT GETS REMOVED)
            Thread.sleep(400);
            hangup(true);
          } catch (InterruptedException e) {
            Log.e(TAG, "onPressButton hangup error: " + e.getMessage());
            e.printStackTrace();
          }
        }).start();
        break;
      default:
        break;
    }

    params.putString("kind", kind);
    emitMessageToRN(ON_PRESS_BUTTON, params);
  }

  public Map getExportedCustomBubblingEventTypeConstants() {
    return MapBuilder.builder()
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
    return MapBuilder.<String, Integer>builder()
      .put("create", COMMAND_CREATE)
      .put("initSdk", COMMAND_INIT_SDK)
      .put("login", COMMAND_LOGIN)
      .put("joinConf", COMMAND_JOIN_CONF)
      .put("showCallWindow", COMMAND_SHOW_CALL_WINDOW)
      .put("stopSdk", COMMAND_STOP_SDK)
      .put("logout", COMMAND_LOGOUT)
      .put("callTo", COMMAND_CALL_TO)
      .put("hangup", COMMAND_HANGUP)
      .put("acceptCall", COMMAND_ACCEPT_CALL)
      .put("acceptRecord", COMMAND_ACCEPT_RECORD)
      .put("mute", COMMAND_MUTE)
      .put("muteCamera", COMMAND_MUTE_CAMERA)
      .put("muteAudio", COMMAND_MUTE_AUDIO)
      .put("sendChatMessage", COMMAND_SEND_CHAT_MESSAGE)
      .put("parseProtocolLink", COMMAND_PARSE_PROTOCOL_LINK)
      .put("scheduleLoginAs", COMMAND_SCHEDULE_LOGIN_AS)
      .build();
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
          String password = args.getString(1);
          boolean encryptPassword = args.getBoolean(2);
          boolean enableAutoLogin = args.getBoolean(3);
          boolean result = TrueConfSDK.getInstance().loginAs(user, password, encryptPassword, enableAutoLogin);
          break;
        }
        case COMMAND_JOIN_CONF: {
          String confId = args.getString(0);

          Log.d(TAG, "joinConf isMicMuted " + isMicMuted + " isCameraMuted " + isCameraMuted + " isAudioMuted " + isAudioMuted);

          // SETS DEFAULT MIC/CAMERA VALUES
          TrueConfSDK.getInstance().setDefaultAudioEnabled(!isMicMuted);
          TrueConfSDK.getInstance().setDefaultCameraEnabled(!isCameraMuted);
          muteAudio(); // TODO: setDefaultSpeakerEnabled MISSING

          boolean result = TrueConfSDK.getInstance().joinConf(confId);
          break;
        }
        case COMMAND_SHOW_CALL_WINDOW: {
          showCallWindow();
          break;
        }
        case COMMAND_STOP_SDK: {
          stopSdk();
          break;
        }
        case COMMAND_LOGOUT: {
          logout();
          break;
        }
        case COMMAND_CALL_TO: {
          String userId = args.getString(0);
          callTo(userId);
          break;
        }
        case COMMAND_HANGUP: {
          boolean forAll = args.getBoolean(0);
          hangup(forAll);
          break;
        }
        case COMMAND_ACCEPT_CALL: {
          boolean accept = args.getBoolean(0);
          acceptCall(accept);
          break;
        }
        case COMMAND_ACCEPT_RECORD: {
          boolean accept = args.getBoolean(0);
          String userId = args.getString(1);
          acceptRecord(accept, userId);
          break;
        }
        case COMMAND_MUTE: {
          boolean _isMicMuted = args.getBoolean(0);
          isMicMuted = _isMicMuted;
          mute();
          break;
        }
        case COMMAND_MUTE_CAMERA: {
          boolean _isCameraMuted = args.getBoolean(0);
          isCameraMuted = _isCameraMuted;
          muteCamera();
          break;
        }
        case COMMAND_MUTE_AUDIO: {
          boolean _isAudioMuted = args.getBoolean(0);
          isAudioMuted = _isAudioMuted;
          muteAudio();
          break;
        }
        case COMMAND_SEND_CHAT_MESSAGE: {
          String userId = args.getString(0);
          String message = args.getString(1);
          sendChatMessage(userId, message);
          break;
        }
        case COMMAND_PARSE_PROTOCOL_LINK: {
          String cmd = args.getString(0);
          parseProtocolLink(cmd);
          break;
        }
        case COMMAND_SCHEDULE_LOGIN_AS: {
          String userId = args.getString(0);
          String password = args.getString(1);
          boolean encryptPassword = args.getBoolean(2);
          String callToUser = args.getString(3);
          boolean autoClose = args.getBoolean(4);
          boolean loginTemp = args.getBoolean(5);
          boolean loginForce = args.getBoolean(6);
          String domain = args.getString(7);
          String serversList = args.getString(8);
          boolean isPublic = args.getBoolean(9);
          scheduleLoginAs(userId, password, encryptPassword, callToUser, autoClose, loginTemp, loginForce, domain, serversList, isPublic);
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

  @ReactProp(name = "isMicMuted")
  public void setIsMicMuted(FrameLayout view, @Nullable Boolean value) {
    isMicMuted = value != null ? value : false;
  }

  @ReactProp(name = "isCameraMuted")
  public void setIsCameraMuted(FrameLayout view, @Nullable Boolean value) {
    isCameraMuted = value != null ? value : false;
  }

  @ReactProp(name = "isAudioMuted")
  public void setIsAudioMuted(FrameLayout view, @Nullable Boolean value) {
    isAudioMuted = value != null ? value : false;
  }

  @ReactProp(name = "isFadeTransitionEnabled")
  public void setIsFadeTransitionEnabled(FrameLayout view, @Nullable Boolean value) {
    isFadeTransitionEnabled = value != null ? value : false;
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


  private void initCustomViews () {
    Log.d(TAG, "initCustomViews");

    WindowManager.LayoutParams params = new WindowManager.LayoutParams();
    params.width = WindowManager.LayoutParams.MATCH_PARENT;
    params.height = WindowManager.LayoutParams.MATCH_PARENT;
    params.gravity = Gravity.TOP;
    params.y = 0;
    TrueConfSDK.getInstance().setCallLayoutParams(params);

    conferenceFragmentCast = new ConferenceFragmentCast(R.layout.fragment_conference_cast, this);

    // CALL FRAGMENT (SHOWED WHEN CALLING - JUST BEFORE JOINING CONF OR CALL)
    TrueConfSDK.getInstance().setPlaceCallFragment(conferenceFragmentCast);
    // CONFERENCE FGRAMENT
    TrueConfSDK.getInstance().setConferenceFragment(conferenceFragmentCast);

    // CAN CUSTOMIZE INCOMING CALL
    // TrueConfSDK.getInstance().setReceiveCallFragment(new IncomingCallFragmentCast(R.layout.fragment_conference_cast));
  }

  private void initEvents () {
    Log.d(TAG, "initEvents");

    TrueConfSDK.getInstance().addTrueconfListener(this);
  }

  private void initSdk() {
    Log.d(TAG, "initSdk server: " + server + " isMicMuted: " + isMicMuted + " isCameraMuted: " + isCameraMuted + " isAudioMuted: " + isAudioMuted);

    TrueConfSDK.getInstance().start(server, true);

    // INIT CUSTOM FRAGMENTS MUST BE INITED AFTER "start" CALL
    // OTHERWISE THEY WON'T BE USED
    initCustomViews();

    // EVENTS MUST BE INITED AFTER "start" CALL
    // OTHERWISE ON FIRST "initSdk" WE WON'T RECEIVE FIRST EVENTS
    // MUST CALL AFTER "initCustomViews" SINCE WE NEED TO START CALL WITH CUSTOM FRAGMENTS
    initEvents();
  }

  private void stopSdk() {
    TrueConfSDK.getInstance().stop();
    logout();
    removeListeners();
  }

  private void removeListeners() {
    TrueConfSDK.getInstance().removeTrueconfListener(this);
  }

  private void logout() {
    boolean result = TrueConfSDK.getInstance().logout();
    Log.d(TAG, "logout: result " + result);
  }

  private void callTo(String userId) {
    boolean result = TrueConfSDK.getInstance().callTo(userId);
    Log.d(TAG, "callTo: result " + result);
  }

  private void hangup(boolean forAll) {
    boolean result = TrueConfSDK.getInstance().hangup(forAll);
    Log.d(TAG, "hangup: result " + result);
  }

  private void acceptCall(boolean accept) {
    boolean result = TrueConfSDK.getInstance().acceptCall(accept);
    Log.d(TAG, "acceptCall: result " + result);
  }

  private void acceptRecord(boolean accept, String userId) {
    TrueConfSDK.getInstance().acceptRecord(accept, userId);
  }

  private void mute() {
    Log.d(TAG, "mute isMicMuted " + isMicMuted);
    TrueConfSDK.getInstance().muteMicrophone(isMicMuted);
  }

  private void muteCamera() {
    TrueConfSDK.getInstance().muteCamera(isCameraMuted);
  }

  private void muteAudio() {
    TrueConfSDK.getInstance().muteSpeaker(isAudioMuted);
  }

  private void sendChatMessage(String userId, String message) {
    TrueConfSDK.getInstance().sendChatMessage(userId, message);
  }

  private void parseProtocolLink(String cmd) {
    TrueConfSDK.getInstance().parseProtocolLink(reactContext, cmd);
  }

  private void scheduleLoginAs(String userId, String password, boolean encryptPassword, String callToUser, boolean autoClose, boolean loginTemp, boolean loginForce, String domain, String serversList, boolean isPublic) {
    TrueConfSDK.getInstance().scheduleLoginAs(userId, password, encryptPassword, callToUser, autoClose, loginTemp, loginForce, domain, serversList, isPublic);
  }

  // NEED THESE ? OR REMOVE?

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


  // TODO: ADD METHODS AND RECEIVE RESULT (THROUGH EVENT/CALLBACK OR JSI ?)

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
