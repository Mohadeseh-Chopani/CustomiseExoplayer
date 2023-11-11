package com.example.exo;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.SeekBar;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.analytics.AnalyticsListener;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.TrackSelectionParameters;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.DefaultTimeBar;
import com.google.android.exoplayer2.util.MimeTypes;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackException;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.BuildConfig;
import com.google.android.exoplayer2.ui.StyledPlayerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DialogSpeed.ListenerSpeed, DialogQuality.ListenerQuality, DialogSubtitle.setSubtitle, DialogLanguage.setLanguage {

    ExoPlayer player;
    ImageView btnPlay, img_volume, settings, btnSubtitle, btnFullscreen, btnLanguage, img_brightness;
    StyledPlayerView playerView;
    SeekBar seekBarVolume, seekBarBrightness;
    float currentVolume;
    VolumeChangeReceiver volumeChangeReceiver;

    Fullscreen.StatusFullscreen statusFullscreen = Fullscreen.StatusFullscreen.Fill;
    MediaItem.SubtitleConfiguration subtitle1, subtitle2;
    AudioManager audioManager;
    ProgressBar progressBar;
    DefaultTimeBar defaultTimeBar;
    LinearLayout layoutVolume;
    ConstraintLayout layoutController;
    long position;
    Language languageClass;
    static boolean statusRepetition;
    static int currentQuality;
    Uri subtitleUri1 = Uri.parse("https://vod2.afarinak.com/api/video/subtitle/72/download/");

    Uri videoUri = Uri.parse("https://vod2.afarinak.com/api/video/a624b7ec-50b6-4997-a04a-b8e440a6bba4/stream/afarinak/eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1dWlkIjoiYTYyNGI3ZWMtNTBiNi00OTk3LWEwNGEtYjhlNDQwYTZiYmE0In0.4y-WUvDNBBULYgvma0Vup3G0PaKnRvI6QF6ivVUWiZc/master.mpd");

    List<MediaItem.SubtitleConfiguration> subtitles = new ArrayList<>();
    Subtitle subtitleClass;
    TrackSelector trackSelectorSub;
    Uri subtitleUri2 = Uri.parse("");

    @SuppressLint({"MissingInflatedId", "ResourceAsColor"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playerView = findViewById(R.id.playview);
        seekBarVolume = findViewById(R.id.SeekBarVolume);
        img_volume = findViewById(R.id.img_volume);
        btnSubtitle = findViewById(R.id.btnSubtitle);
        defaultTimeBar = findViewById(R.id.exo_progress);
        btnFullscreen = findViewById(R.id.btn_fullscreen);
        btnLanguage = findViewById(R.id.exo_language);
        btnPlay = playerView.findViewById(R.id.exo_play_pause);
        settings = playerView.findViewById(R.id.settings);
        progressBar = findViewById(R.id.progressbar);
        layoutVolume = findViewById(R.id.layout_volume);
        layoutController = findViewById(R.id.layout_controller);
        seekBarBrightness = findViewById(R.id.SeekBarBrightness);
        img_brightness = findViewById(R.id.img_brightness);


        //Set subtitle over video
        subtitleClass = new Subtitle(this, subtitleUri1);


        TrackSelector defaultTrackSelector =new DefaultTrackSelector(this);
        TrackSelectionParameters trackSelectionParameters = new TrackSelectionParameters.Builder()
                .setPreferredAudioLanguage("fa")
                .setPreferredTextLanguage("fa")
                .build();

        defaultTrackSelector.setParameters(trackSelectionParameters);

        //Make instance of Exoplayer
        player = Initialize(defaultTrackSelector);

        //Make instance of language class
        languageClass = new Language(player,this);

        playerView.setPlayer(player);


        //Change brightness in player
        BrightnessControl brightnessControl = new BrightnessControl(img_brightness, player, this);
        seekBarBrightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                brightnessControl.setBrightness(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

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


        MediaItem mediaItem = new MediaItem.Builder()
                .setUri(videoUri)
                .setSubtitleConfigurations(subtitles)
                .build();


        player.setMediaItem(mediaItem);
        player.prepare();
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
                seekBarVolume.setProgress(0);
            } else {
                img_volume.setImageResource(R.drawable.baseline_volume_down_24);
                player.setVolume(currentVolume);
                seekBarVolume.setProgress((int) (currentVolume * 100));
            }
        });


        //Fullscreen button
        btnFullscreen.setOnClickListener(view -> {

            Context wrapper = new ContextThemeWrapper(this, R.style.MyPopupMenu);
            PopupMenu popupMenu = new PopupMenu(wrapper, view);
            popupMenu.getMenuInflater().inflate(R.menu.menu_fullscreen, popupMenu.getMenu());

            MenuItem fill, fit, fixed_width, fixed_height, zoom;

            fill = popupMenu.getMenu().findItem(R.id.FillMode);
            fit = popupMenu.getMenu().findItem(R.id.FitMode);
            fixed_width = popupMenu.getMenu().findItem(R.id.FixedWidthMode);
            fixed_height = popupMenu.getMenu().findItem(R.id.FixedHeightMode);
            zoom = popupMenu.getMenu().findItem(R.id.ZoomMode);

            try {
                switch (statusFullscreen) {
                    case Fill:
                        fill.setChecked(true);
                        break;
                    case Fit:
                        fit.setChecked(true);
                        break;
                    case FIXED_WIDTH:
                        fixed_width.setChecked(true);
                        break;
                    case FIXED_HEIGHT:
                        fixed_height.setChecked(true);
                        break;
                    case ZOOM:
                        zoom.setChecked(true);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    Fullscreen fullscreen = new Fullscreen(playerView);
                    statusFullscreen = fullscreen.setFullscreen(menuItem);
                    return false;
                }
            });
            popupMenu.show();
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


        //Option auto replay video
        player.addListener(new Player.Listener() {
            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if (playbackState == Player.STATE_ENDED && DialogSpeed.statusSwitch) {
                    player.seekTo(0);
                    player.setPlayWhenReady(true);
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
        volumeChangeReceiver = new VolumeChangeReceiver(seekBarVolume, audioManager);
        IntentFilter intentFilter = new IntentFilter("android.media.VOLUME_CHANGED_ACTION");
        registerReceiver(volumeChangeReceiver, intentFilter);


        //Set seekBar based on desired volume
        seekBarVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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
        Context wrapper = new ContextThemeWrapper(view.getContext(), R.style.MyPopupMenu);
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.menu_setting, popupMenu.getMenu());

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
        statusRepetition = DialogSpeed.statusSwitch;
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
         trackSelectorSub = subtitleClass.setSubtitle(player, status);
         Initialize(trackSelectorSub);
    }

    //Set language through dialog
    @Override
    public void clickItemToChooseLan(int status) {
        languageClass.setLanguage(status);
    }


    public ExoPlayer Initialize(TrackSelector selector){
       return new ExoPlayer.Builder(this).setLoadControl(Buffering.getBuffer(Subtitle.buffering)).setTrackSelector(selector).build();
    }
}