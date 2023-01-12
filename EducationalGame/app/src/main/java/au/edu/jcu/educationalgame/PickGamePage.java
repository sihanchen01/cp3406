package au.edu.jcu.educationalgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

public class PickGamePage extends AppCompatActivity {
    Button bReflex;
    Button bMath;
    Button bScoreboard;
    TextView tvWelcome;

    UserModel currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_game_page);

        // Retrieve current user from intent
        Intent intent = getIntent();
        currentUser = intent.getParcelableExtra("currentUser");

        tvWelcome = findViewById(R.id.tvHomeWelcome);
        tvWelcome.setText(String.format(Locale.getDefault(),
                "Hello %s,\nPick a Game and gets started!", currentUser.getUserName()));

        bReflex = findViewById(R.id.bReflex);
        bReflex.setOnClickListener(v -> {
            Intent reflexWelcomeIntent = new Intent(PickGamePage.this, ReflexSetting.class);
            reflexWelcomeIntent.putExtra("currentUser", currentUser);
            startActivity(reflexWelcomeIntent);
        });

        bMath = findViewById(R.id.bMath);
        bMath.setOnClickListener(v -> {
            Intent mathIntent = new Intent(PickGamePage.this, MathSetting.class);
            mathIntent.putExtra("currentUser", currentUser);
            startActivity(mathIntent);
        });

        bScoreboard = findViewById(R.id.bScoreboard);
        bScoreboard.setOnClickListener(v -> {
            Intent scoreboardIntent = new Intent(PickGamePage.this, Scoreboard.class);
            scoreboardIntent.putExtra("currentUser", currentUser);
            startActivity(scoreboardIntent);
        });
    }
}