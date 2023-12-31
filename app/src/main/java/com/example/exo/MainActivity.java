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
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;

import com.example.exo.Brightness.BrightnessControl;
import com.example.exo.Buffer.Buffering;
import com.example.exo.Fullscreen.FullscreenDialog;
import com.example.exo.Language.LanguageDialog;
import com.example.exo.Model.DataPlayer;
import com.example.exo.Quality.TrackSelectionDialog;
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
import com.google.android.exoplayer2.ui.DefaultTimeBar;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackException;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.BuildConfig;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.google.android.exoplayer2.util.EventLogger;
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
    public static FullscreenDialog.StatusFullscreen statusFullscreen = FullscreenDialog.StatusFullscreen.Fill;
    MediaItem.SubtitleConfiguration subtitleConfig;
    AudioManager audioManager;
    ProgressBar progressBar;
    DefaultTimeBar defaultTimeBar;
    LinearLayout layoutVolume;
    ConstraintLayout layoutController;
    public static boolean statusRepetition;
    static int currentQuality;
    CountDownTimer countDownTimerLock, countDownTimerShowController;
    Uri subtitleUri1 = Uri.parse("https://vod2.afarinak.com/api/video/subtitle/72/download/");

    public static List<MediaItem.SubtitleConfiguration> subtitleList = new ArrayList<>();
    static DefaultTrackSelector defaultTrackSelector;
    public static List<String> languageListName = new ArrayList<>();
    public static List<String> languageListId = new ArrayList<>();
    public static String statusLanguage = "en";
    public static String statusSubtitle = "fa";
    boolean statusLock = false;
    int countLanguage = 0;
    public static List<Format> formatList = new ArrayList<>();
    @SuppressLint({"MissingInflatedId", "ResourceAsColor"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playerView = findViewById(R.id.playView);
        seekBarVolume = findViewById(R.id.SeekBarVolume);
        img_volume = findViewById(R.id.img_volume);
        btnSubtitle = findViewById(R.id.btnSubtitle);
        defaultTimeBar = playerView.findViewById(R.id.exo_progress);
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

        DataPlayer dataPlayer = new DataPlayer();
        dataPlayer.setVideoUrl("https://vod2.afarinak.com/api/video/a624b7ec-50b6-4997-a04a-b8e440a6bba4/stream/afarinak/eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1dWlkIjoiYTYyNGI3ZWMtNTBiNi00OTk3LWEwNGEtYjhlNDQwYTZiYmE0In0.4y-WUvDNBBULYgvma0Vup3G0PaKnRvI6QF6ivVUWiZc/master.mpd");

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

        defaultTimeBar.showScrubber(5000);
        showController();

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

        formatList.clear();
        for (int i = 0; i < SubtitleList.subtitles.size() ; i++) {
            subtitleConfig = new MediaItem.SubtitleConfiguration.Builder(Uri.parse(SubtitleList.subtitles.get(i).getUrl()))
                    .setUri(Uri.parse(SubtitleList.subtitles.get(i).getUrl()))
                    .setMimeType(MimeTypes.APPLICATION_SUBRIP)
                    .setLanguage(SubtitleList.subtitles.get(i).getId())
                    .setLabel(SubtitleList.subtitles.get(i).getLanguage())
                    .setSelectionFlags(C.SELECTION_FLAG_AUTOSELECT)
                    .build();
            dataPlayer.setSubtitleList(subtitleConfig);
        }

        MediaItem mediaItem = new MediaItem.Builder()
                .setUri(dataPlayer.getVideoUrl())
                .setSubtitleConfigurations(dataPlayer.getSubtitleList())
                .build();

        player.setMediaItem(mediaItem);
        player.prepare();
        player.setPlayWhenReady(true);

        //Set quality for video
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isShowingTrackSelectionDialog && TrackSelectionDialog.willHaveContent(defaultTrackSelector)) {
                    isShowingTrackSelectionDialog = true;
                    TrackSelectionDialog qualityDialog = TrackSelectionDialog.createForTrackSelector(defaultTrackSelector,
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
            SubtitleDialog subtitleDialog = new SubtitleDialog(dataPlayer.getSubtitleList());
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
            FullscreenDialog fullscreenDialog = new FullscreenDialog(playerView);
            fullscreenDialog.show(getSupportFragmentManager(),null);
        });


        btnLanguage.setOnClickListener(view -> {
            LanguageDialog dialog = new LanguageDialog(dataPlayer.getLanguageList());
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


        defaultTimeBar.showScrubber(5000);
        defaultTimeBar.setDuration(5000);

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
                    countLanguage = formatList.size();
                    formatList.clear();
                    //Read audio language from vide and add to list
                    audioLanguage();

                } else if (state == Player.STATE_BUFFERING) {
                    progressBar.setVisibility(View.VISIBLE);
                    playerView.setKeepScreenOn(true);
                    formatList.clear();
                    //Read audio language from vide and add to list
                    audioLanguage();

                } else {
                    progressBar.setVisibility(View.GONE);
                    player.setPlayWhenReady(true);
                }
            }
        });

        player.addAnalyticsListener(new EventLogger(defaultTrackSelector));


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

                    countDownTimerLock = new CountDownTimer(3000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            // Not used in this example
                        }

                        @Override
                        public void onFinish() {
                            btnLockBehind.setVisibility(View.INVISIBLE);
                        }
                    };
                    countDownTimerLock.start();
                    statusLock = true;
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
                    playerView.showController();
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
                    countDownTimerLock.cancel();
                    countDownTimerLock.start();
                }
                showController();
                countDownTimerShowController.cancel();
                countDownTimerShowController.start();
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

    @Override
    protected void onStop() {
        super.onStop();
        player.release();
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

    DataPlayer dataPlayer = new DataPlayer();
    private void audioLanguage() {
        MappingTrackSelector.MappedTrackInfo mappedTrackInfo = defaultTrackSelector.getCurrentMappedTrackInfo();
        if (mappedTrackInfo != null) {
            for (int i = 0; i < mappedTrackInfo.getRendererCount(); i++) {
                if (player.getRendererType(i) == C.TRACK_TYPE_AUDIO) {
                    TrackGroupArray trackGroupArray = mappedTrackInfo.getTrackGroups(i);
                    for (int j = 0; j < trackGroupArray.length; j++) {
                        Format format = trackGroupArray.get(j).getFormat(0);
//                        languageListName.add(format.id);
//                        languageListId.add(format.language);
//                        dataPlayer.setLanguageList(format);
                        formatList.add(format);
                    }
                }
            }
        }
    }


    // Set saveInstanceState for player when rotate screen
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (player != null) {
            outState.putLong("playback_position", player.getCurrentPosition());
            outState.putInt("current_window", player.getCurrentWindowIndex());
            outState.putBoolean("play_when_ready", player.getPlayWhenReady());
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        long playbackPosition = savedInstanceState.getLong("playback_position");
        int currentWindow = savedInstanceState.getInt("current_window");
        boolean playWhenReady = savedInstanceState.getBoolean("play_when_ready");

        // Restore the player state
        if (player != null) {
            player.seekTo(currentWindow, playbackPosition);
            player.setPlayWhenReady(playWhenReady);
        }
    }

    void showController(){
        countDownTimerShowController = new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // Not used in this example
            }

            @Override
            public void onFinish() {
                playerView.hideController();
            }
        };
        countDownTimerShowController.start();
    }
}
