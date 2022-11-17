package au.edu.jcu.cp3406.simpleintentdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class SecondActivity extends AppCompatActivity {

    private static Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(this, "SecondActivity: on create called", Toast.LENGTH_SHORT).show();

        setContentView(R.layout.activity_second);

        // get access to the intent send to this activity
        Intent intent = getIntent();

        // data inside an intent are stored as key/value pairs!
        int amount = intent.getIntExtra("amount", -1);

        TextView textView = findViewById(R.id.textView);

        textView.setText("amount: " + amount);
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "SecondActivity: on back pressed called", Toast.LENGTH_SHORT).show();

        int number = random.nextInt(100); // 0 - 99

        // create the result intent to be send back to MainActivity!
        Intent data = new Intent();
        data.putExtra("number", number);
        setResult(Activity.RESULT_OK, data);
        finish();
        super.onBackPressed();
    }
}