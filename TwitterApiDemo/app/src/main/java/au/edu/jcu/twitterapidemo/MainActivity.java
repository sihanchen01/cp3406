package au.edu.jcu.twitterapidemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterV2;
import twitter4j.TwitterV2ExKt;

public class MainActivity extends AppCompatActivity {
    TextView tvInfo;
    Button bTweet;
    Twitter twitter;

    final String consumerKey = "rZcEEE0qfhTqovs3l8kip6ICf";
    final String consumerSecret = "aASy1qoAbP3wBEXQGJrf9dvTg2XjfytxIV9vacY8sra9GL1hbg";
    String accessToken = "3367006792-vQ2eFZhyj9ZkMUokvZpbqGozE1s8QuEim9d3wpx";
    String accessTokenSecret="Vdua1htS9rPnbZ8zPNhwfsCsGV9gjT3Op4ErWdq7YyL3R";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bTweet = findViewById(R.id.bTweet);
        tvInfo = findViewById(R.id.tvInfo);

        twitter = Twitter.newBuilder()
                .prettyDebugEnabled(true)
                .oAuthConsumer(consumerKey, consumerSecret)
                .oAuthAccessToken(accessToken, accessTokenSecret)
                .build();
        final TwitterV2 v2 = TwitterV2ExKt.getV2(twitter);



        bTweet.setOnClickListener(v -> {
            Background.run(() -> {
                try {
                    String hi = "test123";
                    v2.createTweet(null, null, null, null, null,
                            null, null, null, null, null,
                            null, hi);
                } catch (TwitterException e) {
                    e.printStackTrace();
                }
            });

//            Intent intent = new Intent(MainActivity.this, Authenticate.class);
//            startActivity(intent);

//            try {
//
//            } catch (TwitterException e) {
//                e.printStackTrace();
//            }
        });
    }
}