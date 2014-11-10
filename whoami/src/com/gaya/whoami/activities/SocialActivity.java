package com.gaya.whoami.activities;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
import com.facebook.AppEventsLogger;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.gaya.whoami.Logger;
import com.gaya.whoami.social.PlusLifecycle;
import com.gaya.whoami.social.SocialHelper;
import com.google.android.gms.common.api.GoogleApiClient;
import com.parse.ParseAnalytics;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public abstract class SocialActivity extends ActionBarActivity {
    private UiLifecycleHelper uiHelper;
    private PlusLifecycle plusLifecycle;

    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };
    private PlusLifecycle.SessionCallback plusCallback = new PlusLifecycle.SessionCallback() {
        @Override
        public void onSessionStateChanged(GoogleApiClient client) {
            SocialActivity.this.onSessionStateChanged(client);
        }
    };
    private AppEventsLogger appEventsLogger;


    public PlusLifecycle getPlusLifecycle(){
        return plusLifecycle;
    }
    public UiLifecycleHelper getUiHelper(){
        return uiHelper;
    }


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        appEventsLogger = AppEventsLogger.newLogger(this);
        uiHelper = new UiLifecycleHelper(this, callback);
        uiHelper.onCreate(savedInstanceState);
        plusLifecycle = new PlusLifecycle(this, plusCallback);
        printHashKey();
    }

    @Override
    protected void onStart() {
        super.onStart();
        plusLifecycle.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        plusLifecycle.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        uiHelper.onResume();
        //isResumed = true;

        // Call the 'activateApp' method to log an app event for use in analytics and advertising reporting.  Do so in
        // the onResume methods of the primary Activities that an app may be launched into.
        //AppEventsLogger.activateApp(this);
        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
        //isResumed = false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
        plusLifecycle.onActivityResult(requestCode, resultCode, data);
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

    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        appEventsLogger = AppEventsLogger.newLogger(this, session);
        SocialHelper.handleFacebookSession(session);

    }

    private void onSessionStateChanged(GoogleApiClient client) {
        SocialHelper.handlePlusSession(client);
    }

    protected void printHashKey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.gaya.whoami",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                StringBuilder result = new StringBuilder();
                byte[] bytes = md.digest();
                for (byte b : bytes) {
                    if(result.length()>0)
                        result.append(":"); // delimiter
                    result.append(String.format("%02X", b));
                }
                Logger.d("KeyHash:'%s'", Base64.encodeToString(bytes, Base64.DEFAULT));
                Logger.d("KeyHash:'%s'", result.toString());
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }
}
