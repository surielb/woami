package com.gaya.whoami;

import android.content.*;
import android.content.pm.*;
import android.content.pm.PackageManager.*;
import android.content.pm.Signature;
import android.os.*;
import android.support.v4.app.*;
import android.util.*;
import android.view.*;
import com.facebook.*;
import com.facebook.Session.*;
import com.gaya.whoami.R.*;
import com.gaya.whoami.fragments.*;

import java.security.*;

public class MainActivity extends FragmentActivity {

    private UiLifecycleHelper uiHelper;
    private boolean isResumed;
    private final StatusCallback statusCallback = new StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionChanged(session, state, exception);
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case id.settings:
                showFragment(FragmentsEnum.SETTINGS, true);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void onSessionChanged(Session session, SessionState state, Exception exception) {
        if (isResumed) {
            FragmentManager manager = getSupportFragmentManager();
            int backStackSize = manager.getBackStackEntryCount();
            for (int i = 0; i < backStackSize; i++) {
                manager.popBackStack();
            }
            // check for the OPENED state instead of session.isOpened() since for the
            // OPENED_TOKEN_UPDATED state, the selection fragment should already be showing.
            if (state.equals(SessionState.OPENED)) {
                showFragment(FragmentsEnum.WELCOME, false);
            } else if (state.isClosed()) {
                showFragment(FragmentsEnum.SPLAH, false);
            }
        }

        boolean isSplash = getSupportFragmentManager().findFragmentById(id.fragment_container) instanceof SplashFragment;
        if (session != null && session.isOpened()) {
            if (isSplash) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(id.fragment_container, FragmentsEnum.WELCOME.get(this), "Welcome")
                        .commit();
            }
        } else {
            if (!isSplash) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(id.fragment_container, FragmentsEnum.SPLAH.get(this), "Splash")
                        .commit();
            }
        }
    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uiHelper = new UiLifecycleHelper(this, statusCallback);
        uiHelper.onCreate(savedInstanceState);
        printKeyHash();
        setContentView(R.layout.main);
    }

    @Override
    public void onResume() {
        super.onResume();
        uiHelper.onResume();
        isResumed = true;

        // Call the 'activateApp' method to log an app event for use in analytics and advertising reporting.  Do so in
        // the onResume methods of the primary Activities that an app may be launched into.
        AppEventsLogger.activateApp(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
        isResumed = false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);


    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        Session session = Session.getActiveSession();

        if (session != null && session.isOpened()) {
            // if the session is already open, try to show the selection fragment
            showFragment(FragmentsEnum.WELCOME, false);

        } else {
            // otherwise present the splash screen and ask the user to login, unless the user explicitly skipped.
            showFragment(FragmentsEnum.SPLAH, false);
        }
    }

    void showFragment(FragmentsEnum fragment, boolean addToBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction()
                .replace(id.fragment_container, fragment.get(this));
        if (addToBackStack)
            transaction.addToBackStack(null);
        transaction.commit();
    }

    void printKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.gaya.whoami",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }


}
