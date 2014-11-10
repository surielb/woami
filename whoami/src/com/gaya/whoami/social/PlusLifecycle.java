package com.gaya.whoami.social;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;

/**
 * @author suriel
 *         Date: 9/2/14
 *         Time: 12:03 PM
 */
public class PlusLifecycle implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private int lastError = -1;

    public interface SessionCallback {
        void onSessionStateChanged(GoogleApiClient client);
    }

    private final SessionCallback sessionCallback;

    private boolean connectionRequested;

    /* Request code used to invoke sign in user interactions. */
    private static final int RC_SIGN_IN = 580;

    /* Client used to interact with Google APIs. */
    private GoogleApiClient mGoogleApiClient;

    /* A flag indicating that a PendingIntent is in progress and prevents
     * us from starting further intents.
     */
    private boolean mIntentInProgress;
    private final Activity context;

    public GoogleApiClient getClient() {
        return mGoogleApiClient;
    }

    public PlusLifecycle(Activity context, SessionCallback sessionCallback) {
        this.context = context;
        this.sessionCallback = sessionCallback;

        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .build();
    }

    public void connect() {
        if (mGoogleApiClient.isConnected())
            mGoogleApiClient.disconnect();
        connectionRequested = true;
        mGoogleApiClient.connect();
    }

    public void disconnect() {
        if (mGoogleApiClient.isConnected()) {
            //Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient);
            mGoogleApiClient.disconnect();
            mGoogleApiClient.connect();
        }
        connectionRequested = false;
        if (sessionCallback != null)
            sessionCallback.onSessionStateChanged(mGoogleApiClient);
    }


    public void onStart() {
        mGoogleApiClient.connect();

    }

    public void onStop() {

        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        connectionRequested = false;
        mIntentInProgress = false;
        // We've resolved any connection errors.  mGoogleApiClient can be used to
        // access Google APIs on behalf of the user.
        if (sessionCallback != null)
            sessionCallback.onSessionStateChanged(mGoogleApiClient);

    }

    @Override
    public void onConnectionSuspended(int cause) {
        if (sessionCallback != null)
            sessionCallback.onSessionStateChanged(mGoogleApiClient);
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {

        if (connectionRequested) {
            if (!mIntentInProgress && result.hasResolution()) {

                try {
                    mIntentInProgress = true;
                    result.startResolutionForResult(context, RC_SIGN_IN);

                } catch (IntentSender.SendIntentException e) {
                    // The intent was canceled before it was sent.  Return to the default
                    // state and attempt to connect to get an updated ConnectionResult.
                    mIntentInProgress = false;
                    mGoogleApiClient.connect();
                }
            } else {
                mIntentInProgress = false;
                if (sessionCallback != null)
                    sessionCallback.onSessionStateChanged(mGoogleApiClient);
            }
        }
    }


    public void onActivityResult(int requestCode, int responseCode, Intent intent) {
        if (requestCode == RC_SIGN_IN) {

            mIntentInProgress = false;

            if (!mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
            }


        }
    }


}
