package com.example.exo;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.StyledPlayerView;

public class Fullscreen {
    StyledPlayerView playerView;

    public Fullscreen(StyledPlayerView playerView) {
        this.playerView = playerView;
    }

    enum StatusFullscreen {
        Fill,
        ZOOM,
        Fit,
        FIXED_WIDTH,
        FIXED_HEIGHT
    }

    StatusFullscreen statusFullscreen;

    public StatusFullscreen setFullscreen(MenuItem menuItem) {

        if (menuItem.getItemId() == R.id.FillMode) {
            playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
            statusFullscreen = StatusFullscreen.Fill;
        } else if (menuItem.getItemId() == R.id.FitMode) {
            playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
            playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
            statusFullscreen = StatusFullscreen.Fit;
        } else if (menuItem.getItemId() == R.id.FixedWidthMode) {
            playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
            playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH);
            statusFullscreen = StatusFullscreen.FIXED_WIDTH;
        } else if (menuItem.getItemId() == R.id.FixedHeightMode) {
            playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
            playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_HEIGHT);
            statusFullscreen = StatusFullscreen.FIXED_HEIGHT;
        } else if (menuItem.getItemId() == R.id.ZoomMode) {
            playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
            playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_ZOOM);
            statusFullscreen = StatusFullscreen.ZOOM;
        }
        return statusFullscreen;
    }
}

