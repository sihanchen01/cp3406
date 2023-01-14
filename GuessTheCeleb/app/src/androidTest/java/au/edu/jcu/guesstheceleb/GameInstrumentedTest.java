package au.edu.jcu.guesstheceleb;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import au.edu.jcu.guesstheceleb.game.CelebrityManager;
import au.edu.jcu.guesstheceleb.game.Difficulty;
import au.edu.jcu.guesstheceleb.game.Game;
import au.edu.jcu.guesstheceleb.game.GameBuilder;
import au.edu.jcu.guesstheceleb.game.Question;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class GameInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("au.edu.jcu.guesstheceleb", appContext.getPackageName());
    }

    @Test
    public void testGameBuilder() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        AssetManager assetManager = context.getAssets();
        CelebrityManager celebrityManager = new CelebrityManager(assetManager, "celebs");

        GameBuilder gameBuilder = new GameBuilder(celebrityManager);
        Game game = gameBuilder.create(Difficulty.EASY);

        int correctlyAnswered = 0;
        loop: while (!game.isGameOver()) {
            Question question = game.next();
            for (int i = 0; i < celebrityManager.count(); i++) {
                Log.i("GameInstrumentedTest", celebrityManager.getName(i));
                if (question.check(celebrityManager.getName(i))){
                    correctlyAnswered ++;
                    continue loop;
                }
            }
            fail("didn't answer question correctly");
        }
        assertEquals(game.count(), correctlyAnswered);
    }
}