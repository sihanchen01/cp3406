package au.edu.jcu.guesstheceleb.game;

import android.graphics.Bitmap;
import android.os.Build;

import java.util.ArrayList;
import java.util.Random;
import java.util.stream.IntStream;


public class GameBuilder {
    CelebrityManager celebrityManager;

    public GameBuilder(CelebrityManager celebrityManager) {
        this.celebrityManager = celebrityManager;
    }

    public Game create(Difficulty difficulty) {
        int numberOfQuestions = difficulty.getNumberOfQuestions();
        // total number of pictures
        int upperLimit = CelebrityManager.count();
        ArrayList<Integer> randomQuestionNumbers = new ArrayList<Integer>();
        // All question share same possible names pool.
        String[] possibleNames = new String[numberOfQuestions];
        Question[] questions = new Question[numberOfQuestions];

        for (int i = 0; i < numberOfQuestions; i++) {
            Random rand = new Random();
            int randomInt = rand.nextInt(upperLimit);
            while(randomQuestionNumbers.contains(randomInt)){
                randomInt = (randomInt + 1) % CelebrityManager.count();
            }
            randomQuestionNumbers.add(randomInt);

            possibleNames[i] = celebrityManager.getName(randomInt);
            String celebName = celebrityManager.getName(randomInt);
            Bitmap celebBitmap = celebrityManager.get(randomInt);

            questions[i] = new Question(celebName, celebBitmap, possibleNames);
        }

        // Create Game
        return new Game(questions);
    }
}
