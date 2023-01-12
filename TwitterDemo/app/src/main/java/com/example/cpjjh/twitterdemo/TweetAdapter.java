package com.example.cpjjh.twitterdemo;

import android.content.Context;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class TweetAdapter extends ArrayAdapter<Tweet> {
    TweetAdapter(Context context, List<Tweet> tweets) {
        super(context, 0, tweets);
    }

    @NonNull @Override
    public View getView(int position, View convertView,
                        @NonNull ViewGroup parent) {

        Tweet tweet = getItem(position);
        assert tweet != null;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.tweet, parent, false);
        }

        TextView author = convertView.findViewById(R.id.author);
        TextView message = convertView.findViewById(R.id.message);

        author.setText(tweet.author);
        message.setText(tweet.message);

        return convertView;
    }
}

