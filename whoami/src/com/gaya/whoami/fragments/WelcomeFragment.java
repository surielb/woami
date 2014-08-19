package com.gaya.whoami.fragments;

import android.os.*;
import android.support.v4.app.Fragment;
import android.view.*;
import android.widget.*;
import com.facebook.model.*;
import com.facebook.widget.*;
import com.gaya.whoami.*;
import com.gaya.whoami.R.*;

/**
 * here is where te application logic starts
 */
public class WelcomeFragment extends FacebookFragment {

    private ProfilePictureView profilePictureView;
    private TextView profileName;

    public WelcomeFragment() {
        setHasOptionsMenu(true);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(layout.fragment_welcome, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        profileName = (TextView) view.findViewById(id.profile_name);
        profilePictureView = (ProfilePictureView) view.findViewById(id.profile_pic);
        //ok here is where we need to select the fragment to show
        //lets say we know nothing about you, so we ask for info
        if (getChildFragmentManager().findFragmentById(id.fragment_container) == null)
            getChildFragmentManager()
                    .beginTransaction()
                    .replace(id.fragment_container, Fragments.ABOUT_ME.get(getActivity()))
                    .commit();
    }


    protected void updateUI() {
        if (!isAdded())
            return;
        //update user info
        GraphUser user = getUser();
        if (user != null) {
            profilePictureView.setProfileId(user.getId());
            profileName.setText(getActivity().getString(string.welcome, user.getName()));
        } else {
            profilePictureView.setProfileId(null);
            profileName.setText("Unknown");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Globals.detachFragment(this);

    }
}
