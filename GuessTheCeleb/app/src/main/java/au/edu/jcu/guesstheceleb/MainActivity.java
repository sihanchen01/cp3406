package au.edu.jcu.guesstheceleb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;

import java.util.Locale;

import au.edu.jcu.guesstheceleb.game.CelebrityManager;
import au.edu.jcu.guesstheceleb.game.Difficulty;
import au.edu.jcu.guesstheceleb.game.Game;
import au.edu.jcu.guesstheceleb.game.GameBuilder;

public class MainActivity extends AppCompatActivity implements StateListener{
//    ImageView ivImage;
//    CelebrityManager imageManager;
    private GameFragment gameFragment;
    private StatusFragment statusFragment;
    private QuestionFragment questionFragment;
    private boolean isLargeScreen;

    private GameBuilder gameBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getSupportFragmentManager();
        gameFragment = (GameFragment) fragmentManager.findFragmentById(R.id.fragGame);
        statusFragment = (StatusFragment) fragmentManager.findFragmentById(R.id.fragStatus);
        questionFragment = (QuestionFragment) fragmentManager.findFragmentById(R.id.fragQuestion);
        Log.i("GettingFrags", "Game: " + gameFragment);
        Log.i("GettingFrags", "statue: " + statusFragment);
        Log.i("GettingFrags", "question: " + questionFragment);
        isLargeScreen = statusFragment != null;
        Log.i("GettingFrags", "isLarge: " + isLargeScreen);

        AssetManager assetManager = getAssets();
        CelebrityManager celebrityManager = new CelebrityManager(assetManager, "celebs");
        gameBuilder = new GameBuilder(celebrityManager);
//        ivImage = findViewById(R.id.ivImage);

//        AssetManager assetManager = getAssets();
//        try {
//            String[] names = assetManager.list("celebs");
//            System.out.println(Arrays.toString(names));
//
//            InputStream stream = assetManager.open("celebs/" + names[0]);
//            Bitmap bitmap = BitmapFactory.decodeStream(stream);
//
//            ivImage.setImageBitmap(bitmap);
//        } catch (IOException e) {
//            System.out.println("failed to get names");
//        }
//
//        imageManager = new ImageManager(this.getAssets(), "celebs");
//        ivImage.setImageBitmap(imageManager.get(0));

    }

    @Override
    public void onUpdate(State state) {
        Log.i("MainActivityState", "State: " + state);
        Difficulty level = gameFragment.getLevel();
        String text = String.format(Locale.getDefault(), "state: %s level: %s", state, level);
        Log.i("MainActivityState", text);

        if (isLargeScreen) {
            switch(state){
                case START_GAME:
                    Game game = gameBuilder.create(level);
                    questionFragment.setCurrentGame(game);
                    break;
                case CONTINUE_GAME:
                    statusFragment.setScore(questionFragment.getScore());
                    break;
                case GAME_OVER:
                    statusFragment.setScore(questionFragment.getScore());
                    statusFragment.setMessage("Game Over!");
                    break;
            }
        } else {

        }
    }
}