package com.kesha_antonov.rn_trueconf_react_sdk;
import android.os.Bundle;
import androidx.fragment.app.FragmentManager;
import com.facebook.react.ReactActivity;

public class TrueConfSDKReactActivity extends ReactActivity {
  private static TrueConfSDKReactActivity instance;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    instance = this;
  }

  public FragmentManager getfm() {
    return getSupportFragmentManager();
  }

  public static TrueConfSDKReactActivity getInstance() {
    return instance;
  }
}
