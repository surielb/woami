package com.gaya.whoami.social;

import com.facebook.model.GraphUser;
import com.google.android.gms.plus.model.people.Person;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Gaya
 *         Date: 9/2/14
 *         Time: 11:30 AM
 */
public interface SocialIdentity {
    String getId();

    String getName();

    String getEmail();

    String getImageUrl();

    String getToken();

    int getType();

    String getUrl();

    public final static int TYPE_FACEBOOK = 1;
    public final static int TYPE_GPLUS = 2;

    public static class Factory {

        public static JSONObject toJson(SocialIdentity identity) throws JSONException {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("provider",identity.getType() == TYPE_FACEBOOK?"facebook":"google");
            jsonObject.put("social_id",identity.getId());
            jsonObject.put("name",identity.getName());
            jsonObject.put("picture",identity.getImageUrl());
            jsonObject.put("email",identity.getEmail());
            jsonObject.put("url",identity.getUrl());
            JSONObject auth = new JSONObject();
            auth.put("key",identity.getToken());
            auth.put("expires",System.currentTimeMillis() + 720000);
            jsonObject.put("auth",auth);
            return jsonObject;
        }


        public static SocialIdentity fromGraph(final String token, final GraphUser graphUser) {

            if (graphUser == null) return null;
            return new SocialIdentity() {
                @Override
                public String getId() {
                    return graphUser.getId();
                }

                @Override
                public String getName() {
                    return graphUser.getName();
                }

                @Override
                public String getEmail() {
                    Object email = graphUser.getProperty("email");
                    if (email != null) return email.toString();
                    return String.format("%s@facebook.com",getId());
                }

                @Override
                public String getImageUrl() {
                    return String.format("https://graph.facebook.com/%s/picture?type=large", graphUser.getId());
                }

                @Override
                public String getToken() {
                    return token;
                }

                @Override
                public int getType() {
                    return TYPE_FACEBOOK;
                }

                @Override
                public String getUrl() {
                    return graphUser.getLink();
                }
            };
        }

        public static SocialIdentity fromPlus(final String token, final Person person,final String email) {
            if (person == null) return null;
            return new SocialIdentity() {
                @Override
                public String getId() {
                    return person.getId();
                }

                @Override
                public String getName() {
                    return person.getDisplayName();
                }

                @Override
                public String getEmail() {
                    return email;
                }

                @Override
                public String getImageUrl() {
                    if (person.hasImage())
                        return person.getImage().getUrl().replace("sz=50", "");
                    return null;
                }

                @Override
                public String getToken() {
                    return token;
                }

                @Override
                public int getType() {
                    return TYPE_GPLUS;
                }

                @Override
                public String getUrl() {
                    return person.getUrl();
                }
            };
        }


    }

}