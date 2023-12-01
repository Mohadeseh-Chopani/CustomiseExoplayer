package com.example.exo.Subtitle;

import static com.example.exo.MainActivity.player;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exo.Adapter.AdapterSubtitle;
import com.example.exo.MainActivity;
import com.example.exo.Model.DataPlayer;
import com.example.exo.R;
import com.google.android.exoplayer2.MediaItem;

import java.util.ArrayList;
import java.util.List;

public class SubtitleDialog extends DialogFragment implements AdapterSubtitle.setSubtitle {
    AdapterSubtitle adapterSubtitle;
    RecyclerView rv_subtitle;
    List<MediaItem.SubtitleConfiguration>list = new ArrayList<>();
    public SubtitleDialog(List<MediaItem.SubtitleConfiguration> list) {
        this.list = list;
    }

    @Override
    public void onResume() {
        super.onResume();

        Window window = getDialog().getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        if (window == null)
            return;
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = 800;
        window.setAttributes(params);
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.MyCustomTheme);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_subtitle, null, false);
        builder.setView(view);

        rv_subtitle = view.findViewById(R.id.rv_subtitle);

        rv_subtitle.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        adapterSubtitle = new AdapterSubtitle(list, getContext(), this);
        rv_subtitle.setAdapter(adapterSubtitle);



        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.background_dialog);
        return alertDialog;
    }
    @Override
    public void clickItemToChooseSub(String status) {
        MainActivity.statusSubtitle = status;

        player.setTrackSelectionParameters(
                player.getTrackSelectionParameters()
                        .buildUpon()
                        .setMaxVideoSizeSd()
                        .setPreferredTextLanguage(status)
                        .build());
//        dismiss();
    }
}
