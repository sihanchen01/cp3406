package au.edu.jcu.foleyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class FoleyActivity extends AppCompatActivity {
    // e.g, {human: [1,2], animal[2,3]}
    HashMap<String, ArrayList<Integer>> sounds = new HashMap<>();
    ArrayList<Integer> natureSounds = new ArrayList<>();
    ArrayList<Integer> animalSounds = new ArrayList<>();
    ArrayList<Integer> humanSounds = new ArrayList<>();
    ArrayList<Integer> technologySounds = new ArrayList<>();
    AudioManager audioManager;
    ImageView ivFoley;
    String buttonText;
    int deviceHeight;
    int deviceWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foley);

        ivFoley = findViewById(R.id.ivFoley);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        deviceHeight = displayMetrics.heightPixels;
        deviceWidth = displayMetrics.widthPixels;
        audioManager = new AudioManager();

        Collections.addAll(natureSounds, R.raw.nature_heavy_rain_drops,
                R.raw.nature_light_rain_loop, R.raw.nature_rain_and_thunder_storm,
                R.raw.nature_thunderstorm_background_sound);

        Collections.addAll(animalSounds, R.raw.animal_cartoon_animal_crying_in_pain,
                R.raw.animal_dog_barking_twice, R.raw.animal_pig_grunting,
                R.raw.animal_wild_lion_animal_roar);

        Collections.addAll(humanSounds, R.raw.human_exclamation_of_pain,
                R.raw.human_female_astonished_gasp, R.raw.human_small_group_cheer_and_applause,
                R.raw.human_very_sick_man_coughing);

        Collections.addAll(technologySounds, R.raw.tech_classic_alarm,
                R.raw.tech_clock_countdown_bleeps, R.raw.tech_technological_futuristic_hum,
                R.raw.tech_telephone_dial_tone);

        sounds.put("nature", natureSounds);
        sounds.put("animal", animalSounds);
        sounds.put("human", humanSounds);
        sounds.put("technology", technologySounds);

        Intent intent = getIntent();
        buttonText = intent.getStringExtra("buttonText");

        switch (buttonText) {
            case "nature":
                ivFoley.setImageResource(R.drawable.nature);
                break;
            case "animal":
                ivFoley.setImageResource(R.drawable.animal);
                break;
            case "human":
                ivFoley.setImageResource(R.drawable.human);
                break;
            case "technology":
                ivFoley.setImageResource(R.drawable.technology);
                break;
        }

    } // end of onCreate

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (x > deviceWidth/2.0 && y > deviceHeight/2.0){
                audioManager.loadSoundFile(this,sounds.get(buttonText).get(0));
            }
            else if (x > deviceWidth/2.0 && y < deviceHeight/2.0){
                audioManager.loadSoundFile(this,sounds.get(buttonText).get(1));
            }
            else if (x < deviceWidth/2.0 && y > deviceHeight/2.0){
                audioManager.loadSoundFile(this,sounds.get(buttonText).get(2));
            }
            else {
                audioManager.loadSoundFile(this,sounds.get(buttonText).get(3));
            }
        } else if (event.getAction() == MotionEvent.ACTION_UP){
            audioManager.playSound();
        }
        return true;
    }

    @Override
    protected void onResume(){
        super.onResume();
        if (audioManager != null) {
            audioManager.resume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (audioManager != null) {
            audioManager.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (audioManager != null){
            audioManager.destroy();
        }
    }
}