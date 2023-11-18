package com.example.exo.Buffer;

import android.util.Log;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.LoadControl;

public class Buffering {

    public static LoadControl getBuffer() {
        DefaultLoadControl loadControl;
        try {
            Log.i("Buffering_Test","try");
            loadControl = new DefaultLoadControl.Builder()
                    .setBufferDurationsMs(400000, 400000, 2500, 5000)
                    .build();
        } catch (Exception e) {
            Log.i("Buffering_Test","catch");
            loadControl = new DefaultLoadControl.Builder()
                    .setBufferDurationsMs(DefaultLoadControl.DEFAULT_MIN_BUFFER_MS, DefaultLoadControl.DEFAULT_MAX_BUFFER_MS,
                            DefaultLoadControl.DEFAULT_BUFFER_FOR_PLAYBACK_MS, DefaultLoadControl.DEFAULT_BUFFER_FOR_PLAYBACK_AFTER_REBUFFER_MS)
                    .build();
        }
        return loadControl;
    }
}
