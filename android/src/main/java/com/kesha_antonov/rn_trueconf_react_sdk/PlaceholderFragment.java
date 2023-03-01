package com.trueconfsdk;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import com.vc.TrueConfSDK;
import com.vc.data.SelfViewInfo;
import com.vc.data.VideoViewInfo;
import com.vc.interfaces.TrueConfListener;


import java.util.Objects;


/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment implements TrueConfListener.LoginEventsCallback, TrueConfListener.ServerStatusEventsCallback{

    private EditText mETLogin, mETPass, mETServerId, mETUserId;
    private Spinner mETServer;
    private Button mBtnLogin;
    private Button mBtnCall;
    private Button mBtnLogout;
    private TextView mConnectionStatus, mTvLoginPass;

    private InputMethodManager imm;
    private String[] data;

    public PlaceholderFragment() {
    }


    private final View.OnClickListener loginClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            hideSoftInput();

            if (TextUtils.isEmpty(mETLogin.getText()) & TextUtils.isEmpty(mETPass.getText())) {
                showToast(R.string.msg_empty_login_or_pass);
            } else {
                String login = mETLogin.getText().toString().trim();
                String pass = mETPass.getText().toString().trim();
                Boolean b = TrueConfSDK.getInstance().loginAs(login, pass, true, true);
            }
        }
    };


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        data = getResources().getStringArray(R.array.ip_set);
        return rootView;
    }


    @Override
    public void onViewCreated(@NonNull View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        setUpUI(v);
    }


    @Override
    public void onResume() {
        super.onResume();
        TrueConfSDK.getInstance().addTrueconfListener(this);
    }


    public void onPause() {
        super.onPause();
    }


    private void afterServerEnter() {

        hideSoftInput();
        String server;

        if (data == null || data.length == 0) {
            server = mETServerId.getText().toString();
        } else {
            server = mETServer.getSelectedItem().toString();
        }

        boolean isValid = validateInput(server);
        if (isValid) connectToServer(server);

    }


    private boolean validateInput(String server) {

        if (server.contains(":")) {

            String array[] = server.split(":");

            if (array.length != 2) {
                showToast(R.string.msg_invalid_server_port_value);
                return false;
            }

            int port;

            try {
                port = Integer.parseInt(array[1]);
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
                return false;
            }

            if (!isValidPort(port)) {
                showToast(R.string.msg_invalid_server_port_value);
                return false;
            }
        }

        return true;
    }

    private boolean isValidPort(int port) {
        return port >= 0 && port <= 99999;
    }


    private void connectToServer(final String server) {

        if (TextUtils.isEmpty(server)) return;

        if (TrueConfSDK.getInstance().checkServer(server).length() == 0) {
            showToast(R.string.msg_invalid_server_value);
            return;
        }
        TrueConfSDK.getInstance().start(getContext(), server, true);
    }


    private void showToast(@StringRes int txt) {
        Toast.makeText(getActivity(), txt, Toast.LENGTH_SHORT).show();
    }


    private void hideSoftInput() {
        imm.hideSoftInputFromWindow(mETPass.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }


    private void setUpUI(View v) {

        mETLogin = v.findViewById(R.id.et_login);
        mETPass = v.findViewById(R.id.et_pass);
        mETUserId = v.findViewById(R.id.et_call_to_peer);
        mETServer = v.findViewById(R.id.sp_server_adress);
        mETServerId = v.findViewById(R.id.et_server_adress);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()), android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        if (data == null || data.length == 0) {
            mETServer.setVisibility(View.GONE);
        } else {
            mETServerId.setVisibility(View.GONE);
        }

        mETServer.setAdapter(adapter);
        mETServer.setSelection(0);
        mBtnLogin = v.findViewById(R.id.btn_login);

        v.findViewById(R.id.btn_connect).setOnClickListener(v1 -> afterServerEnter());

        mBtnLogin.setOnClickListener(loginClickListener);
        mConnectionStatus = v.findViewById(R.id.tv_connection_state);
        mBtnCall = v.findViewById(R.id.btn_call);
        mBtnCall.setOnClickListener(v1 -> {

            if (!mETUserId.getText().toString().isEmpty()) {
                TrueConfSDK.getInstance().callTo(mETUserId.getText().toString());
            } else {
                showToast(R.string.empty_id);
            }

        });

        mBtnLogout = v.findViewById(R.id.btn_logout);
        mBtnLogout.setOnClickListener(v1 -> TrueConfSDK.getInstance().logout());
        mTvLoginPass = v.findViewById(R.id.tv_login_pass);
    }

    private void loginOkActions() {
        mBtnCall.setVisibility(View.VISIBLE);
        mETUserId.setVisibility(View.VISIBLE);
        mBtnLogout.setVisibility(View.VISIBLE);
        Objects.requireNonNull(getView()).findViewById(R.id.tv_call_to).setVisibility(View.VISIBLE);
        mBtnLogin.setVisibility(View.GONE);
    }


    public void logout() {
        mBtnCall.setVisibility(View.GONE);
        mETUserId.setVisibility(View.GONE);
        Objects.requireNonNull(getView()).findViewById(R.id.tv_call_to).setVisibility(View.GONE);
        mBtnLogin.setVisibility(View.VISIBLE);
        mBtnLogout.setVisibility(View.GONE);
    }

    @Override
    public void onLogin(boolean b, String s) {
        if (b) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    loginOkActions();
                }
            });
        }
    }

    @Override
    public void onLogout() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                logout();
            }
        });
    }

    @Override
    public void onStateChanged() { }

    @Override
    public void onServerStatus(boolean b, String s, int i) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (b) {
                    mConnectionStatus.setText(R.string.state_connected);
                    mETLogin.setVisibility(View.VISIBLE);
                    mETPass.setVisibility(View.VISIBLE);
                    mBtnLogin.setVisibility(View.VISIBLE);
                    mBtnLogout.setVisibility(View.GONE);
                    mTvLoginPass.setVisibility(View.VISIBLE);
                } else {
                    mConnectionStatus.setText(R.string.state_disconnected);
                    mETLogin.setVisibility(View.GONE);
                    mETPass.setVisibility(View.GONE);
                    mBtnLogin.setVisibility(View.GONE);
                    mTvLoginPass.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        TrueConfSDK.getInstance().removeTrueconfListener(this);
    }
}
