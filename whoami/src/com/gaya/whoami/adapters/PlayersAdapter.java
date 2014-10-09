package com.gaya.whoami.adapters;

import android.content.*;
import android.view.*;
import android.widget.*;
import com.android.volley.toolbox.*;
import com.gaya.whoami.*;
import com.gaya.whoami.events.*;
import com.gaya.whoami.players.*;

import java.util.*;

/**
 * An adapter for displaying players nearby
 * you must call @see players.PlayerManager.scan() to start looking for players
 * use Globals.getPlayerManager().scan();
 */
public class PlayersAdapter extends BaseAdapter implements PlayerPresenceListener {
    final LayoutInflater layoutInflater;
    private final List<Player> players = new ArrayList<Player>();


    public PlayersAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
        //we register ourselves with the player presence global event
        //so we are notified when players are seen and are gone
        //see onPlayerSeen and onPlayerGone below
        Globals.eventManager().addEvent(PlayerPresenceListener.class,this, IEventManager.INFINITE);
    }

    @Override
    public int getCount() {
        if (players == null)
            return 0;
        return players.size();
    }

    @Override
    public Player getItem(int i) {
        if (players == null || i < 0 || i >= players.size())
            return null;
        return players.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        PlayerHolder holder;
        if (convertView != null)
            holder = (PlayerHolder) convertView.getTag();
        else
            holder = new PlayerHolder(layoutInflater.inflate(R.layout.player, viewGroup, false));
        Player answer = getItem(position);

        holder.setItem(answer);
        return holder.view;
    }

    @Override
    public void onPlayerSeen(Player player) {
        //we receive this event if a player has become visible to us
        //so we add him to the list and notify the data set has changed
        Logger.d("Player %s was added",player.getName());
        players.add(player);
        validateDataSet();
    }


    @Override
    public void onPlayerGone(Player player) {
        //we receive this event if a player has gone
        //so we remove him from the list and notify the data set has changed
        Logger.d("Player %s was removed",player.getName());

        players.remove(player);
        validateDataSet();
    }

    private void validateDataSet() {
        if (players.size() == 0)
            notifyDataSetInvalidated();
        else
            notifyDataSetChanged();
    }

    static class PlayerHolder {
        final View view;
        final TextView textView;
        final NetworkImageView networkImageView;

        PlayerHolder(View view) {
            view.setTag(this);

            this.view = view;
            textView = (TextView) view.findViewById(R.id.text);
            networkImageView = (NetworkImageView) view.findViewById(R.id.thumb);
        }

        PlayerHolder setItem(Player player) {
            textView.setText(player.getName());
            networkImageView.setImageUrl(player.getImageUrl(), ImageLoaderHelpers.getImageLoader());
            return this;
        }
    }
}