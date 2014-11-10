package com.gaya.whoami.social;

import com.gaya.whoami.events.IEvent;
import com.gaya.whoami.players.User;

/**
 * @author suriel
 *         Date: 9/2/14
 *         Time: 11:43 AM
 */
public interface SocialProfileChangedListener extends IEvent {
    void onSocialProfileChanged(User socialProfile);
}
