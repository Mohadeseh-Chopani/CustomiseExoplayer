package com.example.exo;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.LoadControl;

public class Buffering {

    public static LoadControl getBuffer(int buffer) {
        if (buffer == 0){
            return new DefaultLoadControl.Builder()
                    .setBufferDurationsMs(400000, 400000, 2500, 5000)
                    .build();
        }
        else {
            return new DefaultLoadControl.Builder()
                    .setBufferDurationsMs(400000, buffer, 2500, 5000)
                    .build();
        }
    }
}
