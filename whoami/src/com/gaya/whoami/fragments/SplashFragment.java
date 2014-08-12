package com.gaya.whoami.fragments;

import android.os.*;
import android.support.v4.app.*;
import android.view.*;
import com.gaya.whoami.R.*;

/**
 * Noting fancy, just a login button. MainActivity handles the transition
 */
public class SplashFragment extends Fragment {

     @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(layout.fragment_splash,container,false);
    }
}


