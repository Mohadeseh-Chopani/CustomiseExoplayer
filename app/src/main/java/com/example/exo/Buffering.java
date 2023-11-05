package com.example.exo;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.LoadControl;

public class Buffering {

    public static LoadControl getBuffer(int buffer) {
        DefaultLoadControl loadControl;
        try {
            loadControl = new DefaultLoadControl.Builder()
                    .setBufferDurationsMs(400000, 400000, 2500, 5000)
                    .build();
        } catch (Exception e) {
            loadControl = new DefaultLoadControl.Builder()
                    .setBufferDurationsMs(DefaultLoadControl.DEFAULT_MIN_BUFFER_MS, DefaultLoadControl.DEFAULT_MAX_BUFFER_MS,
                            DefaultLoadControl.DEFAULT_BUFFER_FOR_PLAYBACK_MS, DefaultLoadControl.DEFAULT_BUFFER_FOR_PLAYBACK_AFTER_REBUFFER_MS)
                    .build();
        }
        return loadControl;
    }
}
