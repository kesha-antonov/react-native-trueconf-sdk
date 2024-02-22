//
//  TrueConfSDK.h
//  TrueConfSDK v.2.00
//
//  Copyright © 2015-2016 TrueConf. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <AVFoundation/AVFoundation.h>
#import "TCTheme.h"

typedef enum {
    TCSDKUserPresStatusUndef = -127,
    TCSDKUserPresStatusUnknown = -1,
    TCSDKUserPresStatusOffline = 0,
    TCSDKUserPresStatusOnline = 1,
    TCSDKUserPresStatusBusy = 2,
    TCSDKUserPresStatusBusy1 = 3,
    TCSDKUserPresStatusBusy2 = 4,
    TCSDKUserPresStatusBusy3 = 5,
    TCSDKUserPresStatusInCloud = 100
} TCSDKUserPresStatus;

typedef enum {
    TCSDKConfControlsButtonEnd,
    TCSDKConfControlsButtonSlideShow,
    TCSDKConfControlsButtonMicrophone,
    TCSDKConfControlsButtonSound,
    TCSDKConfControlsButtonCamera,
    TCSDKConfControlsButtonParticipants,
    TCSDKConfControlsButtonPodium,
    TCSDKConfControlsButtonAdd,
    TCSDKConfControlsVideoLayout,
    TCSDKConfControlsBroadcast,
    TCSDKConfControlsButtonNone
} TCSDKConfControlsButton; // part of TCHCollectionItemKey

typedef void (^TCSDKExtraButtonAction)(id _Nullable sender);

@interface TCSDKExtraButton : NSObject

@property (nonatomic, readonly) NSString* _Nullable title;
@property (nonatomic, readonly) UIImage* _Nullable image;
@property (nonatomic, readonly) UIImage* _Nullable imageTouched;
@property (nonatomic, readonly) UIImage* _Nullable imageDisabled;
@property (nonatomic, readonly) UIColor* _Nullable textColor;
@property (nonatomic, readonly) UIColor* _Nullable textHiColor;
@property (nonatomic, readonly) UIColor* _Nullable textDisColor;
@property (nonatomic, readonly) BOOL enabled;
@property (nonatomic, readonly) TCSDKExtraButtonAction _Nullable action;
@property (nonatomic) TCSDKConfControlsButton key;

- (instancetype _Nullable)initWithTitle:(NSString* _Nullable) aTitle
                        image:(UIImage* _Nullable) aImage
                 imageTouched:(UIImage* _Nullable) aImageTouched
                imageDisabled:(UIImage* _Nullable) aImageDisabled
                    textColor:(UIColor* _Nullable) aTextColor
           textHighlightColor:(UIColor* _Nullable) aTextHiColor
            textDisabledColor:(UIColor* _Nullable) aTextDisColor
                      enabled:(BOOL) aEnabled
                       action:(TCSDKExtraButtonAction _Nullable) aAction;

@end;

@protocol TCConfControlsDelegate <NSObject>

@optional

- (void) tcConfControlsNeedsUpdate:(NSArray<TCSDKExtraButton *> * _Nullable) aButtons; // array of TCSDKExtraButton objects for all visible buttons

@end

@interface TCSDKWindowRect : NSObject

@property (nonatomic) CGRect rect;
@property (nonatomic) CGSize resolution;
@property (nonatomic) NSString* _Nullable userId;

@end

@protocol TCWindowsDelegate <NSObject>

@optional

- (NSArray<TCSDKWindowRect *> * _Nullable) tcWindowsGetPlaces:(NSArray<TCSDKWindowRect *> * _Nullable) aWindows bounds:(CGSize) aSize;

@end

@interface TCSDK : NSObject

@property (nonatomic) UIView* _Nullable xview;
@property (nonatomic) UIView* _Nullable xsview;
@property (nonatomic, readonly, getter=getAVCaptureSession) AVCaptureSession* _Nullable aVCaptureSession;
@property (weak) id <TCConfControlsDelegate> _Nullable confControlsDelegate;
@property (weak) id <TCWindowsDelegate> _Nullable windowsDelegate;
@property (nonatomic) NSBundle* _Nullable resources;
@property (nonatomic) BOOL muteAudio; // NO by default
@property (nonatomic) BOOL trueConfSDKLogEnable; // NO by default

- (instancetype _Nonnull )initWithViewController:(UIViewController* _Nullable) vc forServer:(NSString* _Nullable) serverIP;
- (instancetype _Nonnull )initWithViewController:(UIViewController* _Nullable) vc forServer:(NSString* _Nullable) serverIP directConnection:(BOOL) aDirectConnection;
- (void)setInitViewController:(UIViewController* _Nullable (^_Nullable) (void)) aInitViewController;
- (void)setNewExtraButtons:(NSArray<UIAlertAction *> * _Nullable) btns;

//
// You can use existing button as popoverFromExtraButton for popover presentation. Or use nil.
//
- (void)presentViewController:(UIViewController* _Nullable)viewControllerToPresent animated: (BOOL)flag popoverFrom:(id _Nullable) popoverFrom completion:(void (^ _Nullable)(void))completion;

- (void)start;
- (void)startWithServersList:(NSString* _Nullable) serversList;

// Note: If it was a group call when you call "stop" method and you are owner of it then all participants will be disconnected from this call
- (void)stop;

- (BOOL)loginAs:(NSString* _Nullable) user password:(NSString* _Nullable) pwd encryptPassword:(BOOL) encryptPassword enableAutoLogin:(BOOL) enableAutoLogin;
- (BOOL)logout;
- (BOOL)isStarted;

- (BOOL)callTo:(NSString* _Nullable) user;
- (BOOL)joinConf:(NSString* _Nullable) conf_ID;
- (BOOL)acceptCall:(BOOL) accept;
- (void)parseProtocolLink:(NSString* _Nullable) cmd;

// Ending call. If it was a group call and you are owner of it then all participants will be disconnected from this call
- (BOOL)hangup;
// Ending call with options. If it was a group call and you are owner of it then you can choose if all participants will be disconnected from this call (forAll=YES) or they will continue to stay in call without you (forAll=NO)
- (BOOL)hangup:(BOOL) forAll;

- (BOOL)acceptRecord:(BOOL)accept forUser:(NSString* _Nullable) userID;

- (void)scheduleLoginAs:(NSString* _Nullable) login password:(NSString* _Nullable) pwd encryptPassword:(BOOL) encryptPassword andCallTo:(NSString* _Nullable) callToUser autoClose:(BOOL) autoClose loginTemp:(BOOL) loginTemp loginForce:(BOOL) loginForce domain:(NSString* _Nullable) domain serversList:(NSString* _Nullable) serversList isPublic:(BOOL) isPublic;

- (void)orientationWillChangeTo:(UIInterfaceOrientation) toOrientation;
- (void)orientationDidChangeTo:(UIInterfaceOrientation) toOrientation;

// getting ID and Name of the user currently logged in
- (NSString* _Nullable)getMyId;
- (NSString* _Nullable)getMyName;

// getting User Name
- (NSString* _Nullable)getUserName:(NSString* _Nullable) user;

// checking current app/user's state
- (BOOL)isConnectedToServer;
- (BOOL)isLoggedIn;
- (BOOL)isInConference;

// getting User Status
- (TCSDKUserPresStatus)getUserStatus:(NSString* _Nullable) user;

// callbacks
- (void)onServerStatus:(void (^ _Nullable) (BOOL connected, NSString* _Nullable serverName, NSInteger serverPort))_onServerStatus;
- (void)onLogin:(void (^ _Nullable) (BOOL loggedIn, NSString* _Nullable userID))_onLogin;
- (void)onLogout:(void (^ _Nullable) (void))_onLogout;
- (void)onStateChanged:(void (^ _Nullable) (void))_onStateChanged; //use state* methods to get curent state
- (void)onConferenceStart:(void (^ _Nullable) (void))_onConferenceStart;
- (void)onConferenceEnd:(void (^ _Nullable) (void))_onConferenceEnd;
- (void)onInvite:(void (^ _Nullable) (NSString* _Nullable userID, NSString* _Nullable userName))_onInvite;
- (void)onAccept:(void (^ _Nullable) (NSString* _Nullable userID, NSString* _Nullable userName))_onAccept;
- (void)onReject:(void (^ _Nullable) (NSString* _Nullable userID, NSString* _Nullable userName))_onReject;
- (void)onRejectTimeOut:(void (^ _Nullable) (NSString* _Nullable userID, NSString* _Nullable userName))_onRejectTimeOut;
- (void)onUserStatusUpdate:(void(^ _Nullable) (NSString* _Nullable usr, TCSDKUserPresStatus state)) _onStatusUpdate;
- (void)onRecordRequest:(void (^ _Nullable) (NSString* _Nullable userID, NSString* _Nullable userName))_onRecordRequest;

// hardware
- (void) muteMicrophone:(BOOL)mute;
- (void) muteCamera:(BOOL)mute;
- (BOOL) microphoneMuted;
- (BOOL) cameraMuted;

- (int32_t)getOutputMaxBitrate;
- (int32_t)getInputMaxBitrate;
- (void)setOutputMaxBitrate:(int32_t) br;
- (void)setInputMaxBitrate:(int32_t) br;

- (void) rearrangeReceivers;

- (void) setResources:(NSBundle* _Nullable) newResources;

// theme management
- (TCTheme* _Nullable) getDefaultTheme;
- (void) useDefaultTheme;
- (void) useCustomTheme:(TCTheme* _Nullable) theme;
- (TCTheme* _Nullable) getActiveTheme;

// text messages
- (BOOL) sendChatMessage:(NSString* _Nullable) userID :(NSString* _Nullable) message;
- (void) onChatMessageReceived:(void (^ _Nullable) (NSString* _Nullable fromUserID, NSString* _Nullable fromUserName, NSString* _Nullable message, NSString* _Nullable toUserID))_onMessage;

@end
