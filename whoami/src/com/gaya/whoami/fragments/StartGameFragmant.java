package com.gaya.whoami.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gaya.whoami.R;
import com.gaya.whoami.R.*;

/**
 * Created by Lenovo-User on 11/08/2014.
 */
public class StartGameFragmant extends FacebookFragment{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(layout.fragmant_start_game, container, false);

        return v;
    }

    @Override
    protected void updateUI() {

    }
}
