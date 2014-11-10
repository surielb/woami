package com.gaya.whoami.social;

import java.util.Collection;

/**
 * @author suriel
 *         Date: 9/2/14
 *         Time: 11:37 AM
 */
public interface SocialProfile extends SocialIdentity {
    /**
     * returns a list of all the social identities the profile has
     * @return
     */
    Collection<SocialIdentity> getIdentities();

    /**
     * returns true if the profile has a valid identity
     * @return
     */
    boolean isValid();

    /**
     * returns the Profile id for the current interactive user
     * @return
     */
    String getProfileId();


}
