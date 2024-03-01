package com.example

import android.content.Intent
import com.facebook.react.ReactActivity
import com.facebook.react.ReactActivityDelegate
import com.facebook.react.defaults.DefaultNewArchitectureEntryPoint.fabricEnabled
import com.facebook.react.defaults.DefaultReactActivityDelegate
import expo.modules.ReactActivityDelegateWrapper
import android.os.Bundle
import com.zoontek.rnbootsplash.RNBootSplash

class MainActivity : ReactActivity() {

  /**
   * Returns the name of the main component registered from JavaScript. This is used to schedule
   * rendering of the component.
   */
  override fun getMainComponentName(): String = "example"

  /**
   * Returns the instance of the [ReactActivityDelegate]. We use [DefaultReactActivityDelegate]
   * which allows you to enable New Architecture with a single boolean flags [fabricEnabled]
   */
  override fun createReactActivityDelegate(): ReactActivityDelegate =
      ReactActivityDelegateWrapper(this, BuildConfig.IS_NEW_ARCHITECTURE_ENABLED,
        DefaultReactActivityDelegate(this, mainComponentName, fabricEnabled))

  override fun onNewIntent(intent: Intent) {
    super.onNewIntent(intent)

    val isTrueConfSdkHideCallWindow:Boolean = intent.getBooleanExtra("isTrueConfSdkHideCallWindow", false)
    if (isTrueConfSdkHideCallWindow)
      overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
  }

   override fun onCreate(savedInstanceState: Bundle?) {
    // ADDED NULL TO PREVENT CRASHES
    // https://github.com/software-mansion/react-native-screens/issues/17#issuecomment-424704067
    super.onCreate(null)

    RNBootSplash.init(R.drawable.bootsplash, this)
  }
}
