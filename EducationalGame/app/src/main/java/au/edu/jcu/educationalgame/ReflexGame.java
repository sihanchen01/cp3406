package au.edu.jcu.educationalgame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class ReflexGame extends AppCompatActivity {
    TextView tvGameMode;
    TextView tvScore;
    TextView tvTime;
    TextView tvInstruction;
    ImageView ivReflexDirection;
    TextView[] allTextViews;
    ConstraintLayout constraintLayout;
    SwipeListener swipeListener;

    // integers for four swipe gestures
    final int SWIPE_UP = 0;
    final int SWIPE_DOWN = 1;
    final int SWIPE_LEFT = 2;
    final int SWIPE_RIGHT = 3;

    // score for consecutive correct swipe gesture
    int score;
    int totalTries;
    int randomDirection;
    int userGesture;

    // 1 - Easy, 2 - Medium, 3 - Hard; Default is Easy
    int difficulty_level;
    final int GAME_MODE_EASY = 1;
    final int GAME_MODE_MEDIUM = 2;
    final int GAME_MODE_HARD = 3;

    // The minimum time interval between each random direction generated.
    // 1500ms - Easy, 1000ms - Medium, 500ms - Hard.
    int minimum_interval;
    // The starting time interval between each random directions, initial value:
    // 4500ms - Easy, 3000ms - Medium, 1500ms - Hard;
    int interval;
    // TODO: remove "running" if can't find a good use
    boolean running = true;
    // Timer for each reflex test, game over when time ran out.
    CountDownTimer countDownTimer;
    // Sound effects
    MediaPlayer correctAnswerSound;
    MediaPlayer wrongAnswerSound;

    UserModel currentUser;
    DataBaseHelper dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reflex_game);

        dataBaseHelper = new DataBaseHelper(ReflexGame.this);
        // Retrieve current user from intent
        Intent intent = getIntent();
        currentUser = intent.getParcelableExtra("currentUser");

        constraintLayout = findViewById(R.id.root);
        ivReflexDirection = findViewById(R.id.ivReflexDirection);
        tvGameMode = findViewById(R.id.tvReflexGameMode);
        tvInstruction = findViewById(R.id.tvReflexInfo);
        tvScore = findViewById(R.id.tvReflexScore);
        tvTime = findViewById(R.id.tvReflexTime);
        // Use array to store all TextViews, change their text color when game is over
        allTextViews = new TextView[]{tvGameMode, tvInstruction, tvScore, tvTime};

        // Get user selected difficulty level, set game difficulty variable values accordingly.
        Intent reflexGameStart = getIntent();
        difficulty_level = reflexGameStart.getIntExtra("difficulty", GAME_MODE_EASY);
        switch (difficulty_level) {
            case GAME_MODE_EASY:
                tvGameMode.setText(R.string.game_mode_easy);
                break;
            case GAME_MODE_MEDIUM:
                tvGameMode.setText(R.string.game_mode_medium);
                break;
            case GAME_MODE_HARD:
                tvGameMode.setText(R.string.game_mode_hard);
                break;
        }
        minimum_interval = 500 * (4 - difficulty_level);
        interval = 3 * minimum_interval;

        // Initiate media player
        correctAnswerSound = MediaPlayer.create(this, R.raw.correct_answer);
        wrongAnswerSound = MediaPlayer.create(this, R.raw.wrong_answer);

        gameStart();
    }

    private void gameStart() {
        // When game starts, hide instruction text box, make reflex direction arrow visible.
        tvInstruction.setVisibility(View.INVISIBLE);
        ivReflexDirection.setVisibility(View.VISIBLE);
        generateRandomDirection();
        swipeListener = new SwipeListener(constraintLayout);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void gameOver(String gameOverInfo) {
        // Show user their highest score
        tvScore.setText(String.format("Score: %s\n(Your Highest Score: %s)",
                score, currentUser.getReflexScore()));

        // check if score is a new highest score, update DB if it is
        if (currentUser.getReflexScore() < (score * difficulty_level)) {
            boolean success = dataBaseHelper.updateScoreboard(currentUser.getUserName(),
                    score * difficulty_level, "reflex");
            String prompt;
            if (success){
                prompt = "Congrats! Scoreboard update succeed!";
            } else {
                prompt = "Scoreboard update failed!";
            }
            Toast.makeText(ReflexGame.this, prompt, Toast.LENGTH_LONG).show();
        }
        constraintLayout.setBackgroundColor(Color.RED);
        // TODO: remove "running" if can't find a good use
        running = false;
        // When game is over, hide reflex direction arrow, make instruction text visible.
        ivReflexDirection.setVisibility(View.INVISIBLE);
        tvInstruction.setVisibility(View.VISIBLE);
        tvInstruction.setText(String.format(Locale.getDefault(), "Game Over: %s", gameOverInfo));
        float secondsTotal = (float) interval/1000;
        tvTime.setText(String.format(Locale.getDefault(), "Time left: 0.00 / %.2fs", secondsTotal));
        // Change all text color to white
        for (TextView tv : allTextViews) {
            tv.setTextColor(Color.WHITE);
        }
        // Disable on touch listener
        constraintLayout.setOnTouchListener(null);
    }

    private void generateRandomDirection() {
        // Reset background color to white.
        constraintLayout.postDelayed(() -> constraintLayout.setBackgroundColor(Color.WHITE), 500);

        startCountDown();
        randomDirection = (int) (Math.random()*3);
        switch (randomDirection){
            case SWIPE_UP:
//                tvInstruction.setText(R.string.up);
                ivReflexDirection.setImageResource(R.drawable.ic_round_arrow_upward_24);
                break;
            case SWIPE_DOWN:
//                tvInstruction.setText(R.string.down);
                ivReflexDirection.setImageResource(R.drawable.ic_round_arrow_downward_24);
                break;
            case SWIPE_LEFT:
//                tvInstruction.setText(R.string.left);
                ivReflexDirection.setImageResource(R.drawable.ic_round_arrow_back_24);
                break;
            case SWIPE_RIGHT:
//                tvInstruction.setText(R.string.right);
                ivReflexDirection.setImageResource(R.drawable.ic_round_arrow_forward_24);
                break;
        }
    }

    private void startCountDown() {
        countDownTimer = new CountDownTimer(interval, 25){
            @Override
            public void onTick(long l) {
                float secondsRemain = (float) l /1000;
                float secondsTotal = (float) interval/1000;
                tvTime.setText(String.format(Locale.getDefault(), "Time left: %.2f / %.2fs",
                        secondsRemain, secondsTotal));
            }

            @Override
            public void onFinish() {
                gameOver("Time's Up ");
            }
        }.start();
    }

    /**
     * Swipe listener, listen to swipe up/down/left/right, and show message accordingly.
     */
    private class SwipeListener implements View.OnTouchListener {
        GestureDetector gestureDetector;

        SwipeListener(View view) {
            int threshold = 100;
            int velocity_threshold = 100;

            GestureDetector.SimpleOnGestureListener listener =
                new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onDown(MotionEvent e) {
                        return true;
                    }

                    @Override
                    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                        float xMove = e2.getX() - e1.getX();
                        float yMove = e2.getY() - e1.getY();
                        try {
                            if (Math.abs(xMove) > Math.abs(yMove)){
                                // Horizontal movement is more significant
                                if (Math.abs(xMove) > threshold && Math.abs(velocityX) > velocity_threshold) {
                                    if (xMove > 0) {
                                        // user swipe right
                                        userGesture = SWIPE_RIGHT;
                                    } else {
                                        // user swipe left
                                        userGesture = SWIPE_LEFT;
                                    }
                                    checkUserGesture();
                                    return true;
                                }
                            } else {
                                // Vertical movement is more significant
                                if (Math.abs(yMove) > threshold && Math.abs(velocityY) > velocity_threshold) {
                                    if (yMove > 0) {
                                        // user swipe up
                                        userGesture = SWIPE_DOWN;
                                    } else {
                                        // user swipe down
                                        userGesture = SWIPE_UP;
                                    }
                                    checkUserGesture();
                                    return true;
                                }
                            }
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                        return false;
                    }
                };
            gestureDetector = new GestureDetector(listener);
            view.setOnTouchListener(this);
        }


       private void checkUserGesture() {
            totalTries += 1;
            countDownTimer.cancel();
            if (userGesture == randomDirection) {
                constraintLayout.setBackgroundColor(Color.GREEN);
                correctAnswerSound.start();
                score += 1;
                // Interval shrinks each time user answers with a correct swipe gesture,
                // until it reaches minimum time interval (100ms).
                if (interval > minimum_interval) {
                    interval -= 500;
                }
                tvScore.setText(String.format("Score: %s", score ));
                generateRandomDirection();
            } else {
                wrongAnswerSound.start();
                gameOver("Wrong Input");
            }
       }


       @SuppressLint("ClickableViewAccessibility")
       @Override
       public boolean onTouch(View view, MotionEvent motionEvent) {
           return gestureDetector.onTouchEvent(motionEvent);
       }
    }
}