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

        radio_Group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.quality_atomatic) {
                    listenerQuality.itemClickToChangeQuality("auto");
                } else if (i == R.id.quality_360) {
                    listenerQuality.itemClickToChangeQuality("360");
                } else if (i == R.id.quality_480) {
                    listenerQuality.itemClickToChangeQuality("480");
                } else if (i == R.id.quality_720) {
                    listenerQuality.itemClickToChangeQuality("720");
                } else if (i == R.id.quality_1080) {
                    listenerQuality.itemClickToChangeQuality("1080");
                }
                dismiss();
            }
        });

        return builder.create();
    }

    interface ListenerQuality {
        void itemClickToChangeQuality(String selectedQuality);
    }
}