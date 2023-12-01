package com.example.exo.Language;

import static com.example.exo.MainActivity.player;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exo.Adapter.AdapterLanguage;
import com.example.exo.MainActivity;
import com.example.exo.Model.DataPlayer;
import com.example.exo.R;
import com.google.android.exoplayer2.Format;

import java.util.ArrayList;
import java.util.List;

public class LanguageDialog extends DialogFragment implements AdapterLanguage.setLanguage {
    AdapterLanguage adapterLanguage;
    RecyclerView rv_language;
    List<Format> list = new ArrayList<>();

    public LanguageDialog(List<Format> list) {
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

    @SuppressLint("MissingInflatedId")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.MyCustomTheme);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.language_dialog, null, false);
        builder.setView(view);


        rv_language = view.findViewById(R.id.rv_language);

        rv_language.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        adapterLanguage = new AdapterLanguage(MainActivity.formatList, getContext(), this);
        rv_language.setAdapter(adapterLanguage);


        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.background_dialog);
        return alertDialog;
    }

    @Override
    public void clickItemToChooseLan(String status) {
        MainActivity.statusLanguage = status;

        player.setTrackSelectionParameters(
                player.getTrackSelectionParameters()
                        .buildUpon()
                        .setMaxVideoSizeSd()
                        .setPreferredAudioLanguage(status)
                        .build());
//        dismiss();
    }
}
