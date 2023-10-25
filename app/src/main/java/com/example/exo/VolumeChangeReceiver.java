package com.example.exo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.widget.SeekBar;

public class VolumeChangeReceiver extends BroadcastReceiver {

    private SeekBar volumeSeekBar;
    private AudioManager audioManager;

    public VolumeChangeReceiver(SeekBar volumeSeekBar, AudioManager audioManager) {
        this.volumeSeekBar = volumeSeekBar;
        this.audioManager = audioManager;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && intent.getAction().equals("android.media.VOLUME_CHANGED_ACTION")) {
            int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            volumeSeekBar.setProgress(currentVolume);
        }
    }
}
