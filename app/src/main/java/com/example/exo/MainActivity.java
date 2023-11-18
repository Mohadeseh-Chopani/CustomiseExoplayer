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
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.SeekBar;

import com.example.exo.Brightness.BrightnessControl;
import com.example.exo.Buffer.Buffering;
import com.example.exo.Fullscreen.Fullscreen;
import com.example.exo.Language.LanguageDialog;
import com.example.exo.Quality.QualityDialog;
import com.example.exo.Speed.SpeedDialog;
import com.example.exo.Subtitle.SubtitleDialog;
import com.example.exo.Subtitle.SubtitleList;
import com.example.exo.Volume.VolumeChangeReceiver;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.analytics.AnalyticsListener;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionParameters;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.DefaultTimeBar;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackException;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.BuildConfig;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.google.android.exoplayer2.util.MimeTypes;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SpeedDialog.ListenerSpeed {

    public static ExoPlayer player;
    boolean isShowingTrackSelectionDialog;
    ImageView btnPlay, img_volume, settings, btnSubtitle, btnFullscreen, btnLanguage, img_brightness, btnLock, btnLockBehind, btn_speed;
    StyledPlayerView playerView;
    SeekBar seekBarVolume, seekBarBrightness;
    float currentVolume;
    ConstraintLayout layoutControl;
    VolumeChangeReceiver volumeChangeReceiver;
    Fullscreen.StatusFullscreen statusFullscreen = Fullscreen.StatusFullscreen.Fill;
    MediaItem.SubtitleConfiguration subtitleConfig;
    AudioManager audioManager;
    ProgressBar progressBar;
    DefaultTimeBar defaultTimeBar;
    LinearLayout layoutVolume;
    ConstraintLayout layoutController;
    public static boolean statusRepetition;
    static int currentQuality;
    Uri subtitleUri1 = Uri.parse("https://vod2.afarinak.com/api/video/subtitle/72/download/");

    Uri videoUri = Uri.parse("\n" +
            " https://vod2.afarinak.com/api/video/5f3aa557-e4ca-4d46-9844-b1059b59b3c6/stream/afarinak/eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1dWlkIjoiNWYzYWE1NTctZTRjYS00ZDQ2LTk4NDQtYjEwNTliNTliM2M2In0.N-pMsQ1RvBpd0sr4J029BYCqKi7TixT9CrG8Mv-Ai0A/master.mpd");

    public static List<MediaItem.SubtitleConfiguration> subtitleList = new ArrayList<>();
    static DefaultTrackSelector defaultTrackSelector;
    public static List<String> languageListName = new ArrayList<>();
    public static List<String> languageListId = new ArrayList<>();
    public static String statusLanguage = "en";
    public static String statusSubtitle = "fa";
    boolean statusLock = false;
    int countLanguage = 0;

    @SuppressLint({"MissingInflatedId", "ResourceAsColor"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playerView = findViewById(R.id.playView);
        seekBarVolume = findViewById(R.id.SeekBarVolume);
        img_volume = findViewById(R.id.img_volume);
        btnSubtitle = findViewById(R.id.btnSubtitle);
        defaultTimeBar = findViewById(R.id.exo_progress);
        btnFullscreen = findViewById(R.id.btn_fullscreen);
        btnLanguage = findViewById(R.id.exo_language);
        btnPlay = playerView.findViewById(R.id.exo_play_pause);
        settings = playerView.findViewById(R.id.exo_track_selection_view);
        progressBar = findViewById(R.id.progressbar);
        layoutVolume = findViewById(R.id.layout_volume);
        layoutController = findViewById(R.id.layout_controller);
        seekBarBrightness = findViewById(R.id.SeekBarBrightness);
        img_brightness = findViewById(R.id.img_brightness);
        btnLock = findViewById(R.id.btn_lock);
        btnLockBehind = playerView.findViewById(R.id.btn_lock_behind);
        layoutControl = findViewById(R.id.layout_control);
        btn_speed = findViewById(R.id.btn_speed);


        //Set default trackselection for exoplayer
        defaultTrackSelector = new DefaultTrackSelector(this);
        TrackSelectionParameters trackSelectionParameters = new TrackSelectionParameters.Builder()
                .setPreferredAudioLanguage("en")
                .setPreferredTextLanguage("fa")
                .setForceLowestBitrate(true)
                .build();

        defaultTrackSelector.setParameters(trackSelectionParameters);

        //Make instance of Exoplayer
        player = Initialize(defaultTrackSelector);

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
        SubtitleList subtitles = new SubtitleList();
        subtitles.addSubtitle(" ", "off", "خاموش");
        subtitles.addSubtitle(String.valueOf(subtitleUri1),"fa" ,"فارسی");

        for (int i = 0; i < SubtitleList.subtitles.size() ; i++) {
            subtitleConfig = new MediaItem.SubtitleConfiguration.Builder(Uri.parse(SubtitleList.subtitles.get(i).getUrl()))
                    .setUri(Uri.parse(SubtitleList.subtitles.get(i).getUrl()))
                    .setMimeType(MimeTypes.APPLICATION_SUBRIP)
                    .setLanguage(SubtitleList.subtitles.get(i).getId())
                    .setLabel(SubtitleList.subtitles.get(i).getLanguage())
                    .setSelectionFlags(C.SELECTION_FLAG_AUTOSELECT)
                    .build();
            MainActivity.subtitleList.add(subtitleConfig);
        }

        MediaItem mediaItem = new MediaItem.Builder()
                .setUri(videoUri)
                .setSubtitleConfigurations(this.subtitleList)
                .build();

        player.setMediaItem(mediaItem);
        player.prepare();
        player.setPlayWhenReady(true);

        //Set quality for video
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isShowingTrackSelectionDialog && QualityDialog.willHaveContent(defaultTrackSelector)) {
                    isShowingTrackSelectionDialog = true;
                    QualityDialog qualityDialog = QualityDialog.createForTrackSelector(defaultTrackSelector,
                            /* onDismissListener= */ dismissedDialog -> isShowingTrackSelectionDialog = false);
                    qualityDialog.show(getSupportFragmentManager(), /* tag= */ null);
                }
            }
        });

        //Set speed display for video
        btn_speed.setOnClickListener(view -> {
            SpeedDialog speedDialog = new SpeedDialog();
            speedDialog.show(getSupportFragmentManager(), null);
        });

        btnSubtitle.setOnClickListener(view -> {
            SubtitleDialog subtitleDialog = new SubtitleDialog();
            subtitleDialog.show(getSupportFragmentManager(), null);
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
            PopupMenu popupMenu = new PopupMenu(this, view);
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
            LanguageDialog dialog = new LanguageDialog();
            dialog.show(getSupportFragmentManager(), null);
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
                if (playbackState == Player.STATE_ENDED && SpeedDialog.statusSwitch) {
                    player.seekTo(0);
                    player.setPlayWhenReady(true);
                }
            }
        });


        player.addListener(new Player.Listener() {
            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

                if (trackSelections.get(0) != null) {
                    currentQuality = trackSelections.get(0).getFormat(0).height;
                }
                if (trackSelections.get(0).getType() == C.TRACK_TYPE_VIDEO) {

                }
            }
        });

        player.addAnalyticsListener(new AnalyticsListener() {
            @Override
            public void onPlaybackStateChanged(EventTime eventTime, int state) {
                if (state == Player.STATE_READY) {
                    progressBar.setVisibility(View.GONE);
                    player.setPlayWhenReady(true);
                    countLanguage = languageListName.size();
                    languageListName.clear();
                    //Read audio language from vide and add to list
                    audioLanguage();

                } else if (state == Player.STATE_BUFFERING) {
                    progressBar.setVisibility(View.VISIBLE);
                    playerView.setKeepScreenOn(true);
                    languageListName.clear();
                    //Read audio language from vide and add to list
                    audioLanguage();

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

        //Lock Player
        btnLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (layoutControl.isEnabled()) {
                    layoutControl.setVisibility(View.INVISIBLE);
                    layoutVolume.setVisibility(View.INVISIBLE);
                    btnLockBehind.setVisibility(View.VISIBLE);

                    statusLock = true;
                    timerLock();
                }
            }
        });

        btnLockBehind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int statusVisibility = layoutControl.getVisibility();
                if (statusVisibility == View.INVISIBLE) {
                    layoutControl.setVisibility(View.VISIBLE);
                    layoutVolume.setVisibility(View.VISIBLE);
                    btnLockBehind.setVisibility(View.INVISIBLE);
                }
                statusLock = false;
            }
        });

        player.addListener(new Player.Listener() {
            @Override
            public void onTracksChanged(TrackGroupArray trackArray, TrackSelectionArray trackSelections) {


            }
        });

        playerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (statusLock){
                    btnLockBehind.setVisibility(View.VISIBLE);
                    timerLock();
                }
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

    @Override
    protected void onPause() {
        super.onPause();
        player.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        player.play();
    }

    public static float currentSpeed = 1.0f;

    //Set volume over speed dialog
    @Override
    public void itemClick(Float speed) {

        Float pitch = 1.0f;
        PlaybackParameters playbackParameters = new PlaybackParameters(speed, pitch);
        player.setPlaybackParameters(playbackParameters);
        statusRepetition = SpeedDialog.statusSwitch;
        currentSpeed = player.getPlaybackParameters().speed;
    }

    private ExoPlayer Initialize(TrackSelector selector) {
        return new ExoPlayer.Builder(this).setLoadControl(Buffering.getBuffer()).setTrackSelector(selector).build();
    }

    private void audioLanguage() {
        MappingTrackSelector.MappedTrackInfo mappedTrackInfo = defaultTrackSelector.getCurrentMappedTrackInfo();
        if (mappedTrackInfo != null) {
            for (int i = 0; i < mappedTrackInfo.getRendererCount(); i++) {
                if (player.getRendererType(i) == C.TRACK_TYPE_AUDIO) {
                    TrackGroupArray trackGroupArray = mappedTrackInfo.getTrackGroups(i);
                    for (int j = 0; j < trackGroupArray.length; j++) {
                        Format format = trackGroupArray.get(j).getFormat(0);
                        languageListName.add(format.id);
                        languageListId.add(format.language);
                    }
                }
            }
        }
    }

    //Set timer to show lock button
    private void timerLock(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                btnLockBehind.setVisibility(View.INVISIBLE);
            }
        }, 3000);
    }
}