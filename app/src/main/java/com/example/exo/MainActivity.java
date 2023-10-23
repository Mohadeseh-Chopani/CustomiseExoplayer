package com.example.exo;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.SeekBar;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.util.MimeTypes;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackException;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.BuildConfig;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.google.android.exoplayer2.ui.SubtitleView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DialogSpeed.ListenerSpeed, DialogQuality.ListenerQuality {

    ExoPlayer player;
    ImageView btnPlay;
    StyledPlayerView playerView;
    SeekBar seekBar;
    ImageView img_volume;

    VolumeChangeReceiver volumeChangeReceiver;
    AudioManager audioManager;
    ImageView settings;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playerView = findViewById(R.id.playview);
        seekBar = findViewById(R.id.mySeekBar);
        img_volume = findViewById(R.id.img_volume);

        player = new ExoPlayer.Builder(this).build();

        MediaItem mediaItem = new MediaItem.Builder()
                .setUri(" https://vod2.afarinak.com/api/video/5f3aa557-e4ca-4d46-9844-b1059b59b3c6/stream/afarinak/eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1dWlkIjoiNWYzYWE1NTctZTRjYS00ZDQ2LTk4NDQtYjEwNTliNTliM2M2In0.N-pMsQ1RvBpd0sr4J029BYCqKi7TixT9CrG8Mv-Ai0A/master.mpd")
                .setMediaId(MimeTypes.APPLICATION_MPD)
                .build();


        playerView.setPlayer(player);
        player.setMediaItem(mediaItem);
        player.prepare();
        player.setPlayWhenReady(true);

        btnPlay = playerView.findViewById(R.id.exo_play_pause);
        settings = playerView.findViewById(R.id.settings);

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settings(v);
            }
        });


        player.addListener(new Player.Listener() {
            @Override
            public void onPlayerError(PlaybackException error) {
                Player.Listener.super.onPlayerError(error);

                if (BuildConfig.DEBUG) {
                    Log.i(TAG, "onPlayerError: " + error.getMessage());
                }
            }
        });

        player.addListener(new Player.Listener() {
            @Override
            public void onPlaybackStateChanged(int playbackState) {
                Player.Listener.super.onPlaybackStateChanged(playbackState);
                if (playbackState == Player.STATE_READY)
                    Log.e(TAG, "onPlaybackStateChanged: playing ");
                else if (playbackState == Player.STATE_IDLE) {
                    Log.e(TAG, "onPlaybackStateChanged: it has not start");
                }
            }
        });


        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        volumeChangeReceiver = new VolumeChangeReceiver(seekBar, audioManager);
        IntentFilter intentFilter = new IntentFilter("android.media.VOLUME_CHANGED_ACTION");
        registerReceiver(volumeChangeReceiver, intentFilter);


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                float volume = progress / 100f;
                setVolume(volume);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    float currentVolume;

    @SuppressLint("ResourceAsColor")
    void setVolume(float volume) {
        img_volume.setBackgroundColor(R.color.volume);
        player.setVolume(volume);
        if (volume == 0.0f) {
            img_volume.setImageResource(R.drawable.outline_volume_off_24);
        } else if (volume == 0.1f) {
            img_volume.setImageResource(R.drawable.outline_volume_up_24);
        } else {
            img_volume.setImageResource(R.drawable.baseline_volume_down_24);
        }


        img_volume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (volume != 0.0f) {
                    img_volume.setImageResource(R.drawable.outline_volume_off_24);
                    currentVolume = player.getVolume();
                    player.setVolume(0.0f);
                    seekBar.setProgress(0);
                } else {
                    img_volume.setImageResource(R.drawable.baseline_volume_down_24);
                    player.setVolume(currentVolume);
                    seekBar.setProgress((int) (currentVolume * 100));
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.setPlayWhenReady(false);
        player.release();
        player = null;
    }

    void settings(View view) {
        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
        popupMenu.getMenuInflater().inflate(R.menu.menu, popupMenu.getMenu());
        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if (item.getItemId() == R.id.speed) {

                    DialogSpeed dialogSpeed = new DialogSpeed();
                    dialogSpeed.show(getSupportFragmentManager(), null);
                } else if (item.getItemId() == R.id.quality) {
                    DialogQuality dialogQuality = new DialogQuality();
                    dialogQuality.show(getFragmentManager(), null);
                }
                return false;
            }
        });
    }

    @Override
    public void itemClick(Float speed) {

        Float pitch = 1.0f;
        PlaybackParameters playbackParameters = new PlaybackParameters(speed, pitch);
        player.setPlaybackParameters(playbackParameters);
    }

    @Override
    public void itemClickToChangeQuality(String selectedQuality) {
        DefaultTrackSelector trackSelector = (DefaultTrackSelector) player.getTrackSelector();
        DefaultTrackSelector.Parameters.Builder parametersBuilder = trackSelector.buildUponParameters();

        switch (selectedQuality) {
            case "auto":
                parametersBuilder.setMaxVideoSize(480, 360);
                break;
            case "360":
                parametersBuilder.setMaxVideoSize(680, 360);
                break;
            case "480":
                parametersBuilder.setMaxVideoSize(480, 360);
                break;
            case "720":
                parametersBuilder.setMaxVideoSize(1280, 720);
                break;
            case "1080":
                parametersBuilder.setMaxVideoSize(1920, 1080);
                break;
        }
        trackSelector.setParameters(parametersBuilder.build());
        player.setTrackSelectionParameters(parametersBuilder.build());
    }
}