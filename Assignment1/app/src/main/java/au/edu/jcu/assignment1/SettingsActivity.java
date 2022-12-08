package au.edu.jcu.assignment1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {
    TextView tvNewGoal;
    Button bChange;

    private String newGoal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        tvNewGoal = findViewById(R.id.tvNewGoal);
        bChange = findViewById(R.id.bChange);

        // Read saved instance state
        if (savedInstanceState != null){
            newGoal = savedInstanceState.getString("newGoal");
        }

        // Change monthly reading goal with valid user input
        bChange.setOnClickListener(view -> {
            Intent backToMain = new Intent();
            newGoal = tvNewGoal.getText().toString();
            try {
                int pages = Integer.parseInt(newGoal);
                // Pages must be greater than 0
                if (pages > 0) {
                    backToMain.putExtra("newGoal", pages);
                    setResult(RESULT_OK, backToMain);
                    finish();
                } else {
                    Toast.makeText(this, "Goal can not be less than 1 page!", Toast.LENGTH_SHORT).show();
                }
            } catch (NumberFormatException e) {
                // Prompt invalid message if user input can not be parse into Integer
                Toast.makeText(this, "Invalid input, try again!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Save instance state so app won't crash when rotate
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("newGoal", newGoal);
    }
}