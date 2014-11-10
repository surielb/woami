package com.gaya.whoami.fragments;

import android.app.Activity;
import android.support.v4.app.Fragment;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * Helper enum that holds a singleton fragment for each activity
 */
public enum Fragments {

    /**
     * The splash (login) screen
     */
    SPLASH(SplashFragment.class),
    /**
     * The first screen of the app (Profile)
     */
    PROFILE(ProfileFragment.class),
    /**
     * Search for players fragment
     */
    FIND_PLAYERS(FindPlayersFragment.class),

    /**
     * the settings screen
     */
    SETTINGS(SettingsFragment.class),

    PLAY(GameFragment.class),
    ;

    public static Fragments getActiveFragment(Fragment fragment)
    {
        if (fragment instanceof SplashFragment)
            return Fragments.SPLASH;
        if (fragment instanceof ProfileFragment)
            return Fragments.PROFILE;
        if (fragment instanceof FindPlayersFragment)
            return Fragments.FIND_PLAYERS;
        return null;
    }

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