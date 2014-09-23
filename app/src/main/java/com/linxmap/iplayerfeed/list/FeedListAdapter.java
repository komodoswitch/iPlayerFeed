package com.linxmap.iplayerfeed.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.linxmap.iplayerfeed.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FeedListAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private ArrayList<FeedEntry> list;
    private Context context;

    private static final String LOG_TAG = "FeedListAdapter";
    private static final boolean LOG_ENABLED = true;

    public FeedListAdapter(Context context, ArrayList<FeedEntry> list) {
        layoutInflater = LayoutInflater.from(context);
        this.list = list;
        this.context = context;
    }

    public int getCount() {
        return list.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    class ViewHolder {
        TextView titleTextView;
        ImageView albumImageView;
        TextView ratingTextView;

    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {

            convertView = layoutInflater.inflate(R.layout.feed_list_item, null);
            holder = new ViewHolder();
            holder.titleTextView = (TextView) convertView.findViewById(R.id.title_text_view);
            holder.albumImageView = (ImageView) convertView.findViewById(R.id.album_image_view);
            holder.ratingTextView = (TextView) convertView.findViewById(R.id.rating_text_view);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (list != null && (!list.isEmpty())) {
            FeedEntry entry = (FeedEntry) list.get(position);
            holder.titleTextView.setText(entry.getTitle());
            String imageUrl = list.get(position).getImageUrl();
            if (imageUrl != null) {
                String trimImageUrl = imageUrl.trim();
                if (!trimImageUrl.equals("") && (!trimImageUrl.isEmpty()))
                    Picasso.with(context).load(trimImageUrl).into(holder.albumImageView);
            }
            int rating = list.get(position).getRating();
            if (rating > -1)
                holder.ratingTextView.setText("Rotten Tomatoes Score : " + list.get(position).getRating());
            else
                holder.ratingTextView.setText("Rotten Tomatoes Score : Unrated");
        }
        return convertView;
    }


}
