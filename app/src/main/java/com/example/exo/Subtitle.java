package com.example.exo;

import android.content.Context;
import android.net.Uri;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionParameters;
import com.google.android.exoplayer2.trackselection.TrackSelector;

import java.util.List;

public class Subtitle {
    Uri subtitleUri;
    static int buffering;
    Context context;
    static int currentSubtitle = 1;

    public Subtitle (Context context, Uri subtitleUri) {
        this.context = context;
        this.subtitleUri = subtitleUri;
    }

    public TrackSelector setSubtitle(ExoPlayer player, DialogSubtitle.Status status) {

        String subtitle;
        if (status == DialogSubtitle.Status.OFF)
            subtitle = " ";
        else
            subtitle = "fa";

        TrackSelector trackSelector =new DefaultTrackSelector(context);
        TrackSelectionParameters trackSelectionParameters = new TrackSelectionParameters.Builder()
                .setPreferredTextLanguage(subtitle)
                .build();

        trackSelector.setParameters(trackSelectionParameters);


        if (status == DialogSubtitle.Status.OFF) {
            player.setTrackSelectionParameters(player.getTrackSelectionParameters().buildUpon().setTrackTypeDisabled(C.TRACK_TYPE_TEXT, true).build());
            currentSubtitle = 0;
        } else if (status == DialogSubtitle.Status.PERSIAN) {
            player.setTrackSelectionParameters(player.getTrackSelectionParameters().buildUpon().setTrackTypeDisabled(C.TRACK_TYPE_TEXT, false).build());
            currentSubtitle = 1;
        }

        return trackSelector;
    }
}
