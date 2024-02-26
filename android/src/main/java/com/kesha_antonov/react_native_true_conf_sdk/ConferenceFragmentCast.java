package com.kesha_antonov.react_native_true_conf_sdk;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import com.facebook.react.bridge.ReactApplicationContext;
import androidx.fragment.app.FragmentActivity;

import com.trueconf.sdk.gui.fragments.ConferenceFragment;

import android.util.Log;

public class ConferenceFragmentCast extends ConferenceFragment {
    private ImageButton btnMic;
    private ImageButton btnCam;
    private ImageButton btnSpeaker;
    TrueConfSDKViewManager tcsdkViewManager;

    public ConferenceFragmentCast(int contentLayoutId, TrueConfSDKViewManager vm) {
        super(contentLayoutId);
        tcsdkViewManager = vm;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RelativeLayout conferenceView = view.findViewById(R.id.conference_view);
        ViewGroup selfView = view.findViewById(R.id.self_view);

        addGFXFragment(conferenceView);
        addGFXSelfViewSurface(selfView);

        btnMic = view.findViewById(R.id.btnMic);
        btnCam = view.findViewById(R.id.btnCam);
        ImageButton btnClose = view.findViewById(R.id.btnClose);
        btnSpeaker = view.findViewById(R.id.btnSpeaker);
        ImageButton btnChat = view.findViewById(R.id.btnChat);

        btnMic.setOnClickListener(view1 -> onSwitchMic());
        btnCam.setOnClickListener(view1 -> onSwitchCamera());
        btnSpeaker.setOnClickListener(view1 -> onSwitchSpeaker());
        btnClose.setOnClickListener(view1 -> onHangupClick());

        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TrueConfSDKViewManager.TAG, "btnChat onClick");

                tcsdkViewManager.onPressButton("chat");
            }
        });
    }

    @Override
    public void onSwitchCameraApplied(boolean isVideoEnabled) {
        super.onSwitchCameraApplied(isVideoEnabled);
        if (isVideoEnabled) {
            btnCam.setImageDrawable(AppCompatResources.getDrawable(requireContext(), R.drawable.ic_camera));
        } else {
            btnCam.setImageDrawable(AppCompatResources.getDrawable(requireContext(), R.drawable.ic_camera_off));
        }
    }

    @Override
    public void onSwitchMicApplied(boolean isRecorderMute) {
        super.onSwitchMicApplied(isRecorderMute);
        if (isRecorderMute) {
            btnMic.setImageDrawable(AppCompatResources.getDrawable(requireContext(), R.drawable.ic_mic_off));
        } else {
            btnMic.setImageDrawable(AppCompatResources.getDrawable(requireContext(), R.drawable.ic_mic));
        }
    }

    @Override
    public void onSwitchSpeakerApplied(boolean isPlayerMute) {
        super.onSwitchSpeakerApplied(isPlayerMute);
        if (isPlayerMute) {
            btnSpeaker.setImageDrawable(
                    AppCompatResources.getDrawable(requireContext(), com.vc.videochat.R.drawable.ic_cancel));
        } else {
            btnSpeaker.setImageDrawable(AppCompatResources.getDrawable(requireContext(), R.drawable.ic_speaker));
        }
    }
}
