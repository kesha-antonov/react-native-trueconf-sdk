#import "React/RCTViewManager.h"

@interface RCT_EXTERN_MODULE(RNTrueconfReactSdk, RCTViewManager)

RCT_EXPORT_VIEW_PROPERTY(server, NSString)
RCT_EXPORT_VIEW_PROPERTY(isCameraOn, BOOL)
RCT_EXPORT_VIEW_PROPERTY(isMuted, BOOL)

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

#pragma mark - JS exported methods
RCT_EXTERN_METHOD(
  initSdk:(nonnull NSNumber *)node
  isMuted:(nonnull BOOL *)isMuted
  isCameraOn:(nonnull BOOL *)isCameraOn
)
RCT_EXTERN_METHOD(
  stopSdk:(nonnull NSNumber *)node
)
RCT_EXTERN_METHOD(
  makeCall:(nonnull NSNumber *)node
  to:(nonnull NSString *)to
)
RCT_EXTERN_METHOD(
  hangup:(nonnull NSNumber *)node
  forAll:(nonnull BOOL *)forAll
)
RCT_EXTERN_METHOD(
  acceptCall:(nonnull NSNumber *)node
  accept:(nonnull BOOL *)accept
)
RCT_EXTERN_METHOD(
  joinConf:(nonnull NSNumber *)node
  confId:(nonnull NSString *)confId
)
RCT_EXTERN_METHOD(
  login:(nonnull NSNumber *)node
  userId:(nonnull NSString *)userId
  password:(nonnull NSString *)password
  encryptPassword:(nonnull BOOL *)encryptPassword
  enableAutoLogin:(nonnull BOOL *)enableAutoLogin
)
RCT_EXTERN_METHOD(
  logout:(nonnull NSNumber *)node
)

@end
