package au.edu.jcu.guesstheceleb.game;

import static org.junit.Assert.*;

import android.content.res.AssetManager;
import android.graphics.Bitmap;

import org.junit.Before;
import org.junit.Test;

public class GameUnitTest {
    Question question;

    @Before
    public void setUp() throws Exception {
        // Create an empty bitmap for testing
        Bitmap.Config config = Bitmap.Config.ARGB_8888;

        this.question = new Question("Brad Pitt", null,
                new String[]{"Brad Pitt", "Matt Damon"});
    }

    @Test
    public void checkCorrect() {
        assertTrue(question.check("Brad Pitt"));
    }

    @Test
    public void checkWrongCase() {
        assertFalse(question.check("Brad pitt"));
    }

    @Test
    public void checkWrongName() {
        assertFalse(question.check("Matt Damon"));
    }

    @Test
    public void testGame() {
        Question[] questions = new Question[3];
        String[] answers = new String[]{"bob", "jane", "harry"};
        for (int i = 0; i < 3; i++) {
            questions[i] = new Question(answers[i], null, answers);
        }
        Game game = new Game(questions);

        while (!game.isGameOver()) {
            Question question = game.next();
            game.updateScore(question.check("bob"));
        }

        assertEquals("Score: 1/3", game.getScore());
    }
}