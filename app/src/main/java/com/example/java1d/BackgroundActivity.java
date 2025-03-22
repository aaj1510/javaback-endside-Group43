package com.example.java1d;

import android.content.Context;
import android.media.MediaSession2;
import android.media.MediaSession2Service;
import android.media.browse.MediaBrowser;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.upstream.RawResourceDataSource;

public class BackgroundActivity extends AppCompatActivity{
    private static int currentMusicResId = -1;
    private static SimpleExoPlayer player;
    @Override
    protected void onCreate(Bundle savedInstaceState){
        super.onCreate(savedInstaceState);
        hideSystemUI();
    }

    public void playMusic(int music){
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

    @Override
    protected void onDestroy(){
        super.onDestroy();
//        stopMusic();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    protected void hideSystemUI(){
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        );
        
    }


}
