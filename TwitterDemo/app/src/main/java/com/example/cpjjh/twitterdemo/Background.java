package com.example.cpjjh.twitterdemo;

class Background {
    static void run(Runnable runnable) {
        Thread thread = new Thread(runnable);
        thread.start();
    }
}
