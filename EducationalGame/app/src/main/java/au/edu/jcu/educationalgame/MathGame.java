package au.edu.jcu.educationalgame;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.Locale;

public class MathGame extends AppCompatActivity {
    TextView tvInfo;
    TextView tvScore;
    TextView tvTime;
    TextView tvX;
    TextView tvY;
    TextView tvZ;
    TextView tvK;
    TextView tvMathOp1;
    TextView tvMathOp2;
    ConstraintLayout constraintLayout;

    final int ADD = 0;
    final int MINUS = 1;
    final int MULTIPLY = 2;
    final int DIVIDE = 3;
    Button bAdd;
    Button bMinus;
    Button bMultiply;
    Button bDivide;
    Button bReset;
    Button bSubmit;
    Button[] mathOpButtons;

    int x;
    int y;
    int z;
    int correctResult;
    int randomMathOperator1;
    int randomMathOperator2;

    // score for consecutive correct math equation solved
    int score;
    // Set initial userInput -999 as sentinel to prevent ZeroDivision error
    int userInput = -999;
    // Time for each round, 8000ms.
    int gameRoundTime = 8000;

    CountDownTimer countDownTimer;

    UserModel currentUser;
    DataBaseHelper dataBaseHelper;

    // shake to generate new math equation
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private double accelerationPreviousValue;
    private final SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            double accelerationCurrentValue = Math.sqrt((x * x + y * y + z * z));
            // Calculate difference between two movements
            double changeInAcceleration = Math.abs(accelerationCurrentValue - accelerationPreviousValue);

            // Change math equation upon noticeable shake
            if (changeInAcceleration > 2) {
                Toast.makeText(MathGame.this, "Equation Updated!", Toast.LENGTH_LONG).show();
                generateRandomEquation();
            }
            // Assign previous to current value
            accelerationPreviousValue = accelerationCurrentValue;
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_math_game);

        dataBaseHelper = new DataBaseHelper(MathGame.this);
        // Retrieve current user from intent
        Intent intent = getIntent();
        currentUser = intent.getParcelableExtra("currentUser");

        constraintLayout = findViewById(R.id.root);
        tvInfo = findViewById(R.id.tvMathInfo);
        tvScore = findViewById(R.id.tvMathScore);
        tvTime = findViewById(R.id.tvMathTime);
        tvX = findViewById(R.id.tvX);
        tvY = findViewById(R.id.tvY);
        tvZ = findViewById(R.id.tvZ);
        tvK = findViewById(R.id.tvK);
        tvMathOp1 = findViewById(R.id.tvMathOp1);
        tvMathOp2 = findViewById(R.id.tvMathOp2);
        bReset = findViewById(R.id.bReset);
        bSubmit = findViewById(R.id.bSubmit);
        bAdd = findViewById(R.id.bAdd);
        bMinus = findViewById(R.id.bMinus);
        bMultiply = findViewById(R.id.bMultiply);
        bDivide = findViewById(R.id.bDivide);
        mathOpButtons = new Button[]{bAdd, bDivide, bMultiply, bMinus};

        for (Button b : mathOpButtons) {
            b.setOnClickListener(v -> assignMathOperator(b));
        }

        bReset.setOnClickListener(v -> {
            tvMathOp1.setText("?");
            tvMathOp2.setText("?");
        });

        bSubmit.setOnClickListener(v -> {
            if (tvMathOp1.getText().equals("?") || tvMathOp2.getText().equals("?")){
                Toast.makeText(this, "Unassigned Math Operator!", Toast.LENGTH_LONG).show();
            }
            else {
                calculateUserInput();
                if (userInput != -999) {
                    checkUserInput();
                }
            }
        });

        // Initiate sensor
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // Hide info TextView till game over
        tvInfo.setVisibility(View.INVISIBLE);
        gameStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(sensorEventListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(sensorEventListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    // Let user assign Math operators to two question marks
    private void assignMathOperator(Button b) {
        if (tvMathOp1.getText().equals("?")){
            tvMathOp1.setText(b.getText());
        } else {
            if (tvMathOp2.getText().equals("?")){
                tvMathOp2.setText(b.getText());
            }
        }
    }

    private void gameStart() {
        // Reset background color to white.
        constraintLayout.postDelayed(() -> constraintLayout.setBackgroundColor(Color.WHITE), 500);
        // Start 8 seconds count down.
        generateRandomEquation();
        startCountDown();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void gameOver(String gameOverInfo) {
        // Disable accelerometer sensor when game is over
        sensorManager.unregisterListener(sensorEventListener);

        // Show user their highest score
        tvScore.setText(String.format("Score: %s\n(Your Highest Score: %s)",
                score, currentUser.getMathScore()));

        // Check if score is a new highest score, update DB if it is
        if (currentUser.getMathScore() < score) {
            boolean success = dataBaseHelper.updateScoreboard(currentUser.getUserName(), score, "math");
            String prompt;
            if (success){
                prompt = "Congrats! Scoreboard update succeed!";
            } else {
                prompt = "Scoreboard update failed!";
            }
            Toast.makeText(MathGame.this, prompt, Toast.LENGTH_LONG).show();
        }

        // Set background color to a custom red and show appropriate text when game's over.
        constraintLayout.setBackgroundColor(Color.parseColor("#be4d25"));
        float secondsTotal = (float) gameRoundTime /1000;
        tvTime.setText(String.format(Locale.getDefault(), "Time left: 0.00 / %.2fs", secondsTotal));
        tvInfo.setText(String.format(Locale.getDefault(), "Game Over: %s", gameOverInfo));
        tvInfo.setVisibility(View.VISIBLE);
        tvInfo.setTextColor(Color.WHITE);

        // Disable on touch listener
        constraintLayout.setOnTouchListener(null);

        // Show correct answer when game ended.
        tvMathOp1.setTextColor(Color.parseColor("#00DEEF"));
        tvMathOp2.setTextColor(Color.parseColor("#00DEEF"));
        switch (randomMathOperator1){
            case ADD:
                tvMathOp1.setText("+");
                break;
            case MINUS:
                tvMathOp1.setText("-");
                break;
            case MULTIPLY:
                tvMathOp1.setText("*");
                break;
            case DIVIDE:
                tvMathOp1.setText("/");
                break;
        }
        switch (randomMathOperator2){
            case ADD:
                tvMathOp2.setText("+");
                break;
            case MINUS:
                tvMathOp2.setText("-");
                break;
            case MULTIPLY:
                tvMathOp2.setText("*");
                break;
            case DIVIDE:
                tvMathOp2.setText("/");
                break;
        }

        // Disable all buttons when game's over.
        bReset.setEnabled(false);
        bSubmit.setEnabled(false);
        for (Button b : mathOpButtons) {
            b.setEnabled(false);
        }
    }

    private void generateRandomEquation() {
        // Generate random x, y, z values
        x = (int) (java.lang.Math.random()*9);
        y = (int) (java.lang.Math.random()*9);
        z = (int) (java.lang.Math.random()*9);
        tvX.setText(String.format(Locale.getDefault(), "(%s", x));
        tvY.setText(String.format(Locale.getDefault(), "%s)", y));
        tvZ.setText(String.valueOf(z));
        randomMathOperator1 = (int) (java.lang.Math.random()*3);
        randomMathOperator2 = (int) (java.lang.Math.random()*3);
        // Calculate result value.
        int temp = 0;
        switch (randomMathOperator1){
            case ADD:
                temp = x + y;
                break;
            case MINUS:
                temp = x - y;
                break;
            case MULTIPLY:
                temp = x * y;
                break;
            case DIVIDE:
                temp = x / y;
                break;
        }
        switch (randomMathOperator2){
            case ADD:
                correctResult = temp + z;
                break;
            case MINUS:
                correctResult = temp - z;
                break;
            case MULTIPLY:
                correctResult = temp * z;
                break;
            case DIVIDE:
                correctResult = temp / z;
                break;
        }
        tvK.setText(String.valueOf(correctResult));
    }

    private void startCountDown() {
        countDownTimer = new CountDownTimer(gameRoundTime, 25){
            @Override
            public void onTick(long l) {
                float secondsRemain = (float) l /1000;
                float secondsTotal = (float) gameRoundTime /1000;
                tvTime.setText(String.format(Locale.getDefault(), "Time left: %.2f / %.2fs",
                        secondsRemain, secondsTotal));
            }

            @Override
            public void onFinish() {
                gameOver("Time's Up ");
            }
        }.start();
    }

    private void calculateUserInput() {
        int temp=0;
        switch(tvMathOp1.getText().toString()){
            case "+":
                temp = x + y;
                break;
            case "-":
                temp = x - y;
                break;
            case "*":
                temp = x * y;
                break;
            case "/":
                try {
                    temp = x / y;
                } catch (ArithmeticException e) {
                    Toast.makeText(this, "Can't divide by Zero!", Toast.LENGTH_LONG).show();
                    bReset.performClick();
                    userInput = -999;
                    return;
                }
                break;
        }
        Log.i("mathCal", "temp: " + temp);
        switch(tvMathOp2.getText().toString()) {
            case "+":
                userInput = temp + z;
                break;
            case "-":
                userInput = temp - z;
                break;
            case "*":
                userInput = temp * z;
                break;
            case "/":
                try {
                    userInput = temp / z;
                } catch (ArithmeticException e) {
                    Toast.makeText(this, "Can't divide by Zero!", Toast.LENGTH_LONG).show();
                    bReset.performClick();
                    userInput = -999;
                    return;
                }
                break;
        }
        Log.i("mathCal", "userInput: " + userInput);
    }

    private void checkUserInput() {
        bReset.performClick();
        countDownTimer.cancel();
        if (userInput == correctResult) {
            // TODO: add sound effect
            constraintLayout.setBackgroundColor(Color.GREEN);
            score += 1;
            tvScore.setText(String.format("Score: %s", score));
            gameStart();
        } else {
            gameOver("Wrong Choice");
        }
   }
}
