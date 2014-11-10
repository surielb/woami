package com.gaya.whoami.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.android.volley.toolbox.NetworkImageView;
import com.gaya.whoami.ImageLoaderHelpers;
import com.gaya.whoami.R;
import com.gaya.whoami.entities.ParseUser;
import com.gaya.whoami.players.Player;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import static com.gaya.whoami.Globals.maskText;

/**
 * An adapter for displaying players nearby
 */
public class PlayersAdapter extends ParseQueryAdapter<ParseUser> {
    final LayoutInflater layoutInflater;

    public PlayersAdapter(Context context) {
        super(context, new QueryFactory<ParseUser>() {
            @Override
            public ParseQuery<ParseUser> create() {
                ParseQuery<ParseUser> query = ParseQuery.getQuery(ParseUser.class);
                ParseUser activeUser = (ParseUser) ParseUser.getCurrentUser();
                query.whereNotEqualTo("objectId", activeUser.getId());
                ParseGeoPoint location = activeUser.getParseGeoPoint("location");
                if (location != null)
                    query.whereNear("location", location);
                else
                    query.orderByDescending("lastSeen");
                return query;
            }
        });
        this.setPaginationEnabled(true);
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View getItemView(ParseUser user, View convertView, ViewGroup viewGroup) {
        PlayerHolder holder;

        if (convertView != null)
            holder = (PlayerHolder) convertView.getTag();
        else
            holder = new PlayerHolder(layoutInflater.inflate(R.layout.player, viewGroup, false));
        Player player = (Player) user;

        holder.setItem(player);
        return holder.view;
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
            textView.setText(maskText(player.getName()));
            networkImageView.setImageUrl(player.getImageUrl(), ImageLoaderHelpers.getImageLoader());
            return this;
        }
    }
}