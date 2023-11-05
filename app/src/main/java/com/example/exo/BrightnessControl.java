package com.example.exo;

import static android.media.MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING;

import android.app.Activity;
import android.content.Context;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayer;

public class BrightnessControl {
   ImageView imgBrightness;
   ExoPlayer player;
   Activity activity;

    public BrightnessControl(ImageView imgBrightness, ExoPlayer player, Activity activity) {
        this.imgBrightness = imgBrightness;
        this.player = player;
        this.activity = activity;
    }

    public void setBrightness(int progress){
        float brightness = progress / 100f;
        WindowManager.LayoutParams params = activity.getWindow().getAttributes();
        params.screenBrightness = brightness;
        activity.getWindow().setAttributes(params);

        player.setVideoScalingMode(C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);

        if (brightness <= 0.03f){
            imgBrightness.setImageResource(R.drawable.baseline_brightness_low_24);
        } else if (brightness <= 0.07f) {
            imgBrightness.setImageResource(R.drawable.baseline_brightness_medium_24);
        } else
            imgBrightness.setImageResource(R.drawable.baseline_brightness_high_24);
    }
}
