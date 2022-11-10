package au.edu.jcu.guessinggame;

import java.util.Random;

public class Game {
    Random random = new Random();
    int answer = random.nextInt(11);

    public boolean check(int number) {
        return number == answer;
    }
}
