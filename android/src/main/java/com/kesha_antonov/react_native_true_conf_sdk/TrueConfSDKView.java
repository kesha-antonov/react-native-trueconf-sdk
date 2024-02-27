package com.kesha_antonov.react_native_true_conf_sdk;

import android.content.Context;
import android.graphics.Color;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class TrueConfSDKView extends FrameLayout {
  public TrueConfSDKView(@NonNull Context context) {
    super(context);
    // set padding and background color
    this.setPadding(16, 16, 16, 16);
    this.setBackgroundColor(Color.parseColor("#00ff00"));

    // add default text view
    TextView text = new TextView(context);
    text.setText("Welcome to Android Fragments with React Native.");
    this.addView(text);
  }
}
