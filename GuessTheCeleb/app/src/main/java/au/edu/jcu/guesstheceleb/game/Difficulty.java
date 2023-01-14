package au.edu.jcu.guesstheceleb.game;

public enum Difficulty {
    EASY(3), MEDIUM(6), HARD(12), EXPERT(24);

    private final int numberOfChoices;

    Difficulty(int number){
        this.numberOfChoices = number;
    }

    public int getNumberOfQuestions() {
        return numberOfChoices;
    }
}
