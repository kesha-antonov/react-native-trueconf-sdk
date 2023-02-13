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
    @objc var muted: Bool {
        set(newValue) {
            print("muted:", newValue)

            self.tcsdk?.muteMicrophone(newValue)
        }
        get {
            if (self.tcsdk == nil) { return false }

            return self.tcsdk!.microphoneMuted()
        }
    }
    @objc var cameraOn: Bool {
        set(newValue) {
            print("cameraOn:", newValue)

            self.tcsdk?.muteCamera(newValue)
        }
        get {
            if (self.tcsdk == nil) { return true }

            return !self.tcsdk!.cameraMuted()
        }
    }

    override init(frame: CGRect) {
        super.init(frame: frame)
    }

    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
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
        print("login userId: " + String(userId))
        print("login password: " + String(password))
        print("login encryptPassword: " + String(encryptPassword))
        print("login enableAutoLogin: " + String(enableAutoLogin))
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
            print("connected: " + String(connected))
            print("serverName: " + serverName)
            print("serverPort: " + String(serverPort))

            self.onServerStatus?([
                "connected": connected,
                "serverName": serverName,
                "serverPort": serverPort
            ])
            } as? (Bool, String?, Int) -> Void  )

        self.tcsdk!.onStateChanged( { () -> () in
            print("onStateChanged")
            let isConnectedToServer = self.tcsdk!.isConnectedToServer()
            print("onStateChanged isConnectedToServer: " + String(isConnectedToServer))

            let isLoggedIn = self.tcsdk!.isLoggedIn()
            print("onStateChanged isLoggedIn: \(isLoggedIn)")

            self.onStateChanged?([
                "isConnectedToServer": isConnectedToServer,
                "isLoggedIn": isLoggedIn,
                "isStarted": self.tcsdk!.isStarted(),
                "isInConference": self.tcsdk!.isInConference(),
                "isCameraMuted": self.tcsdk!.cameraMuted(),
                "isMicrophoneMuted": self.tcsdk!.microphoneMuted()
            ])
        } )

        self.tcsdk!.onLogin({ (loggedIn: Bool, userId: String) in
            print("onLogin isLoggedIn: " + String(loggedIn))
            print("onLogin userId: " + String(userId))
            self.onLogin?([
                "userId": userId,
                "isLoggedIn": loggedIn
            ])
            } as? (Bool, String?) -> Void)

        self.tcsdk!.onLogout({
            print("onLogout")
            self.onLogout?([:])
        })

        self.tcsdk!.onAccept({ (userId: String, userName: String) in
            print("onAccept")
            self.onAccept?([
                "userId": userId,
                "userName": userName
            ])
            } as? (String?, String?) -> Void)

        self.tcsdk!.onReject({ (userId: String, userName: String) in
            print("onReject")
            self.onAccept?([
                "userId": userId,
                "userName": userName
            ])
            } as? (String?, String?) -> Void)

        self.tcsdk!.onRejectTimeOut({ (userId: String, userName: String) in
            print("onRejectTimeOut")
            self.onAccept?([
                "userId": userId,
                "userName": userName
            ])
            } as? (String?, String?) -> Void)

        self.tcsdk!.onInvite({ (userId: String, userName: String) in
            print("onInvite")
            self.onInvite?([
                "userId": userId,
                "userName": userName
            ])
            } as? (String?, String?) -> Void)

        self.tcsdk!.onConferenceStart({
            print("onConferenceStart")
            self.onConferenceStart?([:])
        })
        self.tcsdk!.onConferenceEnd({
            print("onConferenceEnd")
            self.onConferenceEnd?([:])
        })

        self.tcsdk!.onUserStatusUpdate({ (user: String, status: Int) in
            print("onUserStatusUpdate")
            self.onUserStatusUpdate?([
                "user": user,
                "status": status
            ])
            } as? (String?, TCSDKUserPresStatus) -> Void)
    }

}
