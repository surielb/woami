package com.gaya.whoami.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import com.gaya.whoami.MainActivity;
import com.gaya.whoami.R;
import com.gaya.whoami.R.*;

/**
 * Created by Lenovo-User on 08/08/2014.
 */
public class CreateGameFragment  extends FacebookFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(layout.fragment_create_game, container, false);

        final NumberPicker nph = (NumberPicker) v.findViewById(R.id.timeh);
        nph.setMaxValue(5);
        nph.setMinValue(0);
        nph.setWrapSelectorWheel(false);

        final NumberPicker npm = (NumberPicker) v.findViewById(R.id.timem);
        npm.setMaxValue(59);
        npm.setMinValue(2);
        npm.setWrapSelectorWheel(false);

        Button b = (Button)v.findViewById(R.id.BtnCreateGame);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity main = ((MainActivity)getActivity());

                main.gameSettings.timeMin = npm.getValue();
                main.gameSettings.timeHour = nph.getValue();

                EditText quest1 = (EditText)v.findViewById(id.quest1);
                main.gameSettings.question1 = quest1.getText().toString();

                EditText quest2 = (EditText)v.findViewById(id.quest2);
                main.gameSettings.question2 = quest2.getText().toString();

                EditText quest3 = (EditText)v.findViewById(id.quest3);
                main.gameSettings.question3 = quest3.getText().toString();

                main.showFragment(Fragments.STARTGAME, false);
            }
        });


        return v;
    }

    @Override
    protected void updateUI() {

    }
}

