package au.edu.jcu.guesstheceleb.game;

import android.graphics.Bitmap;

public class Question {
    private final String celebrityName;
    private final Bitmap celebrityImage;
    private final String[] possibleNames;

    public Question(String celebrityName, Bitmap celebrityImage, String[] possibleNames) {
        this.celebrityName = celebrityName;
        this.celebrityImage = celebrityImage;
        this.possibleNames = possibleNames;
    }

    public boolean check(String guess) {
        return guess.equals(celebrityName);
    }

    public Bitmap getCelebrityImage() {
        return celebrityImage;
    }

    public String[] getPossibleNames() {
        return possibleNames;
    }
}
