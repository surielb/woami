package com.gaya.whoami.players;

/**
 * Created with IntelliJ IDEA. User: Suriel Date: 9/3/14 Time: 9:50 PM To change this template use File | Settings |
 * File Templates.
 */
public interface PlayerManager {
    /**
     * send an invite to a player to join our game
     * @param player
     */
     void invite(Player player);

    /**
     * disconnect a player from our game
     * @param player
     */
     void disconnect(Player player);

    /**
     * start scanning for players in the area
     */
    void scan();

    /**
     * stop scanning for players
     */
    void stopScan();
}
