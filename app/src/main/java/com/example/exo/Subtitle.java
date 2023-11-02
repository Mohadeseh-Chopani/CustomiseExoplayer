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

    public Subtitle(ExoPlayer player, Context context,Uri subtitleUri) {
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

        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context, "userAgent");
//
//// Create a Format.Builder object
//        Format.Builder videoBuilder = new Format.Builder();
//
//// Set the properties of the format
//        videoBuilder.setId(null); // An identifier for the track. May be null.
//        videoBuilder.setSampleMimeType(MimeTypes.APPLICATION_SUBRIP); // The mime type. Must be set correctly.
//        videoBuilder.setInitializationData(null); // A codec-specific initialization data. May be null.
//        videoBuilder.setAverageBitrate(Format.NO_VALUE); // The average bandwidth in bits per second. May be Format.NO_VALUE.
//        videoBuilder.setPeakBitrate(Format.NO_VALUE); // The maximum bandwidth in bits per second. May be Format.NO_VALUE.
//        videoBuilder.setWidth(1280); // The width of the video in pixels. May be Format.NO_VALUE.
//        videoBuilder.setHeight(720); // The height of the video in pixels. May be Format.NO_VALUE.
//        videoBuilder.setFrameRate(30f); // The frame rate in frames per second. May be Format.NO_VALUE.
//        videoBuilder.setDrmInitData(null); // Initialization data for drm schemes. May be null.
//        videoBuilder.setSubsampleOffsetUs(Format.OFFSET_SAMPLE_RELATIVE); // The subsample offset. May be Format.OFFSET_SAMPLE_RELATIVE.
//
//// Build the Format object
//        Format videoFormat = videoBuilder.build();


//        MediaSource video_source = new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(mediaItem);
//
//        MediaSource textMediaSource = new SingleSampleMediaSource.Factory(dataSourceFactory)
//                .createMediaSource(subtitles.get(1), C.TRACK_TYPE_TEXT);
//        MediaSource mediaSource = new MergingMediaSource(video_source, textMediaSource);




        if (status == DialogSubtitle.Status.OFF) {
            player.setTrackSelectionParameters(player.getTrackSelectionParameters().buildUpon().setTrackTypeDisabled(C.TRACK_TYPE_TEXT, true).build());
        }
        else if (status == DialogSubtitle.Status.PERSIAN) {
            player.setTrackSelectionParameters(player.getTrackSelectionParameters().buildUpon().setTrackTypeDisabled(C.TRACK_TYPE_TEXT, false).build());
        }

//        MediaSource mediaSource = new DefaultMediaSourceFactory(dataSourceFactory).createMediaSource(mediaItem);





        player.setMediaItem(mediaItem);
        player.prepare();
    }
}
