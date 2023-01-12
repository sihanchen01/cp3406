package com.example.cpjjh.twitterdemo;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;

public class MainActivity extends AppCompatActivity {
    private TextView userInfo;
    private TweetAdapter adapter;
    private Button authenticate;
    private Twitter twitter = TwitterFactory.getSingleton();
    private User user;
    private List<Tweet> tweets;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userInfo = findViewById(R.id.user_info);
        ListView tweetList = findViewById(R.id.tweets);
        authenticate = findViewById(R.id.authorise);

        tweets = new ArrayList<>();
        adapter = new TweetAdapter(this, tweets);
        tweetList.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Background.run(new Runnable() {
            @Override
            public void run() {
                final boolean status;
                final String text;
                if (isAuthorised()) {

                    try {
                        twitter.updateStatus("hello world!");
                    } catch (TwitterException ignored) {

                    }

                    text = user.getScreenName();
                    tweets.clear();
                    tweets.addAll(queryTwitter());
                    status = false;
                } else {
                    text = "unknown";
                    userInfo.setText(getString(R.string.unknown));
                    status = true;
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        userInfo.setText(text);
                        authenticate.setEnabled(status);
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    public void authorise(View view) {
        Intent intent = new Intent(this, Authenticate.class);
        startActivity(intent);
    }

    private boolean isAuthorised() {
        try {
            user = twitter.verifyCredentials();
            Log.i("MainActivity", "verified");
            return true;
        } catch (Exception e) {
            Log.i("MainActivity", "not verified");
            return false;
        }
    }

    private List<Tweet> queryTwitter() {
        List<Tweet> results = new ArrayList<>();

        Query query = new Query();
        query.setQuery("#quotes");

        try {
            QueryResult result = twitter.search(query);
            for (Status status : result.getTweets()) {
                String name = status.getUser().getScreenName();
                String message = status.getText();
                Tweet tweet = new Tweet(name, message);
                results.add(tweet);
            }
        } catch (final Exception e) {
            Log.e("MainActivity", "query error: " + e.getLocalizedMessage());
        }

        return results;
    }
}
