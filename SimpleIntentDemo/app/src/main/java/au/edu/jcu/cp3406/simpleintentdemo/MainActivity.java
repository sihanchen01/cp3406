package au.edu.jcu.cp3406.simpleintentdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static Random random;

    private Button bSend;
    private EditText etText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(this, "MainActivity: on create called", Toast.LENGTH_SHORT).show();

        bSend = findViewById(R.id.bSend);
        etText = findViewById(R.id.etText);

        bSend.setOnClickListener(view -> {
            String textToSend = getTextToSend();
            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.setType("text/plain");
            sendIntent.putExtra(Intent.EXTRA_TEXT, textToSend);

            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);
        });

        random = new Random(0); // force the same sequence of "random numbers"
    }

    private String getTextToSend() {
        return etText.getText().toString();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "MainActivity: on destroy called", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(this, "MainActivity: on start called", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Toast.makeText(this, "MainActivity: on stop called", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(this, "MainActivity: on pause called", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(this, "MainActivity: on resume called", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Toast.makeText(this, "MainActivity: on low memory called (unlikely!)", Toast.LENGTH_SHORT).show();
    }

    public void handler(View view) {
        // create explicit intent with information to send to SecondActivity
        Intent intent = new Intent(this, SecondActivity.class);
        intent.putExtra("amount", random.nextInt(10));

        // tell android to start SecondActivity, but to expect a result back!
        startActivityForResult(intent, 1234);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1234) {
            if (resultCode == Activity.RESULT_OK && data != null) {

                int result = data.getIntExtra("number", 0);

                Button button = findViewById(R.id.button);
                button.setText("result: " + result);
            }
        }
    }
}