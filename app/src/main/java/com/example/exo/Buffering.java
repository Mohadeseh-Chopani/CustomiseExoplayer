package com.example.exo;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.LoadControl;

public class Buffering {

    public static LoadControl setBuffer() {
        return new DefaultLoadControl.Builder()
                .setBufferDurationsMs(5000, 20000, 1000, 1000)
                .build();
    }
}
