package com.gaya.whoami.players;

import com.gaya.whoami.events.*;

/**
 * Listener for presence of nearby players
 */
public interface PlayerPresenceListener extends IEvent {
    /**
     * Called when a player is in the area
     * @param player
     */
    void onPlayerSeen(Player player);

    /**
     * called when a player is no longer visible
     * @param player
     */
    void onPlayerGone(Player player);
}
