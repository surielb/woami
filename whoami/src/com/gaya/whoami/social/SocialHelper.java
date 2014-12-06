package com.gaya.whoami.social;

import android.content.Context;
import android.os.*;
import android.text.*;
import com.facebook.*;
import com.facebook.model.*;
import com.gaya.whoami.Logger;
import com.gaya.whoami.threading.Worker;
import com.google.android.gms.auth.*;
import com.google.android.gms.common.*;
import com.google.android.gms.common.api.*;
import com.google.android.gms.plus.*;
import com.google.android.gms.plus.model.people.*;


import java.io.*;

import static com.gaya.whoami.ioc.ServiceLocator.getService;

/**
 * Helper class for managing social services and converting them into social identities
 */
public class SocialHelper {
    private static Session userInfoSession;
    private static String plusUserInfoClient;
    static final Worker plusWorker = new Worker("Plus Worker");

    private static final String NAME = "name";
    private static final String ID = "id";
    private static final String PICTURE = "picture";
    private static final String URL = "link";
    private static final String FIELDS = "fields";
    private static final ISocialManager SOCIAL_MANAGER = getService(ISocialManager.class);

    private static final String REQUEST_FIELDS = TextUtils.join(",", new String[]{ID, NAME, PICTURE,URL});

    public static void handleFacebookSession(final Session currentSession) {

        //test if the session is opened and that we have not already retrieved the info for this session
        if (currentSession != null && currentSession.isOpened()) {
            if (currentSession != userInfoSession) {
                userInfoSession = currentSession;

                Request request = Request.newMeRequest(currentSession, new Request.GraphUserCallback() {
                    @Override
                    public void onCompleted(GraphUser me, Response response) {
                        if (currentSession == userInfoSession) {

                            SOCIAL_MANAGER.signIn(SocialIdentity.Factory.fromGraph(currentSession.getAccessToken(), me));
                        }

                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString(FIELDS, REQUEST_FIELDS);
                request.setParameters(parameters);
                Request.executeBatchAsync(request);
            }
        } else if(currentSession == null || currentSession.getState() != SessionState.OPENING) {
            SOCIAL_MANAGER.disconnect();
        }
    }

    public static void handlePlusSession(final GoogleApiClient client) {
        if (client != null && client.isConnected()) {
            String accountName = Plus.AccountApi.getAccountName(client);
            if (accountName != plusUserInfoClient) {
                plusUserInfoClient = accountName;
                plusWorker.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String accessToken = GoogleAuthUtil.getToken(
                                    getService(Context.class),
                                    Plus.AccountApi.getAccountName(client), "oauth2:"
                                            + Scopes.PLUS_LOGIN);
                            Person person = Plus.PeopleApi.getCurrentPerson(client);
                            SOCIAL_MANAGER.signIn(SocialIdentity.Factory.fromPlus(accessToken, person, Plus.AccountApi.getAccountName(client)));
                        } catch (IOException e) {
                            Logger.e(e);
                        } catch (GoogleAuthException e) {
                            Logger.e(e);
                        }
                    }
                });
            }
        }else
            SOCIAL_MANAGER.disconnect();


    }
}