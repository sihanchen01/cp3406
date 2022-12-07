package au.edu.jcu.uploadtofiredb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {
    TextView tvNewGoal;
    Button bChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        tvNewGoal = findViewById(R.id.tvNewGoal);
        bChange = findViewById(R.id.bChange);


        // Change monthly reading goal with valid user input
        bChange.setOnClickListener(view -> {
            Intent backToMain = new Intent();
            String newGoal = tvNewGoal.getText().toString();
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
}