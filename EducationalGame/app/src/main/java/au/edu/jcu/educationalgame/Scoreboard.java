package au.edu.jcu.educationalgame;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.TwitterV2;
import twitter4j.TwitterV2ExKt;

public class Scoreboard extends AppCompatActivity {
    TableLayout tlScoreboard;
    UserModel currentUser;

    // Message to share/tweet
    String shareMessage;

    // Create twitter4j instance
    Twitter twitter = TwitterFactory.getSingleton();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);

        tlScoreboard = findViewById(R.id.tlScoreboard);
        Intent intent = getIntent();
        currentUser = intent.getParcelableExtra("currentUser");

        DataBaseHelper dataBaseHelper = new DataBaseHelper(Scoreboard.this);
        List<UserModel> allUsers = dataBaseHelper.getAll();
        
        // Sort users by total score (reflex + math)
        Collections.sort(allUsers, (u1, u2) -> {
            int r1 = u1.getReflexScore();
            int r2 = u2.getReflexScore();

            int m1 = u1.getMathScore();
            int m2 = u2.getMathScore();
            return Integer.compare(r2 + m2, r1 + m1);
        });

        for (UserModel user : allUsers) {
            String username = user.getUserName();
            int reflexScore = user.getReflexScore();
            int mathScore = user.getMathScore();
            int totalScore = reflexScore + mathScore;

            TableRow tr = new TableRow(this);
            // Create username, reflex/math/total score TextViews, and add to scoreboard table.
            TextView usernameTextView = createTableRowTextView(username);
            tr.addView(usernameTextView);

            String reflexScoreString = String.valueOf(user.getReflexScore());
            TextView reflexScoreTextView = createTableRowTextView(reflexScoreString);
            tr.addView(reflexScoreTextView);

            String mathScoreString = String.valueOf(user.getMathScore());
            TextView mathScoreTextView = createTableRowTextView(mathScoreString);
            tr.addView(mathScoreTextView);


            TextView totalScoreTextView = createTableRowTextView(String.valueOf(totalScore));
            tr.addView(totalScoreTextView);

            tlScoreboard.addView(tr);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //Create message
        generateShareMessage();
        if (item.getItemId() == R.id.itTwitterButton){
            // Share message with twitter, only able to tweet score to my account (sihanchen01),
            // as I failed to implement user auth to allow user to use their own twitter account.
            // Combine twitter4j-v2 with twitter4j to utilize twitter api v2.
            // The demo code given us on LearnJCU is obsolete.
            final TwitterV2 twitterV2 = TwitterV2ExKt.getV2(twitter);
            // Track if tweet succeed, then make Toast prompt, as you can't do it inside Runnable
            AtomicBoolean tweetSucceed = new AtomicBoolean(true);
            Background.run(() -> {
                try{
                    twitterV2.createTweet(null, null, null, null, null,
                            null, null, null, null, null,
                            null, shareMessage);
                    Log.i("tweet", "tweet success");
                } catch(twitter4j.TwitterException e) {
                    e.printStackTrace();
                    tweetSucceed.set(false);
                    Log.i("tweet", "tweet fail");
                }
            });

            if (tweetSucceed.get()) {
                Toast.makeText(Scoreboard.this, "Score tweeted to 'sihanchen01'", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(Scoreboard.this, "Tweet failed!", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Share message with installed app
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT, "Check Out My Score!");
            intent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(intent, "Share Via"));
        }
        return super.onOptionsItemSelected(item);
    }

    public TextView createTableRowTextView (String s) {
        // Create table row for scoreboard table, each row is a user
        TextView tv = new TextView(Scoreboard.this);
        tv.setText(s);
        TableRow.LayoutParams params = new TableRow.LayoutParams(0,
                TableRow.LayoutParams.WRAP_CONTENT, 1f);
        params.setMargins(1, 1, 1, 1);
        tv.setLayoutParams(params);
        tv.setBackgroundColor(Color.WHITE);
        tv.setTextColor(Color.BLACK);
        tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        return tv;
    }

    public void generateShareMessage () {
        // Get current time, to avoid duplicate tweet content, as twitter api does not allow it
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            // need APIv26 for DateTimeFormatter to work
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            String currentTime = dtf.format(now);
            shareMessage = String.format(Locale.getDefault(),
                "%s's educational game score:\nReflex - %s; Math - %s.\n\n(generated at %s)",
                currentUser.getUserName(), currentUser.getReflexScore(), currentUser.getMathScore(),
                    currentTime);
        }
    }
}