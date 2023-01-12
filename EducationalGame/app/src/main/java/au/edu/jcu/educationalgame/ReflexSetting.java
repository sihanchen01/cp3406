package au.edu.jcu.educationalgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class ReflexSetting extends AppCompatActivity {
    Button bStart;
    SeekBar sbDifficulty;
    TextView tvDifficulty;

    int difficulty;
    String difficultyLevelInfo;
    final int GAME_MODE_EASY = 1;
    final int GAME_MODE_MEDIUM = 2;
    final int GAME_MODE_HARD = 3;

    UserModel currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reflex_welcome);

        // Retrieve current user from intent
        Intent intent = getIntent();
        currentUser = intent.getParcelableExtra("currentUser");

        bStart = findViewById(R.id.bReflexStart);
        tvDifficulty = findViewById(R.id.tvReflexDifficulty);
        sbDifficulty = findViewById(R.id.sbDifficulty);
        sbDifficulty.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                // Record user selected difficulty level to local variable.
                difficulty = i;
                switch (difficulty){
                    case GAME_MODE_EASY:
                        difficultyLevelInfo = getString(R.string.difficulty_level_easy);
                        break;
                    case GAME_MODE_MEDIUM:
                        difficultyLevelInfo = getString(R.string.difficulty_level_medium);
                        break;
                    case GAME_MODE_HARD:
                        difficultyLevelInfo = getString(R.string.difficulty_level_hard);
                        break;
                }
                tvDifficulty.setText(difficultyLevelInfo);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        // Start Reflex Game
        bStart.setOnClickListener(v -> {
            Intent reflexGameStart = new Intent(ReflexSetting.this, ReflexGame.class);
            reflexGameStart.putExtra("difficulty", difficulty);
            reflexGameStart.putExtra("currentUser", currentUser);
            startActivity(reflexGameStart);
        });
    }
}