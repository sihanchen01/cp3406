package au.edu.jcu.reflexchecker;

import android.content.Intent;
import android.icu.text.SymbolTable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    Button bStart;
    TextView tvTimeUsed;
    long startTime;
    long usedTime;

    ActivityResultLauncher<Intent> startGame = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    usedTime = System.nanoTime() - startTime;
                    double seconds = (double) usedTime / 1_000_000_000;
                    String output = String.format(Locale.ENGLISH, "Time used: %.2f seconds", seconds);
                    tvTimeUsed.setText(output);
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bStart = findViewById(R.id.bStart);
        tvTimeUsed = findViewById(R.id.tvTimeUsed);

        bStart.setOnClickListener(view -> {
            Intent gameIntent = new Intent(MainActivity.this, GameActivity.class);
            startTime = System.nanoTime();
            startGame.launch(gameIntent);
        });

    }
}