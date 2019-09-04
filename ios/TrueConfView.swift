class TrueConfView : UIView, UITextFieldDelegate, TCConfControlsDelegate, TCWindowsDelegate {
  var tcsdk: TCSDK?
  var xview: UIView?
  var xsview: UIView?

  @objc var onServerStatus: RCTDirectEventBlock?
  @objc var onStateChanged: RCTDirectEventBlock?
  @objc var onLogin: RCTDirectEventBlock?

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

      self.tcsdk?.muteCamera(!newValue)
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
    self.xview!.addSubview(self.xsview!)
//    self.xsview!.backgroundColor = UIColor.white

    self.xsview!.translatesAutoresizingMaskIntoConstraints = false
    self.xsview!.rightAnchor.constraint(equalTo: self.xview!.safeRightAnchor).isActive = true
    self.xsview!.bottomAnchor.constraint(equalTo: self.xview!.safeBottomAnchor).isActive = true
    let xsview_width = UIScreen.main.bounds.width / 3
    self.xsview!.widthAnchor.constraint(equalToConstant: xsview_width).isActive = true
    self.xsview!.heightAnchor.constraint(equalToConstant: xsview_width * 2).isActive = true

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
//      guard let rootViewController = UIApplication.shared.delegate?.window??.rootViewController else { return }
      self.tcsdk = TCSDK(viewController: nil, forServer: self.server, confCustomControlsImages: nil)

      self.tcsdk!.confControlsDelegate = nil // self
      self.tcsdk!.windowsDelegate = nil // self
      self.tcsdk!.xview = self.xview!
      self.tcsdk!.xsview = self.xsview!

      self.initEvents()

      self.tcsdk!.start()
    }
  }

  @objc
  func notifyOnServerStatus(connected: Bool, serverName: String, serverPort: Int) {
    print("notifyOnServerStatus")
    onServerStatus!(["connected": connected])
  }

  @objc
  func notifyOnStateChanged(isConnectedToServer: Bool, isLoggedIn: Bool) {
    print("notifyOnStateChanged")
    onStateChanged?([
      "isConnectedToServer": isConnectedToServer,
      "isLoggedIn": isLoggedIn
    ])
  }

  @objc
  func makeCall(to: NSString) {
    self.tcsdk!.call(to: String(to))
  }

  @objc
  func hangup() {
     self.tcsdk!.hangup()
   }

  @objc
  func onLogin(isLoggedIn: Bool, userID: String) {
    onLogin?([
      "userID": userID,
      "isLoggedIn": isLoggedIn
    ])
  }

  func initEvents() {
    self.tcsdk!.onServerStatus( { (connected: Bool, serverName: String, serverPort: Int) -> () in
      print("connected: " + String(connected))
      print("serverName: " + serverName)
      print("serverPort: " + String(serverPort))
      self.notifyOnServerStatus(connected: connected, serverName: serverName, serverPort: serverPort)
    } as? (Bool, String?, Int) -> Void  )

    self.tcsdk!.onStateChanged( { () -> () in
      print("onStateChanged")
      let isConnectedToServer = self.tcsdk!.isConnectedToServer()
      print("onStateChanged isConnectedToServer: " + String(isConnectedToServer))

      let isLoggedIn = self.tcsdk!.isLoggedIn()
      print("onStateChanged isLoggedIn: \(isLoggedIn)")

      self.notifyOnStateChanged(isConnectedToServer: isConnectedToServer, isLoggedIn: isLoggedIn)
    } )

    self.tcsdk!.onLogin({ (loggedIn: Bool, userID: String) in
      print("onLogin isLoggedIn: " + String(loggedIn))
      print("onLogin userID: " + String(userID))
      self.onLogin(isLoggedIn: loggedIn, userID: userID)
    } as? (Bool, String?) -> Void)

    self.tcsdk!.onLogin()
  }

  @objc
  func login(userId: NSString, password: NSString, encryptPassword: Bool, enableAutoLogin: Bool) {
    print("login userId: " + String(userId))
    print("login password: " + String(password))
    print("login encryptPassword: " + String(encryptPassword))
    print("login enableAutoLogin: " + String(enableAutoLogin))
    self.tcsdk!.login(as: String(userId), password: String(password), encryptPassword: encryptPassword, enableAutoLogin: enableAutoLogin)
  }

}
