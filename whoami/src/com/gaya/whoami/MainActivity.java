package com.gaya.whoami;


import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import com.gaya.whoami.activities.SocialActivity;
import com.gaya.whoami.events.IEventManager;
import com.gaya.whoami.fragments.Fragments;
import com.gaya.whoami.players.User;
import com.gaya.whoami.social.ISocialManager;
import com.gaya.whoami.social.SocialProfileChangedListener;
import com.google.android.gms.common.ConnectionResult;

import static com.gaya.whoami.ioc.ServiceLocator.getService;
import static com.google.android.gms.common.GooglePlayServicesUtil.isGooglePlayServicesAvailable;
import static com.google.android.gms.common.GooglePlayServicesUtil.showErrorDialogFragment;

/**
 * Our main activity tat handles the basic user login and fragment flow Fragments are defined in Fragments
 *
 * @see com.gaya.whoami.fragments.Fragments
 */
public class MainActivity extends SocialActivity implements SocialProfileChangedListener {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mTitle = mDrawerTitle = getTitle();
        getService(IEventManager.class).addEvent(SocialProfileChangedListener.class, this, IEventManager.INFINITE);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, Gravity.LEFT);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                mTitle = getTitle();
                getSupportActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.

        mDrawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    /**
     * helper function to display the given fragment
     *
     * @param fragment       the fragment to display
     * @param addToBackStack should this fragment be added to the BackStack
     */
    public Fragment showFragment(Fragments fragment, boolean addToBackStack) {
        try {
            if (fragment == getActiveFragment())
                return fragment.get(this);
            Fragment f = fragment.get(this);
            FragmentManager fragmentManager = getSupportFragmentManager();
            if (!addToBackStack) {

                while (fragmentManager.getBackStackEntryCount() > 0) {
                    fragmentManager.popBackStackImmediate();
                }

            }


            FragmentTransaction transaction = fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, f);

            if (addToBackStack)
                transaction.addToBackStack(null);

            if (fragment == Fragments.SPLASH) {
                getSupportActionBar().hide();
                mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            } else {
                mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                getSupportActionBar().show();
            }

            transaction.commit();
            return f;
        }finally {
            mDrawerLayout.closeDrawers();
        }
    }


    public Fragments getActiveFragment() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        return Fragments.getActiveFragment(fragment);
    }

    final int RQS_GooglePlayServices = 1;


    @Override
    public void onResume() {
        super.onResume();


        int resultCode = isGooglePlayServicesAvailable(getApplicationContext());

        if (resultCode != ConnectionResult.SUCCESS) {
            showErrorDialogFragment(resultCode, this, RQS_GooglePlayServices);
            //Dialog dialog =GooglePlayServicesUtil.getErrorDialog(resultCode, this, RQS_GooglePlayServices);
        }


        User profile = getService(ISocialManager.class).getActiveUser();
        if (profile != null) {
            // if the session is already open, try to show the selection fragment
            showFragment(getDefaultFragment(profile), false);

        } else {
            // otherwise present the splash screen and ask the user to login
            showFragment(Fragments.SPLASH, false);
        }
    }

    private Fragments getDefaultFragment(User profile) {
        //decide what should be the first fragment
        if (profile.getAnsweredQuestions() >= 5)
            return Fragments.FIND_PLAYERS;
        return Fragments.PROFILE;
    }

    private boolean destroyed;

    @Override
    public void onDestroy() {
        super.onDestroy();
        destroyed = true;
    }

    @Override
    public void onSocialProfileChanged(User socialProfile) {
        if (destroyed)
            return;
        if (socialProfile == null || !socialProfile.isAuthenticated()) {
            FragmentManager manager = getSupportFragmentManager();
            int backStackSize = manager.getBackStackEntryCount();
            for (int i = 0; i < backStackSize; i++) {
                manager.popBackStack();
            }

            showFragment(Fragments.SPLASH, false);
        } else {
            if (getActiveFragment() == Fragments.SPLASH)
                showFragment(getDefaultFragment(socialProfile), false);
            //no need to swap fragment
        }
    }
}