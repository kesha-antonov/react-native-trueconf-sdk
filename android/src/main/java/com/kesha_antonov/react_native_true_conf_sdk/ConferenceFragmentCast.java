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
        btnMic.setOnClickListener(view1 -> onSwitchMic());

        btnCam = view.findViewById(R.id.btnCam);
        btnCam.setOnClickListener(view1 -> onSwitchCamera());

        btnSpeaker = view.findViewById(R.id.btnSpeaker);
        btnSpeaker.setOnClickListener(view1 -> onSwitchSpeaker());

        ImageButton btnClose = view.findViewById(R.id.btnClose);
        btnClose.setOnClickListener(view1 -> onHangupClick());

        ImageButton btnChat = view.findViewById(R.id.btnChat);
        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TrueConfSDKViewManager.TAG, "btnChat onClick");

                tcsdkViewManager.onPressButton("chat");
            }
        });

        updateMicButton(!tcsdkViewManager.isMuted);
        updateCameraButton(tcsdkViewManager.isCameraOn);
    }

    private void updateButtonBackground (ImageButton btn, boolean isActive) {
        btn.setBackgroundResource(isActive ? R.drawable.bg_btn_round : R.drawable.bg_btn_round_black);
    }

    private void updateMicButton (boolean isActive) {
        updateButtonBackground(btnMic, isActive);
        btnMic.setImageDrawable(
            AppCompatResources.getDrawable(requireContext(), isActive ? R.drawable.ic_mic : R.drawable.ic_mic_off));
    }

    private void updateCameraButton (boolean isActive) {
        updateButtonBackground(btnCam, isActive);
        btnCam.setImageDrawable(AppCompatResources.getDrawable(requireContext(),
                isActive ? R.drawable.ic_camera : R.drawable.ic_camera_off));
    }

    @Override
    public void onSwitchMicApplied(boolean isMuted) {
        Log.d(TrueConfSDKViewManager.TAG, "onSwitchMicApplied isMuted " + isMuted);

        super.onSwitchMicApplied(isMuted);
        updateMicButton(!isMuted);
    }

    @Override
    public void onSwitchCameraApplied(boolean isCameraOn) {
        super.onSwitchCameraApplied(isCameraOn);
        updateCameraButton(isCameraOn);
    }

    @Override
    public void onSwitchSpeakerApplied(boolean isMuted) {
        super.onSwitchSpeakerApplied(isMuted);
        updateButtonBackground(btnSpeaker, !isMuted);
        btnSpeaker.setImageDrawable(
            AppCompatResources.getDrawable(requireContext(), isMuted ? com.vc.videochat.R.drawable.ic_cancel : R.drawable.ic_speaker));
    }
}
