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
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;

import com.trueconf.sdk.TrueConfSDK;
import com.trueconf.sdk.gui.fragments.ConferenceFragment;

import android.util.Log;

public class ConferenceFragmentCast extends ConferenceFragment {
    private ImageButton btnChat;
    private ImageButton btnAudio;
    private ImageButton btnCam;
    private ImageButton btnMic;

    TrueConfSDKViewManager tcsdkViewManager;

    public ConferenceFragmentCast(int contentLayoutId, TrueConfSDKViewManager vm) {
        super(contentLayoutId);
        tcsdkViewManager = vm;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RelativeLayout conferenceView = view.findViewById(R.id.conference_view);
        ViewGroup selfView = view.findViewById(R.id.self_view);

        addGFXFragment(conferenceView);
        addGFXSelfViewSurface(selfView);

        btnChat = view.findViewById(R.id.btnChat);
        btnChat.setOnClickListener(view1 -> {
            if (tcsdkViewManager.isInConference) {
                tcsdkViewManager.onPressButton("chat", null);
            }
        });

        btnAudio = view.findViewById(R.id.btnAudio);
        btnAudio.setOnClickListener(view1 -> {
            onSwitchSpeaker();

            WritableMap params = Arguments.createMap();
            params.putBoolean("isMuted", TrueConfSDK.getInstance().isSpeakerMuted());
            tcsdkViewManager.onPressButton("audio", params);
        });

        btnCam = view.findViewById(R.id.btnCam);
        btnCam.setOnClickListener(view1 -> {
            onSwitchCamera();

            WritableMap params = Arguments.createMap();
            params.putBoolean("isMuted", TrueConfSDK.getInstance().isCameraMuted());
            tcsdkViewManager.onPressButton("camera", params);
        });

        btnMic = view.findViewById(R.id.btnMic);
        btnMic.setOnClickListener(view1 -> {
            onSwitchMic();

            WritableMap params = Arguments.createMap();
            params.putBoolean("isMuted", TrueConfSDK.getInstance().isMicrophoneMuted());
            tcsdkViewManager.onPressButton("mic", params);
        });

        ImageButton btnHangup = view.findViewById(R.id.btnHangup);
        btnHangup.setOnClickListener(view1 -> {
            // NOT USED FOR NOW BECAUSE OF WHITE FLICKERING
            // onHangupClick();

            tcsdkViewManager.onPressButton("hangup", null);
        });

        Log.d(TrueConfSDKViewManager.TAG, "ConferenceFragmentCast isMicMuted " + tcsdkViewManager.isMicMuted + " isCameraMuted " + tcsdkViewManager.isCameraMuted + " isAudioMuted " + tcsdkViewManager.isAudioMuted);
        updateMicButton(!tcsdkViewManager.isMicMuted);
        updateCameraButton(!tcsdkViewManager.isCameraMuted);
        updateAudioButton(!tcsdkViewManager.isAudioMuted);
        updateChatButtonVisibility();
    }

    public void updateChatButtonVisibility () {
        updateButtonBackground(btnChat, tcsdkViewManager.isInConference);
    }

    private void updateButtonBackground(ImageButton btn, boolean isActive) {
        btn.setBackgroundResource(isActive ? R.drawable.bg_btn_round : R.drawable.bg_btn_round_black);
    }

    private void updateMicButton(boolean isActive) {
        updateButtonBackground(btnMic, isActive);
        btnMic.setImageDrawable(
                AppCompatResources.getDrawable(requireContext(), isActive ? R.drawable.ic_mic : R.drawable.ic_mic_off));
    }

    private void updateCameraButton(boolean isActive) {
        updateButtonBackground(btnCam, isActive);
        btnCam.setImageDrawable(AppCompatResources.getDrawable(requireContext(),
                isActive ? R.drawable.ic_camera : R.drawable.ic_camera_off));
    }

    private void updateAudioButton(boolean isActive) {
        updateButtonBackground(btnAudio, isActive);
        btnAudio.setImageDrawable(
                AppCompatResources.getDrawable(requireContext(),
                        isActive ? R.drawable.ic_audio : R.drawable.ic_audio_off));
    }

    @Override
    public void onSwitchMicApplied(boolean isMuted) {
        Log.d(TrueConfSDKViewManager.TAG, "onSwitchMicApplied isMuted " + isMuted);

        super.onSwitchMicApplied(isMuted);
        updateMicButton(!isMuted);
    }

    @Override
    public void onSwitchCameraApplied(boolean isCameraOn) {
        Log.d(TrueConfSDKViewManager.TAG, "onSwitchCameraApplied isCameraOn " + isCameraOn);

        super.onSwitchCameraApplied(isCameraOn);
        updateCameraButton(isCameraOn);
    }

    @Override
    public void onSwitchSpeakerApplied(boolean isMuted) {
        Log.d(TrueConfSDKViewManager.TAG, "onSwitchSpeakerApplied isMuted " + isMuted);

        super.onSwitchSpeakerApplied(isMuted);
        updateAudioButton(!isMuted);
    }
}
