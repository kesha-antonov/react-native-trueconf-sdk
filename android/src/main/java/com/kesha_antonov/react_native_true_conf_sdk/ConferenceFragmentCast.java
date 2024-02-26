package com.kesha_antonov.react_native_true_conf_sdk;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.trueconf.sdk.gui.fragments.ConferenceFragment;

public class ConferenceFragmentCast extends ConferenceFragment {

    public ConferenceFragmentCast(int contentLayoutId) {
        super(contentLayoutId);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RelativeLayout gfxFragmentCast = view.findViewById(R.id.gfxFragmentCast);
        ViewGroup selfView = view.findViewById(R.id.self_view);

        addGFXFragment(gfxFragmentCast);
        addGFXSelfViewSurface(selfView);
    }
}
