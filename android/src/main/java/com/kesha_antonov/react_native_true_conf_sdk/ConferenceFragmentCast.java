package com.kesha_antonov.react_native_true_conf_sdk;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.RenderEffect;
import android.graphics.Shader;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.UiThreadUtil;
import com.facebook.react.bridge.WritableMap;

import com.trueconf.sdk.TrueConfSDK;
import com.trueconf.sdk.gui.fragments.ConferenceFragment;

import android.util.Log;

import jp.wasabeef.blurry.Blurry;

import android.graphics.drawable.Drawable;

import java.util.Objects;

import eightbitlab.com.blurview.BlurAlgorithm;
import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderEffectBlur;
import eightbitlab.com.blurview.RenderScriptBlur;

public class ConferenceFragmentCast extends ConferenceFragment {
    private ImageButton btnChat;
    private ImageButton btnAudio;
    private ImageButton btnCam;
    private ImageButton btnMic;
    private View mainView;

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

        mainView = view;

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

//        ViewGroup btnCamContainer = view.findViewById(R.id.btnCamContainer);
        // ImageView imageView = view.findViewById(R.id.btnCamBackground);

        // imageView.post(new Runnable() {
        //     @Override
        //     public void run() {
        //         Blurry.with(requireContext())
        //             .radius(27)
        //             .sampling(1)
        //             .color(Color.argb(60, 255, 255, 255))
        //             .capture(view.findViewById(R.id.btnCam))
        //             .into(imageView);
        //     }
        // });

        // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        //     btnCam.setRenderEffect(RenderEffect.createBlurEffect(5, 5, Shader.TileMode.CLAMP));
        // }

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
        updateAudioButton(tcsdkViewManager.isAudioMuted);
        updateCameraButton(!tcsdkViewManager.isCameraMuted);
        updateMicButton(tcsdkViewManager.isMicMuted);
        updateChatButtonVisibility();

        setupBlurView();

//         new Thread(() -> {
//             try {
//                 Thread.sleep(2000);
//                 UiThreadUtil.runOnUiThread(new Runnable() {
//                     @Override
//                     public void run() {
// //                        Thread.sleep(2000);
//                         setupBlurView();
//                     }
//                 });
//             } catch (InterruptedException e) {
//                 Log.e(TrueConfSDKViewManager.TAG, "onViewCreated setupBlurView error: " + e.getMessage());
//                 e.printStackTrace();
//             }
//         }).start();
    }

    @NonNull
    private BlurAlgorithm getBlurAlgorithm() {
        BlurAlgorithm algorithm;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            algorithm = new RenderEffectBlur();
        } else {
            algorithm = new RenderScriptBlur(requireContext());
        }
        return algorithm;
    }

    private void setupBlurView () {
        final float radius = 25f;

//        https://github.com/Dimezis/BlurView?tab=readme-ov-file#how-to-use
        View decorVIew = Objects.requireNonNull(requireActivity()).getWindow().getDecorView();
//        View decorVIew = Objects.requireNonNull(tcsdkViewManager.reactContext.getCurrentActivity()).getWindow().getDecorView();
        ViewGroup rootView = decorVIew.findViewById(android.R.id.content);
        Drawable windowBackground = decorVIew.getBackground();
        BlurAlgorithm algorithm = getBlurAlgorithm();

        BlurView blurView = mainView.findViewById(R.id.blurView);
        blurView.setupWith(rootView, algorithm)
                .setFrameClearDrawable(windowBackground)
                .setBlurRadius(10)
                .setBlurEnabled(true)
                .setBlurAutoUpdate(true);
    }

    public void updateChatButtonVisibility () {
        if (tcsdkViewManager.isInConference) {
            btnChat.setClickable(true);
            btnChat.getDrawable().setTint(AppCompatResources.getColorStateList(requireContext(), R.color.tcsdk_button_icon).getDefaultColor());
        } else {
            btnChat.setClickable(false);
            btnChat.getDrawable().setTint(AppCompatResources.getColorStateList(requireContext(), R.color.tcsdk_button_icon_inactive).getDefaultColor());
        }
    }

    private void updateButtonBackground(ImageButton btn, boolean isActive) {
        btn.setBackgroundResource(isActive ? R.drawable.button_round : R.drawable.button_round_inactive);
    }

    private void updateMicButton(boolean isActive) {
        updateButtonBackground(btnMic, isActive);
    }

    private void updateCameraButton(boolean isActive) {
        // updateButtonBackground(btnCam, isActive);
        // updateButtonBackground(mainView.findViewById(R.id.btnCamContainer), isActive);
    }

    private void updateAudioButton(boolean isActive) {
        updateButtonBackground(btnAudio, isActive);
    }

    @Override
    public void onSwitchSpeakerApplied(boolean isMuted) {
        Log.d(TrueConfSDKViewManager.TAG, "onSwitchSpeakerApplied isMuted " + isMuted);

        super.onSwitchSpeakerApplied(isMuted);
        updateAudioButton(isMuted);
    }

    @Override
    public void onSwitchCameraApplied(boolean isCameraOn) {
        Log.d(TrueConfSDKViewManager.TAG, "onSwitchCameraApplied isCameraOn " + isCameraOn);

        super.onSwitchCameraApplied(isCameraOn);
        updateCameraButton(isCameraOn);
    }

    @Override
    public void onSwitchMicApplied(boolean isMuted) {
        Log.d(TrueConfSDKViewManager.TAG, "onSwitchMicApplied isMuted " + isMuted);

        super.onSwitchMicApplied(isMuted);
        updateMicButton(isMuted);
    }

}
