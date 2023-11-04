package com.example.exo;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Pair;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.util.Assertions;

import java.util.ArrayList;
import java.util.List;

public class DialogQuality extends DialogFragment {

    ListenerQuality listenerQuality;
    RadioGroup radio_Group;
    RadioButton radiobtn_automatic, radiobtn_360, radiobtn_480, radiobtn_720, radiobtn_1080;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listenerQuality = (ListenerQuality) context;
    }

    int status;
    @SuppressLint("MissingInflatedId")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_quality, null, false);
        builder.setView(view);

        radio_Group = view.findViewById(R.id.radio_group_quality);
        radiobtn_automatic = view.findViewById(R.id.quality_atomatic);
        radiobtn_360 = view.findViewById(R.id.quality_360);
        radiobtn_480 = view.findViewById(R.id.quality_480);
        radiobtn_720 = view.findViewById(R.id.quality_720);
        radiobtn_1080 = view.findViewById(R.id.quality_1080);

        switch (MainActivity.currentQuality){
            case 0:
                radiobtn_automatic.setChecked(true);
                break;
            case 1:
                radiobtn_360.setChecked(true);
                break;
            case 2:
                radiobtn_480.setChecked(true);
                break;
            case 3:
                radiobtn_720.setChecked(true);
                break;
            case 4:
                radiobtn_1080.setChecked(true);
                break;
        }

        radio_Group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.quality_atomatic) {
                    radiobtn_automatic.setChecked(true);
                    status = 0;
                    listenerQuality.itemClickToChangeQuality(status);
                } else if (i == R.id.quality_360) {
                    radiobtn_360.setChecked(true);
                    status = 1;
                    listenerQuality.itemClickToChangeQuality(status);
                } else if (i == R.id.quality_480) {
                    radiobtn_480.setChecked(true);
                    status = 2;
                    listenerQuality.itemClickToChangeQuality(status);
                } else if (i == R.id.quality_720) {
                    radiobtn_720.setChecked(true);
                    status = 3;
                    listenerQuality.itemClickToChangeQuality(status);
                } else if (i == R.id.quality_1080) {
                    radiobtn_1080.setChecked(true);
                    status = 4;
                    listenerQuality.itemClickToChangeQuality(status);
                }
                dismiss();
            }
        });

        return builder.create();
    }

    interface ListenerQuality {
        void itemClickToChangeQuality(int status);
    }
}