package com.gaya.whoami.fragments;

import android.app.*;
import android.support.v4.app.Fragment;
import com.facebook.widget.*;

import java.util.*;

/**
 * Helper enum that holds a singleton fragment for each activity
 */
public enum Fragments {
    /**
     * The splash (login) screen
     */
    SPLAH(SplashFragment.class),
    /**
     * The first screen of the app (welcome)
     */
    WELCOME(WelcomeFragment.class),
    /**
     * the settings screen
     */
    SETTINGS(UserSettingsFragment.class);

    Fragments(Class<? extends Fragment> type) {
        this.type = type;
    }

    private final Class<? extends Fragment> type;
    private final Map<Activity,Fragment> fragmentMap = new WeakHashMap<Activity, Fragment>();


    /**
     * Returns an instance of this fragment for te specified activity
     * @param activity the activity to host the fragment
     * @return the instance of the fragment
     */
    public synchronized Fragment get(Activity activity) {
        Fragment fragment = fragmentMap.get(activity);
        if (fragment == null)
            fragmentMap.put(
                    activity,
                    fragment = Fragment.instantiate(activity, type.getName(), activity.getIntent().getExtras())
            );
        return fragment;
    }
}