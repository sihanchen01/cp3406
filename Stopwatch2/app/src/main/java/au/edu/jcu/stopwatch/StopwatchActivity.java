package au.edu.jcu.stopwatch;


import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;

public class StopwatchActivity extends AppCompatActivity {
    private Stopwatch stopwatch;
    private Handler handler;
    private boolean isRunning;
    private TextView tvTime;
    private Button bToggle;
    private Button bSettings;
    private int speed;

    ActivityResultLauncher<Intent> settingsIntent = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    speed = result.getData().getIntExtra("speed", speed);
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvTime = findViewById(R.id.tvTime);
        bToggle = findViewById(R.id.bToggle);
        bSettings = findViewById(R.id.bSettings);

        isRunning = false;
        speed = 1000;
        if (savedInstanceState == null) {
            stopwatch = new Stopwatch();
        } else {
            stopwatch = new Stopwatch(savedInstanceState.getString("value"));
            boolean running = savedInstanceState.getBoolean("running");
            speed = savedInstanceState.getInt("speed");
            if (running) {
                enableStopwatch();
            }
        }

        bToggle.setOnClickListener(view -> toggleClicked());

        bSettings.setOnClickListener(view -> settingsClicked());
    }

    /*
    * onSaveInstanceState is called when android device rotate
     */
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("value", stopwatch.toString());
        outState.putBoolean("running", isRunning);
        outState.putInt("speed", speed);
    }

    private void toggleClicked() {
        if (isRunning) {
            disableStopwatch();
        } else {
            enableStopwatch();
        }
    }

    private void settingsClicked() {
        Intent intent = new Intent(StopwatchActivity.this, SettingsActivity.class);
        settingsIntent.launch(intent);
    }

    private void enableStopwatch() {
        isRunning = true;
        bToggle.setText(R.string.pause);
        handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (isRunning) {
                    stopwatch.tick();
                    tvTime.setText(stopwatch.toString());
                    handler.postDelayed(this, speed);
                }
            }
        });
    }

    private void disableStopwatch() {
        isRunning = false;
        bToggle.setText(R.string.start);
    }
}