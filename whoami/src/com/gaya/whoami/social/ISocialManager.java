package com.gaya.whoami.social;

import com.gaya.whoami.players.User;

/**
 * @author suriel
 *         Date: 9/2/14
 *         Time: 11:25 AM
 */
public interface ISocialManager {

    /**
     * adds a social identity to the profile
     * @param identity
     */
    void signIn(SocialIdentity identity);


    /**
     * retrieves the active profile
     * @return
     */
    User getActiveUser();
    void disconnect();


}
