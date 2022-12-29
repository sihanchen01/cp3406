package au.edu.jcu.foleyapp;

import android.content.Context;
import android.media.SoundPool;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class AudioManager {
    private final SoundPool soundPool;
    private int sampleId;
    private boolean loadedOkay;

    public AudioManager(Context context, int resId) {
        soundPool = new SoundPool(1, android.media.AudioManager.STREAM_MUSIC, 0);
        soundPool.setOnLoadCompleteListener((soundPool, sampleId, status) -> {
            loadedOkay = (status == 0);
            if(loadedOkay){
                AudioManager.this.sampleId = sampleId;
            }
        });
        soundPool.load(context, resId, 0);
        Log.i("playing", "constructed");
    }

    public boolean isLoadedOkay() {
        return loadedOkay;
    }

    void playSound(float speed, float volume){
        if(!loadedOkay) return;
        soundPool.play(sampleId, volume, volume, 1, 0, speed);
        Log.i("playing", "sound is playing");
    }

    void resume() {
        soundPool.autoResume();
    }

    void pause() {
        soundPool.autoPause();
    }
}
