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
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        if (intent != null && "play_music".equals(intent.getAction())){
            int music = intent.getIntExtra("musicId", -1);
            playBackgroundMusic(music);
            currentMusicResId = music;
        }

        if(intent != null && intent.hasExtra("user_key")){
            user = intent.getParcelableExtra("user_key");
            Log.d("Background Service", user.toString());
        } else {
            Log.d("Background Service", "No user data available");
        }
        return START_STICKY;
    }

    public static User getUserInfo(){
        return user;
    }

    public void playBackgroundMusic(int music){
        if (player != null && currentMusicResId == music && player.isPlaying()) {
            return; // Already playing the requested music
        }

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

    public void stopMusic() {
        if (player != null) {
            player.stop();
            player.release();
            player = null;
            currentMusicResId = -1;
        }
    }
}