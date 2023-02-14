class TrueConfView : UIView, UITextFieldDelegate, TCConfControlsDelegate, TCWindowsDelegate {
    var tcsdk: TCSDK?
    var xview: UIView?
    var xsview: UIView?

    @objc var onServerStatus: RCTDirectEventBlock?
    @objc var onStateChanged: RCTDirectEventBlock?

    @objc var onLogin: RCTDirectEventBlock?
    @objc var onLogout: RCTDirectEventBlock?

    @objc var onAccept: RCTDirectEventBlock?
    @objc var onInvite: RCTDirectEventBlock?
    @objc var onReject: RCTDirectEventBlock?
    @objc var onRejectTimeout: RCTDirectEventBlock?

    @objc var onConferenceStart: RCTDirectEventBlock?
    @objc var onConferenceEnd: RCTDirectEventBlock?

    @objc var onUserStatusUpdate: RCTDirectEventBlock?

    @objc var server: String = "ru10.trueconf.net"

    @objc private var _isMuted : Bool
    @objc private var _isCameraOn : Bool

    @objc var isMuted: Bool {
        set(newValue) {
            print("react-native-trueconf-react-sdk TrueConfView isMuted:", newValue)

            self._isMuted = newValue
            self.tcsdk?.muteMicrophone(newValue)
        }
        get {
            return self._isMuted
        }
    }
    @objc var isCameraOn: Bool {
        set(newValue) {
            print("react-native-trueconf-react-sdk TrueConfView isCameraOn:", newValue)

            self._isCameraOn = newValue
            self.tcsdk?.muteCamera(!self._isCameraOn)
        }
        get {
            return self._isCameraOn
        }
    }

    override init(frame: CGRect) {
        super.init(frame: frame)
    }

    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
    }

    @objc func getIsCameraOn () {
        if (self.tcsdk == nil) {
            return nil
        }

        return !self.tcsdk!.cameraMuted()
    }

    @objc func getIsMicrophoneMuted () {
        if (self.tcsdk == nil) {
            return nil
        }

        return self.tcsdk!.microphoneMuted()
    }

    @objc func draggedView(_ sender:UIPanGestureRecognizer){
        self.xview!.bringSubviewToFront(self.xsview!)
        let translation = sender.translation(in: self.xview)
        self.xsview!.center = CGPoint(x: self.xsview!.center.x + translation.x, y: self.xsview!.center.y + translation.y)
        sender.setTranslation(CGPoint.zero, in: self.xview!)
    }

    func initViews() {
        self.xview = UIView()
        self.addSubview(self.xview!)
        //    self.xview!.backgroundColor = .white

        self.xview!.translatesAutoresizingMaskIntoConstraints = false
        self.xview!.leadingAnchor.constraint(equalTo: self.leadingAnchor).isActive = true
        self.xview!.topAnchor.constraint(equalTo: self.topAnchor).isActive = true
        self.xview!.widthAnchor.constraint(equalTo: self.widthAnchor).isActive = true
        self.xview!.heightAnchor.constraint(equalTo: self.heightAnchor).isActive = true

        self.xsview = UIView()
        self.addSubview(self.xsview!)
        self.xsview!.backgroundColor = UIColor.white

        self.xsview!.translatesAutoresizingMaskIntoConstraints = false
        // MARGIN RIGHT 16
        self.xsview!.rightAnchor.constraint(equalTo: self.xview!.safeRightAnchor, constant: -16.0).isActive = true
        // MARGIN TOP 100
        self.xsview!.topAnchor.constraint(equalTo: self.xview!.safeTopAnchor, constant: 100.0).isActive = true
        // WIDTH & HEIGHT
        let xsview_width = UIScreen.main.bounds.width / 3.5
        self.xsview!.widthAnchor.constraint(equalToConstant: xsview_width).isActive = true
        self.xsview!.heightAnchor.constraint(equalToConstant: xsview_width).isActive = true
        // ADD PAN GESTURE
        let xsviewPanGesture = UIPanGestureRecognizer(target: self, action: #selector(TrueConfView.draggedView(_:)))
        self.xsview!.isUserInteractionEnabled = true
        self.xsview!.addGestureRecognizer(xsviewPanGesture)

        let activityIndicatorView = UIView()
        self.xview!.addSubview(activityIndicatorView)
        activityIndicatorView.translatesAutoresizingMaskIntoConstraints = false
        activityIndicatorView.centerXAnchor.constraint(equalTo: self.xview!.centerXAnchor).isActive = true
        activityIndicatorView.centerYAnchor.constraint(equalTo: self.xview!.centerYAnchor).isActive = true
        //    TODO: MOVE TO LEFT/TOP BY -23

        //    let activityIndicator = UIActivityIndicatorView(activityIndicatorStyle: .white)
        //    activityIndicator.frame = CGRect(x: 0, y: 0, width: 46, height: 46)
        //    activityIndicator.startAnimating()
        //    activityIndicatorView.addSubview(activityIndicator)
    }

    @objc
    func initSdk() {
        if (self.tcsdk != nil ) { return }

        DispatchQueue.main.async {
            //      guard let rootViewController = RCTSharedApplication().delegate?.window??.rootViewController else { return }
            self.tcsdk = TCSDK(viewController: nil, forServer: self.server, confCustomControlsImages: nil)
            self.tcsdk!.trueConfSDKLogEnable = true;

            self.tcsdk!.confControlsDelegate = self // self
            self.tcsdk!.windowsDelegate = self // self
            self.tcsdk!.xview = self.xview!
            self.tcsdk!.xsview = self.xsview!

            self.initEvents()

            // CALLS MIC/CAMERA ON/OFF AFTER SDK INIT
            self.isMuted = self._isMuted
            self.isCameraOn = self._isCameraOn

            self.tcsdk!.start()
        }
    }

    @objc
    func logout() {
        self.tcsdk?.logout()
    }

    @objc
    func stopSdk() {
        self.logout()
        self.tcsdk?.stop()
        self.tcsdk = nil
    }

    @objc
    func makeCall(to: NSString) {
        self.tcsdk!.call(to: String(to))
    }

    @objc
    func hangup(forAll: Bool) {
        self.tcsdk?.hangup(forAll)
    }

    @objc
    func login(userId: NSString, password: NSString, encryptPassword: Bool, enableAutoLogin: Bool) {
        print("react-native-trueconf-react-sdk TrueConfView login userId: " + String(userId))
        print("react-native-trueconf-react-sdk TrueConfView login password: " + String(password))
        print("react-native-trueconf-react-sdk TrueConfView login encryptPassword: " + String(encryptPassword))
        print("react-native-trueconf-react-sdk TrueConfView login enableAutoLogin: " + String(enableAutoLogin))
        self.tcsdk!.login(as: String(userId), password: String(password), encryptPassword: encryptPassword, enableAutoLogin: enableAutoLogin)
    }

    @objc
    func joinConf(confId: String) {
        self.tcsdk!.joinConf(confId)
    }

    @objc
    func acceptCall(accept: Bool) {
        self.tcsdk!.acceptCall(accept)
    }

    func initEvents() {
        self.tcsdk!.onServerStatus( { (connected: Bool, serverName: String, serverPort: Int) -> () in
            print("react-native-trueconf-react-sdk TrueConfView connected: " + String(connected))
            print("react-native-trueconf-react-sdk TrueConfView serverName: " + serverName)
            print("react-native-trueconf-react-sdk TrueConfView serverPort: " + String(serverPort))

            self.onServerStatus?([
                "connected": connected,
                "serverName": serverName,
                "serverPort": serverPort
            ])
            } as? (Bool, String?, Int) -> Void  )

        self.tcsdk!.onStateChanged( { () -> () in
            print("react-native-trueconf-react-sdk TrueConfView onStateChanged")
            let isConnectedToServer = self.tcsdk!.isConnectedToServer()
            print("react-native-trueconf-react-sdk TrueConfView onStateChanged isConnectedToServer: " + String(isConnectedToServer))

            let isLoggedIn = self.tcsdk!.isLoggedIn()
            print("react-native-trueconf-react-sdk TrueConfView onStateChanged isLoggedIn: \(isLoggedIn)")

            self.onStateChanged?([
                "isConnectedToServer": isConnectedToServer,
                "isLoggedIn": isLoggedIn,
                "isStarted": self.tcsdk!.isStarted(),
                "isInConference": self.tcsdk!.isInConference(),
                "isCameraOn": self.getIsCameraOn(),
                "isMicrophoneMuted": self.getIsMicrophoneMuted()
            ])
        } )

        self.tcsdk!.onLogin({ (loggedIn: Bool, userId: String) in
            print("react-native-trueconf-react-sdk TrueConfView onLogin isLoggedIn: " + String(loggedIn))
            print("react-native-trueconf-react-sdk TrueConfView onLogin userId: " + String(userId))
            self.onLogin?([
                "userId": userId,
                "isLoggedIn": loggedIn
            ])
            } as? (Bool, String?) -> Void)

        self.tcsdk!.onLogout({
            print("react-native-trueconf-react-sdk TrueConfView onLogout")
            self.onLogout?([:])
        })

        self.tcsdk!.onAccept({ (userId: String, userName: String) in
            print("react-native-trueconf-react-sdk TrueConfView onAccept")
            self.onAccept?([
                "userId": userId,
                "userName": userName
            ])
            } as? (String?, String?) -> Void)

        self.tcsdk!.onReject({ (userId: String, userName: String) in
            print("react-native-trueconf-react-sdk TrueConfView onReject")
            self.onAccept?([
                "userId": userId,
                "userName": userName
            ])
            } as? (String?, String?) -> Void)

        self.tcsdk!.onRejectTimeOut({ (userId: String, userName: String) in
            print("react-native-trueconf-react-sdk TrueConfView onRejectTimeOut")
            self.onAccept?([
                "userId": userId,
                "userName": userName
            ])
            } as? (String?, String?) -> Void)

        self.tcsdk!.onInvite({ (userId: String, userName: String) in
            print("react-native-trueconf-react-sdk TrueConfView onInvite")
            self.onInvite?([
                "userId": userId,
                "userName": userName
            ])
            } as? (String?, String?) -> Void)

        self.tcsdk!.onConferenceStart({
            print("react-native-trueconf-react-sdk TrueConfView onConferenceStart")
            self.onConferenceStart?([:])
        })
        self.tcsdk!.onConferenceEnd({
            print("react-native-trueconf-react-sdk TrueConfView onConferenceEnd")
            self.onConferenceEnd?([:])
        })

        self.tcsdk!.onUserStatusUpdate({ (user: String, status: Int) in
            print("react-native-trueconf-react-sdk TrueConfView onUserStatusUpdate")
            self.onUserStatusUpdate?([
                "user": user,
                "status": status
            ])
            } as? (String?, TCSDKUserPresStatus) -> Void)
    }

}
