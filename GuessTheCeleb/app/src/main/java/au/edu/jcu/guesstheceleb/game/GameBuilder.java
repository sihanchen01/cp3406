package au.edu.jcu.guesstheceleb.game;

import android.graphics.Bitmap;

public class GameBuilder {
    CelebrityManager celebrityManager;

    public GameBuilder(CelebrityManager celebrityManager) {
        this.celebrityManager = celebrityManager;
    }

    public Game create(Difficulty difficulty) {
        int numberOfQuestions = difficulty.getNumberOfQuestions();

        // All question share same possible names pool.
        String[] possibleNames = new String[numberOfQuestions];
        for (int i = 0; i < numberOfQuestions; i++) {
            possibleNames[i] = celebrityManager.getName(i);
        }

        // Build questions array with fixed length according to difficulty level.
        Question[] questions = new Question[numberOfQuestions];
        for (int j = 0; j < numberOfQuestions; j++) {
            // Create new question
            String celebName = celebrityManager.getName(j);
            Bitmap celebBitmap = celebrityManager.get(j);
            questions[j] = new Question(celebName, celebBitmap, possibleNames);
        }

        // Create Game
        return new Game(questions);
    }
}
