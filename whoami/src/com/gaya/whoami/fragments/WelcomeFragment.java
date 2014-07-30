package com.gaya.whoami.fragments;

import android.os.*;
import android.view.*;
import android.widget.*;
import com.facebook.model.*;
import com.facebook.widget.*;
import com.gaya.whoami.*;
import com.gaya.whoami.R.*;

/**
 * Created with IntelliJ IDEA. User: Suriel Date: 7/30/14 Time: 9:44 PM To change this template use File | Settings |
 * File Templates.
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
    }


    protected void updateUI() {
        if (!isAdded())
            return;
        GraphUser user = getUser();
        if (user != null) {
            profilePictureView.setProfileId(user.getId());
            profileName.setText(getActivity().getString(string.welcome, user.getName()));
        } else {
            profilePictureView.setProfileId(null);
            profileName.setText("Unknown");
        }
    }
}
