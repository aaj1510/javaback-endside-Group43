package com.example.java1d;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.upstream.RawResourceDataSource;

public class BackgroundService extends Service {
    private static User user;
    private static int currentMusicResId = -1;
    private static SimpleExoPlayer player;
    public BackgroundService() {
    }

    @Override
    public IBinder onBind(Intent intent) { //Required method
        throw new UnsupportedOperationException();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        //Gets called whenever an activity starts the Service using startService(intent)
        if (intent != null && "play_music".equals(intent.getAction())){ //play music when intent has action of play_music
            int music = intent.getIntExtra("musicId", -1);
            playBackgroundMusic(music);
            currentMusicResId = music;
        }

        if(intent != null && "stop_music".equals(intent.getAction())){ //stop music when intent has action of stop_music
            stopMusic();
        }

        if(intent != null && intent.hasExtra("user_key")){ //If intent is called with "user_key"
            user = intent.getParcelableExtra("user_key"); //Assign the user class data to user
            Log.d("Background Service", user.toString());
        } else {
            Log.d("Background Service", "No user data available");
        }
        return START_STICKY; //If service is destroyed, automatically recreate the service
    }

    public static User getUserInfo(){ //Method used by other classes to get user data
        return user;
    }

    public void playBackgroundMusic(int music){
        if (player != null && currentMusicResId == music && player.isPlaying()) {
            //Checks if player instance exists, if the current music Id is the same requested music and if player is running
            return; // Do nothing (Already playing the requested music)
        }

        // If not, stop current music, set to the new music and play
        stopMusic();
        player = new SimpleExoPlayer.Builder(this).build();
        Uri musicUri = RawResourceDataSource.buildRawResourceUri(music);
        MediaItem mediaItem = MediaItem.fromUri(musicUri);

        player.setMediaItem(mediaItem);
        player.setVolume(1f);
        player.setRepeatMode(Player.REPEAT_MODE_ALL);
        player.prepare();
        player.play();

    }

    public void stopMusic() { //Stop the music and restarts the Id
        if (player != null) {
            player.stop();
            player.release();
            player = null;
            currentMusicResId = -1;
        }
    }
}