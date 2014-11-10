package com.gaya.whoami.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import com.facebook.Session;
import com.gaya.whoami.ImageLoaderHelpers;
import com.gaya.whoami.MainActivity;
import com.gaya.whoami.R;
import com.gaya.whoami.activities.SocialActivity;
import com.gaya.whoami.events.IEventManager;
import com.gaya.whoami.players.User;
import com.gaya.whoami.social.ISocialManager;
import com.gaya.whoami.social.SocialIdentity;
import com.gaya.whoami.social.SocialProfileChangedListener;
import com.gaya.whoami.widget.ImageProcessors;
import com.gaya.whoami.widget.ProcessImageView;

import static com.gaya.whoami.ioc.ServiceLocator.getService;


/**
 * @author suriel Date: 9/2/14 Time: 5:37 PM
 */
public class SettingsFragment extends Fragment implements SocialProfileChangedListener {

    private ProcessImageView profileImage;
    private TextView profileName;
    private TextView profileType;


    public SettingsFragment() {
        getService(IEventManager.class).addEvent(SocialProfileChangedListener.class, this, IEventManager.INFINITE);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(com.gaya.whoami.R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.signout).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                SocialActivity activity = (SocialActivity) getActivity();
                activity.getPlusLifecycle().disconnect();
                Session session = Session.getActiveSession();
                if (session != null)
                    session.closeAndClearTokenInformation();
            }
        });
        profileImage = (ProcessImageView) view.findViewById(com.gaya.whoami.R.id.profile_image);
        profileImage.setBitmapProcessor(ImageProcessors.CIRCLE_BITMAP_PROCESSOR);
        profileName = (TextView) view.findViewById(com.gaya.whoami.R.id.profile_name);
        profileType = (TextView) view.findViewById(R.id.profile_type);

        view.findViewById(R.id.answer).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity activity = (MainActivity) getActivity();
                if (activity != null)
                    activity.showFragment(Fragments.PROFILE, false);
            }
        });
        view.findViewById(R.id.find).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity activity = (MainActivity) getActivity();
                if (activity != null)
                    activity.showFragment(Fragments.FIND_PLAYERS, true);
            }
        });

        onSocialProfileChanged(getService(ISocialManager.class).getActiveUser());


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onSocialProfileChanged(User socialProfile) {
        if (profileImage != null && socialProfile != null && getActivity() != null) {
            profileImage.setImageUrl(socialProfile.getImageUrl(), ImageLoaderHelpers.getImageLoader());
            profileName.setText(socialProfile.getName());
            profileType.setText(getActivity().getString(R.string.connected_with, socialProfile.getType() == SocialIdentity.TYPE_FACEBOOK ? "Facebook" : "Google+"));
        }
    }

}
