@objc(RNTrueconfReactSdk)
class RNTrueconfReactSdk: RCTViewManager {
  var trueConfView: TrueConfView?

  override func view() -> UIView! {
    trueConfView = TrueConfView()
    trueConfView!.initViewsAndSdk()
    return trueConfView
  }

  override static func requiresMainQueueSetup() -> Bool {
    return true
  }

  func getComponent(node: NSNumber) -> TrueConfView {
    return self.bridge.uiManager.view(
      forReactTag: node
    ) as! TrueConfView
  }

  @objc
  func makeCall(_ node: NSNumber, to: NSString) {
    DispatchQueue.main.async {
      let component = self.getComponent(node: node)
      component.makeCall(to: to)
    }
  }

  @objc
  func hangup(_ node: NSNumber) {
    DispatchQueue.main.async {
      let component = self.getComponent(node: node)
      component.hangup()
    }
  }

  @objc
  func login(_ node: NSNumber, userId: NSString, password: NSString, encryptPassword: Bool, enableAutoLogin: Bool) {
    DispatchQueue.main.async {
      let component = self.getComponent(node: node)
      component.login(userId: userId, password: password, encryptPassword: encryptPassword, enableAutoLogin: enableAutoLogin)
    }
  }

}
