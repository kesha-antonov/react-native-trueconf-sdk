package com.kesha_antonov.rn_true_conf_react_sdk;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;

// replace with your view's import
import com.kesha_antonov.rn_true_conf_react_sdk.TrueConfSDKView;

public class MyFragment extends Fragment {
    TrueConfSDKView trueConfSDKView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        super.onCreateView(inflater, parent, savedInstanceState);
        trueConfSDKView = new TrueConfSDKView(this.getContext());
        return trueConfSDKView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // do any logic that should happen in an `onCreate` method, e.g:
        // trueConfSDKView.onCreate(savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();
        // do any logic that should happen in an `onPause` method
        // e.g.: trueConfSDKView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
       // do any logic that should happen in an `onResume` method
       // e.g.: trueConfSDKView.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // do any logic that should happen in an `onDestroy` method
        // e.g.: trueConfSDKView.onDestroy();
    }
}
