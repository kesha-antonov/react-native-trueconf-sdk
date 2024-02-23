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
    @objc var onRecordRequest: RCTDirectEventBlock?

    @objc var onConferenceStart: RCTDirectEventBlock?
    @objc var onConferenceEnd: RCTDirectEventBlock?

    @objc var onUserStatusUpdate: RCTDirectEventBlock?

    @objc var server: String = "qa4.trueconf.net"

    @objc private var _isMuted : Bool = false
    @objc private var _isCameraOn : Bool = true

    @objc var isMuted: Bool {
        set(newValue) {
            print("react-native-trueconf-sdk TrueConfView isMuted-1:", newValue)

            self._isMuted = newValue
            DispatchQueue.main.async {
                print("react-native-trueconf-sdk TrueConfView isMuted-2:", self._isMuted)
                self.tcsdk?.muteMicrophone(self._isMuted)
            }
        }
        get {
            return self._isMuted
        }
    }
    @objc var isCameraOn: Bool {
        set(newValue) {
            print("react-native-trueconf-sdk TrueConfView isCameraOn-1:", newValue)

            self._isCameraOn = newValue
            DispatchQueue.main.async {
                if (self.tcsdk == nil) { return }

                let isCameraMuted = !newValue
                print("react-native-trueconf-sdk TrueConfView isCameraOn-2:", isCameraMuted)
                self.tcsdk!.muteCamera(isCameraMuted)

                print("cameraOn set: " + String(isCameraMuted))

                if (isCameraMuted) {
                    self.tcsdk!.xsview = nil
                } else {
                    self.tcsdk!.xsview = self.xsview!
                }
            }
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

    @objc func getIsCameraOn () -> Bool {
        if (self.tcsdk == nil) {
            return self._isCameraOn
        }

        return !self.tcsdk!.cameraMuted()
    }

    @objc func getIsMicrophoneMuted () -> Bool {
        if (self.tcsdk == nil) {
            return self._isMuted
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
    func initSdk(isMuted: Bool, isCameraOn: Bool) {
        print("react-native-trueconf-sdk TrueConfView initSdk isMuted-1: " + String(isMuted))
        print("react-native-trueconf-sdk TrueConfView initSdk isCameraMuted-1: " + String(isCameraOn))

        if (self.tcsdk != nil ) { return }

        DispatchQueue.main.async {
            // guard let rootViewController = RCTSharedApplication().delegate?.window??.rootViewController else { return }

            self.tcsdk = TCSDK(viewController: nil, forServer: self.server)
            self.tcsdk!.trueConfSDKLogEnable = true;

            self.tcsdk!.confControlsDelegate = self
            self.tcsdk!.windowsDelegate = self
            self.tcsdk!.xview = self.xview!
            self.tcsdk!.xsview = self.xsview!

            self.initEvents()

            // CALLS MIC/CAMERA ON/OFF AFTER SDK INIT
            self.isMuted = isMuted
            self.isCameraOn = isCameraOn

            self.tcsdk!.start()

            print("react-native-trueconf-sdk TrueConfView initSdk isCameraOn-2: " + String(isCameraOn))

            self.tcsdk!.muteMicrophone(isMuted)
            self.tcsdk!.muteCamera(!isCameraOn)
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
        print("react-native-trueconf-sdk TrueConfView login userId: " + String(userId))
        print("react-native-trueconf-sdk TrueConfView login password: " + String(password))
        print("react-native-trueconf-sdk TrueConfView login encryptPassword: " + String(encryptPassword))
        print("react-native-trueconf-sdk TrueConfView login enableAutoLogin: " + String(enableAutoLogin))
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
        self.tcsdk!.onServerStatus({ (connected: Bool, serverName: String?, serverPort: Int) -> () in
            print("react-native-trueconf-sdk TrueConfView isConnected: " + String(connected))
            print("react-native-trueconf-sdk TrueConfView serverName: " + (serverName ?? ""))
            print("react-native-trueconf-sdk TrueConfView serverPort: " + String(serverPort))

            self.onServerStatus?([
                "isConnected": connected,
                "serverName": (serverName ?? ""),
                "serverPort": serverPort
            ])
        })

        self.tcsdk!.onStateChanged({ () -> () in
            print("react-native-trueconf-sdk TrueConfView onStateChanged")
            let isConnectedToServer = self.tcsdk!.isConnectedToServer()
            print("react-native-trueconf-sdk TrueConfView onStateChanged isConnectedToServer: " + String(isConnectedToServer))

            let isLoggedIn = self.tcsdk!.isLoggedIn()
            print("react-native-trueconf-sdk TrueConfView onStateChanged isLoggedIn: \(isLoggedIn)")

            self.onStateChanged?([
                "isConnectedToServer": isConnectedToServer,
                "isLoggedIn": isLoggedIn,
                "isStarted": self.tcsdk!.isStarted(),
                "isInConference": self.tcsdk!.isInConference(),
                "isCameraOn": self.getIsCameraOn(),
                "isMicrophoneMuted": self.getIsMicrophoneMuted()
            ])
        })

        self.tcsdk!.onLogin({ (loggedIn: Bool, userId: String?) in
            print("react-native-trueconf-sdk TrueConfView onLogin isLoggedIn: " + String(loggedIn))
            print("react-native-trueconf-sdk TrueConfView onLogin userId: " + (userId ?? ""))
            self.onLogin?([
                "userId": userId as Any,
                "isLoggedIn": loggedIn
            ])
        })

        self.tcsdk!.onLogout({
            print("react-native-trueconf-sdk TrueConfView onLogout")
            self.onLogout?([:])
        })

        self.tcsdk!.onAccept({ (userId: String?, userName: String?) in
            print("react-native-trueconf-sdk TrueConfView onAccept")
            self.onAccept?([
                "userId": userId as Any,
                "userName": userName as Any
            ])
        })

        self.tcsdk!.onReject({ (userId: String?, userName: String?) in
            print("react-native-trueconf-sdk TrueConfView onReject")
            self.onAccept?([
                "userId": userId as Any,
                "userName": userName as Any
            ])
        })

        self.tcsdk!.onRejectTimeOut({ (userId: String?, userName: String?) in
            print("react-native-trueconf-sdk TrueConfView onRejectTimeOut")
            self.onAccept?([
                "userId": userId as Any,
                "userName": userName as Any
            ])
        })

        self.tcsdk!.onInvite({ (userId: String?, userName: String?) in
            print("react-native-trueconf-sdk TrueConfView onInvite")
            self.onInvite?([
                "userId": userId as Any,
                "userName": userName as Any
            ])
        })

        self.tcsdk!.onConferenceStart({
            print("react-native-trueconf-sdk TrueConfView onConferenceStart")
            self.onConferenceStart?([:])
        })

        self.tcsdk!.onConferenceEnd({
            print("react-native-trueconf-sdk TrueConfView onConferenceEnd")
            self.onConferenceEnd?([:])
        })

        self.tcsdk!.onUserStatusUpdate({ (user: String?, status: TCSDKUserPresStatus) in
            print("react-native-trueconf-sdk TrueConfView onUserStatusUpdate")
            self.onUserStatusUpdate?([
                "user": user as Any,
                "status": status
            ])
        })

        self.tcsdk!.onRecordRequest({ (userId: String?, userName: String?) in
            print("react-native-trueconf-sdk TrueConfView onRecordRequest")

            self.onRecordRequest?([
                "userId": userId as Any,
                "userName": userName as Any
            ])
        })
    }

}
