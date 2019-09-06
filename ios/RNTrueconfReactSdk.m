#import "React/RCTViewManager.h"

@interface RCT_EXTERN_MODULE(RNTrueconfReactSdk, RCTViewManager)

RCT_EXPORT_VIEW_PROPERTY(server, NSString)
RCT_EXPORT_VIEW_PROPERTY(cameraOn, BOOL)
RCT_EXPORT_VIEW_PROPERTY(muted, BOOL)

RCT_EXPORT_VIEW_PROPERTY(onServerStatus, RCTDirectEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onStateChanged, RCTDirectEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onLogin, RCTDirectEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onLogout, RCTDirectEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onAccept, RCTDirectEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onInvite, RCTDirectEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onReject, RCTDirectEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onRejectTimeout, RCTDirectEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onConferenceStart, RCTDirectEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onConferenceEnd, RCTDirectEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onUserStatusUpdate, RCTDirectEventBlock)

RCT_EXTERN_METHOD(
  initSdk:(nonnull NSNumber *)node
)
RCT_EXTERN_METHOD(
  makeCall:(nonnull NSNumber *)node
  to:(nonnull NSString *)to
)
RCT_EXTERN_METHOD(
  hangup:(nonnull NSNumber *)node
)
RCT_EXTERN_METHOD(
  login:(nonnull NSNumber *)node
  userId:(nonnull NSString *)userId
  password:(nonnull NSString *)password
  encryptPassword:(nonnull BOOL *)encryptPassword
  enableAutoLogin:(nonnull BOOL *)enableAutoLogin
)


@end
