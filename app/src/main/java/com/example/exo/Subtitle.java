package com.example.exo;

import android.content.Context;
import android.media.browse.MediaBrowser;
import android.net.Uri;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MergingMediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.SingleSampleMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.List;

public class Subtitle {
    ExoPlayer player;
    Context context;

    public Subtitle(ExoPlayer player, Context context) {
        this.player = player;
        this.context = context;
    }

    public void setSubtitleConfigurations(Uri videoUri, List<MediaItem.SubtitleConfiguration> subtitles, DialogSubtitle.Status status) {

        MediaItem mediaItem;

        mediaItem = new MediaItem.Builder()
                .setUri(videoUri)
                .setSubtitleConfigurations(subtitles)
                .build();

        if (status == DialogSubtitle.Status.OFF)
            player.setTrackSelectionParameters(player.getTrackSelectionParameters().buildUpon().setTrackTypeDisabled(C.TRACK_TYPE_TEXT, true).build());
        else if (status == DialogSubtitle.Status.PERSIAN) {
            player.setTrackSelectionParameters(player.getTrackSelectionParameters().buildUpon().setTrackTypeDisabled(C.TRACK_TYPE_TEXT, false).build());
        }

        player.setMediaItem(mediaItem);
    }
}
