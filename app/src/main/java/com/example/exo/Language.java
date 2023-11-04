package com.example.exo;

import android.content.Context;
import android.net.Uri;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionParameters;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.util.Assertions;

import java.util.ArrayList;
import java.util.List;

public class Language {

    ExoPlayer player;
    Context context;

    static int currentLanguage;

    public Language(ExoPlayer player, Context context) {
        this.context = context;
        this.player = player;
    }

    String[] languageList = {"en", "fa"};
    String language;

    public void setLanguage(int status) {

        if (status == 0) {
            language = languageList[0];
            currentLanguage = 0;
        } else {
            language = languageList[1];
            currentLanguage = 1;
        }
        player.setTrackSelectionParameters(
                player.getTrackSelectionParameters()
                        .buildUpon()
                        .setMaxVideoSizeSd()
                        .setPreferredAudioLanguage(language)
                        .build());
    }
}
