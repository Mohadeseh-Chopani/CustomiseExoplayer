package com.example.exo;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.exoplayer.hls.HlsMediaSource;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.SeekBar;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.analytics.AnalyticsListener;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.DefaultTimeBar;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.MimeTypes;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackException;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.BuildConfig;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DialogSpeed.ListenerSpeed, DialogQuality.ListenerQuality, DialogSubtitle.setSubtitle, DialogLanguage.setLanguage {

    ExoPlayer player;
    ImageView btnPlay, img_volume, settings, btnSubtitle, btnFullscreen, btnLanguage;
    StyledPlayerView playerView;
    SeekBar seekBar;
    float currentVolume;
    VolumeChangeReceiver volumeChangeReceiver;

    MediaItem.SubtitleConfiguration subtitle1, subtitle2;
    AudioManager audioManager;

    ProgressBar progressBar;
    DefaultTimeBar defaultTimeBar;
    long position;
    Language languageClass;
    static int currentQuality;
    DialogSubtitle.Status status = DialogSubtitle.Status.PERSIAN;
    Uri subtitleUri1 = Uri.parse("https://vod2.afarinak.com/api/video/subtitle/72/download/");

    Uri videoUri = Uri.parse("https://vod2.afarinak.com/api/video/a624b7ec-50b6-4997-a04a-b8e440a6bba4/stream/afarinak/eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1dWlkIjoiYTYyNGI3ZWMtNTBiNi00OTk3LWEwNGEtYjhlNDQwYTZiYmE0In0.4y-WUvDNBBULYgvma0Vup3G0PaKnRvI6QF6ivVUWiZc/master.mpd");

    List<MediaItem.SubtitleConfiguration> subtitles = new ArrayList<>();
    Subtitle subtitleClass;
    Uri subtitleUri2 = Uri.parse("");

    @SuppressLint({"MissingInflatedId", "ResourceAsColor"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playerView = findViewById(R.id.playview);
        seekBar = findViewById(R.id.mySeekBar);
        img_volume = findViewById(R.id.img_volume);
        btnSubtitle = findViewById(R.id.btnSubtitle);
        defaultTimeBar = findViewById(R.id.exo_progress);
        btnFullscreen = findViewById(R.id.btn_fullscreen);
        btnLanguage = findViewById(R.id.exo_language);
        btnPlay = playerView.findViewById(R.id.exo_play_pause);
        settings = playerView.findViewById(R.id.settings);
        progressBar = findViewById(R.id.progressbar);


        DefaultTrackSelector trackSelector = new DefaultTrackSelector(this);

        //Make instance of Exoplayer
        player = new ExoPlayer.Builder(this).setLoadControl(Buffering.getBuffer(Subtitle.buffering)).setTrackSelector(trackSelector).setTrackSelector(trackSelector).build();
        playerView.setPlayer(player);


        //Checking video for bilingual
        if (player.getCurrentTrackGroups().length > 1) {
            int audioTrackCount = player.getCurrentTrackGroups().get(0).length;
            if (audioTrackCount > 1)
                btnLanguage.setBackgroundColor(R.color.unSetItem);
            else
                btnLanguage.setBackgroundColor(R.color.white);
        }

        //this method is to prevent player restart
        playerView.setKeepContentOnPlayerReset(true);


        //Set subtitles
        subtitle1 = new MediaItem.SubtitleConfiguration.Builder(subtitleUri1)
                .setMimeType(MimeTypes.APPLICATION_SUBRIP)
                .setLanguage("fa")
                .setSelectionFlags(C.SELECTION_FLAG_DEFAULT)
                .build();

        subtitle2 = new MediaItem.SubtitleConfiguration.Builder(subtitleUri2)
                .setMimeType(MimeTypes.APPLICATION_SUBRIP)
                .setLanguage("fa")
                .setSelectionFlags(C.SELECTION_FLAG_AUTOSELECT)
                .build();


        subtitles.add(0, subtitle1);
        subtitles.add(1, subtitle2);


        //Make instance of language class
        languageClass = new Language(player, this);

        //Set subtitle over video
        subtitleClass = new Subtitle(player, this, subtitleUri1);
        subtitleClass.setSubtitleConfigurations(videoUri, subtitles, status);
        player.setPlayWhenReady(true);


        settings.setOnClickListener(v -> settings(v));


        btnSubtitle.setOnClickListener(view -> {
            DialogSubtitle dialogSubtitle = new DialogSubtitle();
            dialogSubtitle.show(getSupportFragmentManager(), null);
        });


        //Change image volume than volume of player
        img_volume.setOnClickListener(view -> {
            if (player.getVolume() != 0.0f) {
                img_volume.setImageResource(R.drawable.baseline_volume_off_24);
                currentVolume = player.getVolume();
                player.setVolume(0.0f);
                seekBar.setProgress(0);
            } else {
                img_volume.setImageResource(R.drawable.baseline_volume_down_24);
                player.setVolume(currentVolume);
                seekBar.setProgress((int) (currentVolume * 100));
            }
        });


        //Fullscreen button
        btnFullscreen.setOnClickListener(view -> {
            playerView.setUseController(playerView.getUseController());
            if (playerView.getResizeMode() == AspectRatioFrameLayout.RESIZE_MODE_FILL) {
                playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
                btnFullscreen.setImageResource(R.drawable.outline_fullscreen_24);
            } else {
                playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
                btnFullscreen.setImageResource(R.drawable.baseline_fullscreen_exit_24);
            }
        });

        btnLanguage.setOnClickListener(view -> {
            DialogLanguage dialogLanguage = new DialogLanguage();
            dialogLanguage.show(getSupportFragmentManager(), null);
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


        player.addAnalyticsListener(new AnalyticsListener() {
            @Override
            public void onPlaybackStateChanged(EventTime eventTime, int state) {
                if (state == Player.STATE_READY) {
                    progressBar.setVisibility(View.GONE);
                    player.setPlayWhenReady(true);
                } else if (state == Player.STATE_BUFFERING) {
                    progressBar.setVisibility(View.VISIBLE);
                    playerView.setKeepScreenOn(true);
                } else {
                    progressBar.setVisibility(View.GONE);
                    player.setPlayWhenReady(true);
                }
            }
        });


        //Send broadcast to OS to set volume
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        volumeChangeReceiver = new VolumeChangeReceiver(seekBar, audioManager);
        IntentFilter intentFilter = new IntentFilter("android.media.VOLUME_CHANGED_ACTION");
        registerReceiver(volumeChangeReceiver, intentFilter);


        //Set seekBar based on desired volume
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

    //Change image based on change volume
    @SuppressLint("ResourceAsColor")
    void setVolume(float volume) {
        player.setVolume(volume);
        if (volume == 0.0f) {
            img_volume.setImageResource(R.drawable.baseline_volume_off_24);
        } else if (volume <= 0.05f) {
            img_volume.setImageResource(R.drawable.baseline_volume_down_24);
        } else {
            img_volume.setImageResource(R.drawable.baseline_volume_up_24);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.setPlayWhenReady(false);
        player.release();
        player = null;
    }

    //Set popup menu for setting button
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

    public static float currentSpeed;

    //Set volume over speed dialog
    @Override
    public void itemClick(Float speed) {

        Float pitch = 1.0f;
        PlaybackParameters playbackParameters = new PlaybackParameters(speed, pitch);
        player.setPlaybackParameters(playbackParameters);
        currentSpeed = player.getPlaybackParameters().speed;
    }


    //Set quality over quality dialog
    @Override
    public void itemClickToChangeQuality(int status) {
        DefaultTrackSelector trackSelector = (DefaultTrackSelector) player.getTrackSelector();
        DefaultTrackSelector.Parameters.Builder parametersBuilder = trackSelector.buildUponParameters();

        switch (status) {
            case 0:
                currentQuality = 0;
                break;
            case 1:
                parametersBuilder.setMaxVideoSize(680, 360);
                currentQuality = 1;
                break;
            case 2:
                parametersBuilder.setMaxVideoSize(480, 360);
                currentQuality = 2;
                break;
            case 3:
                parametersBuilder.setMaxVideoSize(1280, 720);
                currentQuality = 3;
                break;
            case 4:
                parametersBuilder.setMaxVideoSize(1920, 1080);
                currentQuality = 4;
                break;
        }
        trackSelector.setParameters(parametersBuilder.build());
        player.setTrackSelectionParameters(parametersBuilder.build());
    }


    //Set subtitle through dialog
    @Override
    public void clickItemToChooseSub(DialogSubtitle.Status status) {
        this.status = status;
        position = player.getCurrentPosition();
        subtitleClass.setSubtitleConfigurations(videoUri, subtitles, status);
        player.seekTo(position);
    }

    //Set language through dialog
    @Override
    public void clickItemToChooseLan(int status) {
        languageClass.setLanguage(status);
    }
}