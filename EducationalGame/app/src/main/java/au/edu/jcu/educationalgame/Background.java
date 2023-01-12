package au.edu.jcu.educationalgame;

public class Background {
    static void run (Runnable runnable) {
        Thread thread = new Thread(runnable);
        thread.start();
    }
}
