package au.edu.jcu.guesstheceleb.game;

import android.graphics.Bitmap;

import org.jetbrains.annotations.TestOnly;

import java.util.Locale;

public class Game {
    private int questionNumber;
    private int score;
    private final Question[] questions;

    public Game(Question[] questions) {
        this.questions = questions;
    }

    public String getScore() {
        // Example result: "Score: 1/3"
        return String.format(Locale.getDefault(), "Score: %s/%s", score, count());
    }

    public boolean isGameOver() {
        return questionNumber + 1 == count();
    }

    public Question next() {
        if (isGameOver()) {
            return null;
        }
        questionNumber += 1;
        return questions[questionNumber];
    }

    public void updateScore(boolean correct) {
        if (correct) {
            score += 1;
        }
    }

    public int count() {
        return questions.length;
    }

    public Question getFirstQuestion() {
        return questions[0];
    }
}
