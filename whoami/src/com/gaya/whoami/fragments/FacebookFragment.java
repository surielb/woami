package com.gaya.whoami.fragments;

import android.content.*;
import android.os.*;
import android.support.v4.app.*;
import android.text.*;
import com.facebook.*;
import com.facebook.Session.*;
import com.facebook.model.*;

/**
 * Created with IntelliJ IDEA. User: Suriel Date: 7/30/14 Time: 10:55 PM To change this template use File | Settings |
 * File Templates.
 */
public abstract class FacebookFragment extends Fragment {
    private final StatusCallback callback = new StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onStatusChanged(session, state, exception);
        }
    };
    private Session userInfoSession; // the Session used to fetch the current user info
    private GraphUser user;
    private static final String NAME = "name";
    private static final String ID = "id";
    private static final String PICTURE = "picture";
    private static final String FIELDS = "fields";

    private static final String REQUEST_FIELDS = TextUtils.join(",", new String[]{ID, NAME, PICTURE});

    private UiLifecycleHelper uiHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uiHelper = new UiLifecycleHelper(getActivity(), callback);
        uiHelper.onCreate(savedInstanceState);
        fetchUserInfo();
    }

    @Override
    public void onResume() {
        super.onResume();
        uiHelper.onResume();
        updateUI();
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }

    private void onStatusChanged(Session session, SessionState state, Exception exception) {
        if (state.equals(SessionState.OPENED))
            fetchUserInfo();
        else if (state.isClosed()) {
            user = null;
            updateUI();
        }
    }


    Session getSession() {
        if (getActivity() == null) return null;
        return Session.getActiveSession();
    }

    private void fetchUserInfo() {
        final Session currentSession = getSession();
        if (currentSession != null && currentSession.isOpened()) {
            if (currentSession != userInfoSession) {
                Request request = Request.newMeRequest(currentSession, new Request.GraphUserCallback() {
                    @Override
                    public void onCompleted(GraphUser me, Response response) {
                        if (currentSession == getSession()) {
                            user = me;
                            updateUI();
                        }

                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString(FIELDS, REQUEST_FIELDS);
                request.setParameters(parameters);
                Request.executeBatchAsync(request);
                userInfoSession = currentSession;
            }
        } else {
            user = null;
        }
    }

    protected abstract void updateUI();

    public GraphUser getUser() {
        return user;
    }
}
