package au.edu.jcu.foleyapp;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioDescriptor;
import android.media.SoundPool;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class AudioManager {
    private SoundPool soundPool;
    private int sampleId;
    private boolean loadedOkay;

    public AudioManager() {
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        soundPool = new SoundPool.Builder()
                .setAudioAttributes(audioAttributes)
                .setMaxStreams(4)
                .build();
//        soundPool = new SoundPool(4, android.media.AudioManager.STREAM_MUSIC, 0);
        soundPool.setOnLoadCompleteListener((soundPool, sampleId, status) -> {
            if(loadedOkay){
                AudioManager.this.sampleId = sampleId;
            }
            loadedOkay = (status == 0);
        });
    }

    public void loadSoundFile(Context context, int resId){
        soundPool.load(context, resId, 1);
        Log.i("playing", "sound file loaded " + resId);
    }

    void playSound(){
        soundPool.play(sampleId, 1.f, 1.f, 0, 0, 1.f);
        Log.i("playing", sampleId + " is playing");
    }

    void resume() {
        soundPool.autoResume();
    }

    void pause() {
        soundPool.autoPause();
    }

    void destroy(){
        soundPool.release();
        soundPool = null;
    }
}
