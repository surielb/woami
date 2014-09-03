package com.gaya.whoami.players;

import android.content.*;
import com.gaya.whoami.*;
import com.gaya.whoami.JSONHelpers.*;
import com.gaya.whoami.events.IEventManager.*;
import org.json.*;

import java.io.*;
import java.util.*;

/**
 * Created with IntelliJ IDEA. User: Suriel Date: 9/3/14 Time: 10:09 PM To change this template use File | Settings |
 * File Templates.
 */
public class DummyPlayerManager implements PlayerManager {
    final Random random = new Random(System.currentTimeMillis());
    final List<Player> players;
    final List<Player> nearby = new LinkedList<Player>();
    private volatile boolean scanning;

    public DummyPlayerManager(Context context) throws IOException, JSONException {

        //load file from assets
        InputStream inputStream = context.getAssets().open("players.json");
        //load questions from json
        players = JSONHelpers.map(JSONHelpers.getJson(inputStream).optJSONArray("players"), new Factory<Object, Player>() {
            @Override
            public Player build(Object item) {
                if (item instanceof JSONObject) {
                    JSONObject data = (JSONObject) item;
                    final String name = data.optString("name");
                    final String id = data.optString("id");
                    final String imageUrl = String.format("https://graph.facebook.com/{%s}/picture?type=normal", id);
                    return new Player() {
                        @Override
                        public String getId() {
                            return id;
                        }

                        @Override
                        public String getName() {
                            return name;
                        }

                        @Override
                        public String getImageUrl() {
                            return imageUrl;
                        }
                    };
                }
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }
        });

        inputStream.close();

    }

    @Override
    public void invite(Player player) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void disconnect(Player player) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void scan() {
        scanning = true;
        schedule(3);
    }

    void schedule(int max) {
        if (!scanning) return;
        int add = random.nextInt(max);
        for (int i = 0; i < add; i++)
            SafeHandler.postDelayed(addPlayer, random.nextInt(max*10) * 1000);
        int remove = random.nextInt(max);
        for (int i = 0; i < remove; i++)
            SafeHandler.postDelayed(removePlayer, random.nextInt(max*10) * 1000);
    }

    final Runnable addPlayer = new Runnable() {
        @Override
        public void run() {
            schedule(2);
            if (players.size() == 0) return;
            final Player player = players.remove(random.nextInt(players.size()));
            nearby.add(player);
            Globals.eventManager().fireEvent(PlayerPresenceListener.class, new Executor<PlayerPresenceListener>() {
                @Override
                public void Execute(PlayerPresenceListener event) {
                    event.onPlayerSeen(player);
                }
            });
        }
    };
    final Runnable removePlayer = new Runnable() {
        @Override
        public void run() {
            schedule(2);
            if (nearby.size() == 0) return;
            final Player player = nearby.remove(random.nextInt(nearby.size()));
            players.add(player);
            Globals.eventManager().fireEvent(PlayerPresenceListener.class, new Executor<PlayerPresenceListener>() {
                @Override
                public void Execute(PlayerPresenceListener event) {
                    event.onPlayerGone(player);
                }
            });
        }
    };

    @Override
    public void stopScan() {
        scanning = false;
        SafeHandler.removeCallbacks(removePlayer);
        SafeHandler.removeCallbacks(addPlayer);

    }
}
