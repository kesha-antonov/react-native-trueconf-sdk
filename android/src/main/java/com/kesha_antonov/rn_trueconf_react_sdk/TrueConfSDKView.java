package com.kesha_antonov.rn_trueconf_react_sdk;

import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;

import com.facebook.react.ReactActivity;
import com.facebook.react.uimanager.ThemedReactContext;
import com.kesha_antonov.rn_trueconf_react_sdk.TrueConfSDKReactActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

public class TrueConfSDKView extends LinearLayout {
  //  implements MediaPlayer.OnPreparedListener,

  private ThemedReactContext themedReactContext;
  private String LOG_TAG = "TrueConfSDKView";
//  private  MyFragment fragment;
  private PlaceholderFragment fragment;

  public MyFragment outgoingCallFragment;


  public TrueConfSDKView(ThemedReactContext ctx, @Nullable TrueConfSDKViewManager viewManager) {
    super(ctx);
    Log.d(LOG_TAG, "TrueConfSDKView: " + "1");

    themedReactContext = ctx;

    LinearLayout root = (LinearLayout) LayoutInflater.from(themedReactContext)
                .inflate(R.layout.layout_container, this);

      // FragmentManager fm = MainActivity.getInstance().getfm();
      FragmentManager fm = TrueConfSDKReactActivity.getInstance().getfm();

//      fragment = MyFragment.newInstance();
    PlaceholderFragment fragment = new PlaceholderFragment();

      fm
        .beginTransaction()
        .add(fragment, "interface_tag")
        .commit();
      // Execute the commit immediately or can use commitNow() instead
      fm.executePendingTransactions();
      // This step is needed to in order for ReactNative to render your view
//    FrameLayout view = new FrameLayout(themedReactContext);
//      view.addView(fragment.getView(), Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.MATCH_PARENT);
//      root.addView(view, Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.MATCH_PARENT);
      root.addView(fragment.getView(), 0);
//      this.addView(fragment.getView(), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

      Log.d(LOG_TAG, "TrueConfSDKView: " + "2");

    outgoingCallFragment = new MyFragment();
    fm
      .beginTransaction()
      .add(outgoingCallFragment, "outgoing_call_tag")
      .commit();
    fm.executePendingTransactions();

    root.addView(outgoingCallFragment.getView(), 1);


//      this.addView(root);
  }

   public void setServer(final String server) {
     Log.d(LOG_TAG, "setServer: " + server.toString());
   }
}
