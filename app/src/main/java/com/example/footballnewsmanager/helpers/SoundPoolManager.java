package com.example.footballnewsmanager.helpers;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.util.Log;

import com.example.footballnewsmanager.R;

public class SoundPoolManager {


    private static SoundPoolManager INSTANCE;

    private static SoundPool soundPool;
    private Context context;
    public SoundPoolManager(Context context){
        this.context = context;
    }

    public static void init(Context context){
        INSTANCE = new SoundPoolManager(context);
    }

    public static SoundPoolManager get(){
        return INSTANCE;
    }

    public void playSong(int resource, float volume){
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();
        soundPool = new SoundPool.Builder()
                .setAudioAttributes(audioAttributes)
                .build();
        int sound = soundPool.load(context, resource, 1);


        soundPool.setOnLoadCompleteListener((soundPool, sampleId, status) -> soundPool.play(sound, volume, volume, 0,0,1f));
    }

    public void dismiss(){
        if(soundPool!=null)
            soundPool.release();
        soundPool = null;
    }

}
