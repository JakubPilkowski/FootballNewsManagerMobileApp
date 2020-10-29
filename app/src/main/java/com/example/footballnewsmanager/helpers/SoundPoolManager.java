package com.example.footballnewsmanager.helpers;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;

import com.example.footballnewsmanager.R;

public class SoundPoolManager {


    private static SoundPoolManager INSTANCE;

    private static SoundPool soundPool;
    private static int acceptSound;
    private static int notificationSound;

    public SoundPoolManager(){
    }

    public static void init(Context context){
        INSTANCE = new SoundPoolManager();
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        soundPool = new SoundPool.Builder()
                .setAudioAttributes(audioAttributes)
                .build();
        acceptSound = soundPool.load(context, R.raw.accept_sound, 1);
        //notificationSong
    }

    public static SoundPoolManager get(){
        return INSTANCE;
    }

    public void playAcceptSong(){
        soundPool.play(acceptSound, 1.0f, 1.0f, 0, 0, 1.0f);
    }

    public void playNotifcationSong(){

    }

    public void dismiss(){
        soundPool.release();
        soundPool = null;
    }

}
