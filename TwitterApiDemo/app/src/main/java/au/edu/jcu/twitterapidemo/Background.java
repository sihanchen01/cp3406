package au.edu.jcu.twitterapidemo;

import android.net.IpSecManager;

public class Background {
    static void run (Runnable runnable) {
        Thread thread = new Thread(runnable);
        thread.start();
    }
}
