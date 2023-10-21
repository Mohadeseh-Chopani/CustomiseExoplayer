package com.example.exo;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackException;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.BuildConfig;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.util.MimeTypes;

public class MainActivity extends AppCompatActivity implements DialogSpeed.ListenerSpeed, DialogQuality.ListenerQuality {

    ExoPlayer player;
    ImageView btn_play;
    StyledPlayerView playerView;

    ImageView settings;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playerView = findViewById(R.id.playview);

        player = new ExoPlayer.Builder(this).build();

        MediaItem mediaItem = new MediaItem.Builder()
                .setUri("https://vod2.afarinak.com/api/video/0520aff4-b322-4e8e-a002-415a2517c813/stream/afarinak/eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1dWlkIjoiMDUyMGFmZjQtYjMyMi00ZThlLWEwMDItNDE1YTI1MTdjODEzIn0.icmKpiQLkXSd7GVC5w7QVT16lW4eqy9KPyPYzUXo8ds/master.mpd")
                .setMediaId(MimeTypes.APPLICATION_MPD)
                .build();

        playerView.setPlayer(player);
        player.setMediaItem(mediaItem);
        player.prepare();
        player.setPlayWhenReady(true);

        btn_play = playerView.findViewById(R.id.exo_play_pause);
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
                }
                else if (item.getItemId() == R.id.quality) {
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