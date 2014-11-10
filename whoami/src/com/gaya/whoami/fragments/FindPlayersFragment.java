package com.gaya.whoami.fragments;


import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;
import com.gaya.whoami.MainActivity;
import com.gaya.whoami.R;
import com.gaya.whoami.adapters.PlayersAdapter;
import com.gaya.whoami.entities.ParseUser;
import com.parse.ParseQueryAdapter;

import java.util.List;


/**
 * Created by Suri on 11/8/2014.
 */
public class FindPlayersFragment extends ListFragment {
    private ParseQueryAdapter<ParseUser> userParseQueryAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_users, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                userParseQueryAdapter.loadObjects();
            }
        });
        swipeRefreshLayout.setColorScheme(R.color.ucolor_1, R.color.ucolor_2, R.color.ucolor_3, R.color.ucolor_4);
        userParseQueryAdapter = new PlayersAdapter(getActivity());
        userParseQueryAdapter.addOnQueryLoadListener(new ParseQueryAdapter.OnQueryLoadListener<ParseUser>() {
            @Override
            public void onLoading() {
                swipeRefreshLayout.setRefreshing(true);
            }

            @Override
            public void onLoaded(List<ParseUser> parseUsers, Exception e) {
                swipeRefreshLayout.setRefreshing(false);
                if (e != null)
                    Toast.makeText(getActivity(), "Error loading users", Toast.LENGTH_LONG).show();
            }
        });
        userParseQueryAdapter.loadObjects();
        setListAdapter(userParseQueryAdapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        ParseUser user = userParseQueryAdapter.getItem(position);
        GameFragment fragment = (GameFragment) ((MainActivity)getActivity()).showFragment(Fragments.PLAY,true);
        fragment.setPlayer(user);
    }
}
