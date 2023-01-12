package au.edu.jcu.educationalgame;

import android.content.Intent;
import android.graphics.Color;
import android.media.VolumeShaper;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import twitter4j.Twitter;

public class Scoreboard extends AppCompatActivity {
    TableLayout tlScoreboard;
    UserModel currentUser;
    // Message to share/tweet
    String shareMessage;

    Twitter twitter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);

        tlScoreboard = findViewById(R.id.tlScoreboard);
//        Intent intent = getIntent();
//        currentUser = intent.getParcelableExtra("currentUser");
//        shareMessage = String.format(Locale.getDefault(),
//                "Checkout my (%s) educational game score:\nReflex - %s; Math - %s.",
//                currentUser.getUserName(), currentUser.getReflexScore(), currentUser.getMathScore());

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

            // Create twitter4j instance
            twitter = Twitter.newBuilder()
                .oAuthConsumer("rZcEEE0qfhTqovs3l8kip6ICf",
                        "aASy1qoAbP3wBEXQGJrf9dvTg2XjfytxIV9vacY8sra9GL1hbg")
                .oAuthAccessToken("3367006792-G7YcxZMUN3BkZWWQA76f4CGfFBVTgMppgCaJY3H",
                        "20QZIEdokvksxLVCisrgZCAfbgR8XkS7XCwcxdd9N5N4m")
                .build();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        boolean tweetSuccess = true;
        Background.run(() -> {
            try{
                twitter.v1().tweets().updateStatus("hello");
                Log.i("tweet", "tweet success");
            } catch(twitter4j.TwitterException e) {
                e.printStackTrace();
                Log.i("tweet", "tweet fail");
            }
        });

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Check Out My Score!");
        intent.putExtra(Intent.EXTRA_TEXT, shareMessage);
        startActivity(Intent.createChooser(intent, "Share Via"));
        return super.onOptionsItemSelected(item);
    }

    public TextView createTableRowTextView (String s) {
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
}