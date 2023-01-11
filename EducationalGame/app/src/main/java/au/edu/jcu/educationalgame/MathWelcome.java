package au.edu.jcu.educationalgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MathWelcome extends AppCompatActivity {
    Button bStart;

    UserModel currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_math_welcome);

        // Retrieve current user from intent
        Intent intent = getIntent();
        currentUser = intent.getParcelableExtra("currentUser");

        bStart = findViewById(R.id.bMathStart);
        bStart.setOnClickListener(v -> {
            Intent mathGameStart = new Intent(MathWelcome.this, MathGame.class);
            mathGameStart.putExtra("currentUser", currentUser);
            startActivity(mathGameStart);
        });
    }
}