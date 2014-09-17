package com.gaya.whoami.fragments;

import android.os.Bundle;
import android.support.v4.app.*;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.gaya.whoami.Globals;
import com.gaya.whoami.R;
import android.widget.ListView;
import com.gaya.whoami.adapters.PlayersAdapter;


public class FragmentUsers extends Fragment{


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_users, container, false);


        Globals.getPlayerManager().scan();


        ListView list = (ListView)v.findViewById(R.id.usersListView);

        PlayersAdapter playerAdapter = new PlayersAdapter(getActivity());

        list.setAdapter(playerAdapter);






        return v;
    }




}

