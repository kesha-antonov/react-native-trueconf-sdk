@objc(RNTrueconfReactSdk)
class RNTrueconfReactSdk: RCTViewManager {
    var trueConfView: TrueConfView?

    override func view() -> UIView! {
        trueConfView = TrueConfView()
        trueConfView!.initViews()
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
    func initSdk(_ node: NSNumber) {
        DispatchQueue.main.async {
            let component = self.getComponent(node: node)
            component.initSdk()
        }
    }

    @objc
    func stopSdk(_ node: NSNumber) {
        DispatchQueue.main.async {
            let component = self.getComponent(node: node)
            component.stopSdk()
        }
    }

    @objc
    func makeCall(_ node: NSNumber, to: NSString) {
        DispatchQueue.main.async {
            let component = self.getComponent(node: node)
            component.makeCall(to: to)
        }
    }

    @objc
    func hangup(_ node: NSNumber, forAll: Bool) {
        DispatchQueue.main.async {
            let component = self.getComponent(node: node)
            component.hangup(forAll: forAll)
        }
    }

    @objc
    func login(_ node: NSNumber, userId: NSString, password: NSString, encryptPassword: Bool, enableAutoLogin: Bool) {
        DispatchQueue.main.async {
            let component = self.getComponent(node: node)
            component.login(userId: userId, password: password, encryptPassword: encryptPassword, enableAutoLogin: enableAutoLogin)
        }
    }

    @objc
    func logout(_ node: NSNumber) {
        DispatchQueue.main.async {
            let component = self.getComponent(node: node)
            component.logout()
        }
    }

    @objc
    func acceptCall(_ node: NSNumber, accept: Bool) {
        DispatchQueue.main.async {
            let component = self.getComponent(node: node)
            component.acceptCall(accept: accept)
        }
    }

    @objc
    func joinConf(_ node: NSNumber, confId: String) {
        DispatchQueue.main.async {
            let component = self.getComponent(node: node)
            component.joinConf(confId: confId)
        }
    }

}
