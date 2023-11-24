package com.example.exo.Fullscreen;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exo.Adapter.AdapterLanguage;
import com.example.exo.MainActivity;
import com.example.exo.R;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.StyledPlayerView;

public class FullscreenDialog extends DialogFragment {

    public FullscreenDialog(StyledPlayerView playerView) {
        this.playerView = playerView;
    }

    StyledPlayerView playerView;
    RadioGroup radioGroupFullscreen;
    StatusFullscreen statusFullscreen = StatusFullscreen.Fill;
    RadioButton fill,fit,fixedHeight,fixedWidth,zoom;

    public enum StatusFullscreen {
        Fill,
        ZOOM,
        Fit,
        FIXED_WIDTH,
        FIXED_HEIGHT
    }
    @Override
    public void onResume() {
        super.onResume();

        Window window = getDialog().getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.TOP | Gravity.END);
        if (window == null)
            return;
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = 700;
        window.setAttributes(params);
    }

    @SuppressLint("MissingInflatedId")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fullscreen_dialog, null, false);
        builder.setView(view);

        fill = view.findViewById(R.id.FillMode);
        fit = view.findViewById(R.id.FitMode);
        fixedHeight = view.findViewById(R.id.FixedHeightMode);
        fixedWidth = view.findViewById(R.id.FixedWidthMode);
        zoom = view.findViewById(R.id.ZoomMode);
        radioGroupFullscreen = view.findViewById(R.id.radioGroup_fullscreen);

        switch (MainActivity.statusFullscreen){
            case Fit:
                fit.setChecked(true);
                break;
            case Fill:
                fill.setChecked(true);
                break;
            case FIXED_HEIGHT:
                fixedHeight.setChecked(true);
                break;
            case FIXED_WIDTH:
                fixedWidth.setChecked(true);
                break;
            case ZOOM:
                zoom.setChecked(true);
                break;
        }

        radioGroupFullscreen.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checked) {
                if (checked == R.id.FillMode) {
                    playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
                    statusFullscreen = StatusFullscreen.Fill;
                    fill.setChecked(true);
                } else if (checked == R.id.FitMode) {
                    playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
                    playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
                    statusFullscreen = StatusFullscreen.Fit;
                    fit.setChecked(true);
                } else if (checked == R.id.FixedWidthMode) {
                    playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
                    playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH);
                    statusFullscreen = StatusFullscreen.FIXED_WIDTH;
                    fixedWidth.setChecked(true);
                } else if (checked == R.id.FixedHeightMode) {
                    playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
                    playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_HEIGHT);
                    statusFullscreen = StatusFullscreen.FIXED_HEIGHT;
                    fixedHeight.setChecked(true);
                } else if (checked == R.id.ZoomMode) {
                    playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
                    playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_ZOOM);
                    statusFullscreen = StatusFullscreen.ZOOM;
                    zoom.setChecked(true);
                }
                MainActivity.statusFullscreen = statusFullscreen;
                dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.background_dialog);
        return alertDialog;
    }
}
