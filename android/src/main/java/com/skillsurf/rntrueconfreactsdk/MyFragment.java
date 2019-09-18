package com.skillsurf.rntrueconfreactsdk;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.skillsurf.rntrueconfreactsdk.R;

public class MyFragment extends Fragment {
    public static MyFragment newInstance() {
        MyFragment myFragment = new MyFragment();

//        Bundle args = new Bundle();
//        args.putInt("someInt", someInt);
//        myFragment.setArguments(args);

        return myFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.layout_container, parent, false);
    }

}
