package au.edu.jcu.stopwatch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class SettingsActivity extends AppCompatActivity {
//    public static final int SETTINGS_REQUEST = 123;

    private EditText etSpeedValue;
    private SeekBar sbSpeedValue;
    private Button bDone;
    private LinearLayout root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        etSpeedValue = findViewById(R.id.etSpeedValue);
        sbSpeedValue = findViewById(R.id.sbSpeedValue);
        bDone = findViewById(R.id.bDone);
        root = findViewById(R.id.root);

        bDone.setOnClickListener(view -> {
            doneClicked();
        });

        sbSpeedValue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                etSpeedValue.setText(String.valueOf(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Snackbar.make(root, "Using progress bar to set speed", Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Snackbar.make(root, "Speed set!", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    public void doneClicked () {
        String userInput = etSpeedValue.getText().toString();
        Intent intent = new Intent();
        if (!userInput.isEmpty()){
            int speed = Integer.parseInt(userInput);
            intent.putExtra("speed", speed);
        }
        setResult(RESULT_OK, intent);
        finish();

        super.onBackPressed();
    }
}