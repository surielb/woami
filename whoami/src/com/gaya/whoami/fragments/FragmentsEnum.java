package com.gaya.whoami.fragments;

import android.app.*;
import android.support.v4.app.Fragment;
import com.facebook.widget.*;

import java.util.*;

/**
 * Created with IntelliJ IDEA. User: Suriel Date: 7/30/14 Time: 10:13 PM To change this template use File | Settings |
 * File Templates.
 */
public enum FragmentsEnum {
    SPLAH(SplashFragment.class),
    WELCOME(WelcomeFragment.class),
    SETTINGS(UserSettingsFragment.class);

    FragmentsEnum(Class<? extends Fragment> type) {
        this.type = type;
    }

    private final Class<? extends Fragment> type;
    private final Map<Activity,Fragment> fragmentMap = new WeakHashMap<Activity, Fragment>();


    public synchronized Fragment get(Activity context) {
        Fragment fragment = fragmentMap.get(context);
        if (fragment == null)
            fragmentMap.put(
                    context,
                    fragment = Fragment.instantiate(context, type.getName(), context.getIntent().getExtras())
            );
        return fragment;
    }
}