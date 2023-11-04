package com.example.exo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.MediaMetadata;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MergingMediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.SingleSampleMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionParameters;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.MimeTypes;

import java.util.Collections;
import java.util.List;

public class Subtitle {
    ExoPlayer player;
    Uri subtitleUri;
    static int buffering;
    Context context;
    static int currentSubtitle;

    public Subtitle(ExoPlayer player, Context context, Uri subtitleUri) {
        this.player = player;
        this.context = context;
        this.subtitleUri = subtitleUri;
    }

    public void setSubtitleConfigurations(Uri videoUri, List<MediaItem.SubtitleConfiguration> subtitles, DialogSubtitle.Status status) {

        MediaItem mediaItem;

        mediaItem = new MediaItem.Builder()
                .setUri(videoUri)
                .setSubtitleConfigurations(subtitles)
                .build();


        if (status == DialogSubtitle.Status.OFF) {
            player.setTrackSelectionParameters(player.getTrackSelectionParameters().buildUpon().setTrackTypeDisabled(C.TRACK_TYPE_TEXT, true).build());
            currentSubtitle = 0;
        } else if (status == DialogSubtitle.Status.PERSIAN) {
            player.setTrackSelectionParameters(player.getTrackSelectionParameters().buildUpon().setTrackTypeDisabled(C.TRACK_TYPE_TEXT, false).build());
            currentSubtitle = 1;
        }

        player.setMediaItem(mediaItem);
        player.prepare();
    }
}
