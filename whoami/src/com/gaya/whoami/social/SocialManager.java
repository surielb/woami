package com.gaya.whoami.social;

import com.facebook.Session;
import com.gaya.whoami.Logger;
import com.gaya.whoami.SafeHandler;
import com.gaya.whoami.entities.ParseUser;
import com.gaya.whoami.events.IEventManager;
import com.gaya.whoami.ioc.IPreload;
import com.gaya.whoami.players.User;
import com.parse.*;
import org.json.JSONArray;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.gaya.whoami.ioc.ServiceLocator.getService;

/**
 * @author suriel
 *         Date: 9/2/14
 *         Time: 11:50 AM
 */
public class SocialManager implements ISocialManager, IPreload {
    private ParseUser profile;


    public SocialManager() {
        loadState();
    }


    @Override
    public void signIn(final SocialIdentity identity) {
        final String key = String.format("%s_%s", identity.getType(), identity.getId());
        if (profile != null && profile.getUsername().equals(key)) {
            //this is simple, we are ok- just update the info
            profile.setName(identity.getName());
            profile.setEmail(identity.getEmail());
            profile.setImageUrl(identity.getImageUrl());
            profile.setType(identity.getType());
            profile.setLink(identity.getUrl());
            Logger.d("is current user, updating");
            profile.saveEventually();
            profile.updateLocation();

        } else {
            //ok tricky
            com.parse.ParseUser.logInInBackground(key, key, new LogInCallback() {
                @Override
                public void done(com.parse.ParseUser parseUser, ParseException e) {
                    if (e != null && e.getCode() == ParseException.OBJECT_NOT_FOUND) {
                        Logger.d("No user, creating");
                        //we must sign in
                        final ParseUser user = new ParseUser();
                        user.setImageUrl(identity.getImageUrl());
                        user.setName(identity.getName());
                        user.setEmail(identity.getEmail());
                        user.setUsername(key);
                        user.setPassword(key);
                        user.setType(identity.getType());
                        user.setLink(identity.getUrl());
                        user.signUpInBackground(new SignUpCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    setProfile(user);
                                    user.updateLocation();
                                }
                            }
                        });
                    } else {
                        Logger.d("Signed in");
                        setProfile((ParseUser) parseUser);
                        ((ParseUser) parseUser).updateLocation();
                    }
                }
            });
        }

    }


    void loadState() {
        setProfile((ParseUser) ParseUser.getCurrentUser());
    }

    private void setProfile(final ParseUser user) {
        //todo save locally
        profile = user;
        fireChanged();
    }


    private static Set<String> getStrings(JSONArray jFollowed) {
        Set<String> followed = new HashSet<String>();
        if (jFollowed != null)
            for (int i = 0; i < jFollowed.length(); i++) {
                String id = jFollowed.optString(i, null);
                if (id != null)
                    followed.add(id);
            }
        return followed;
    }

    private void subscribeToChannel(final String id) {

        SafeHandler.bgPost(new Runnable() {
            @Override
            public void run() {
                String new_channel = null;
                Logger.d("Subscribing to channel %s", id);
                if (id != null)
                    new_channel = id;

                boolean found = false;
                List<String> channels = ParseInstallation.getCurrentInstallation().getList("channels");

                if (channels != null)
                    for (String channel : channels)
                        if (!channel.equals(new_channel))
                            ParsePush.unsubscribeInBackground(channel);
                        else
                            found = true;

                if (new_channel != null && !found) {
                    ParsePush.subscribeInBackground(new_channel);
                }
            }
        });

    }


    void fireChanged() {


        SafeHandler.post(new Runnable() {
            @Override
            public void run() {

                getService(IEventManager.class).fireEvent(SocialProfileChangedListener.class, new IEventManager.Executor<SocialProfileChangedListener>() {
                    @Override
                    public void Execute(SocialProfileChangedListener event) {
                        event.onSocialProfileChanged(profile);
                    }
                });
            }
        });


    }

    @Override
    public User getActiveUser() {
        return profile;
    }

    @Override
    public void disconnect() {
        Session session = Session.getActiveSession();
        if (session != null)
            session.closeAndClearTokenInformation();
        if (profile != null)
            profile.logOut();
        subscribeToChannel(null);
        setProfile(null);

    }

}

