package com.example.exo;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class DialogSubtitle extends DialogFragment {

    RadioGroup radioGroup_subtitle;
    RadioButton radioButton_off, radioButton_persian;


    setSubtitle setSubtitle;

    enum Status {
        OFF,
        PERSIAN
    }

    Status status = Status.PERSIAN;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        setSubtitle = (setSubtitle) context;
    }

    @SuppressLint("MissingInflatedId")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.MyCustomTheme);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_subtitle, null, false);
        builder.setView(view);


        radioGroup_subtitle = view.findViewById(R.id.radioGroup_sub);
        radioButton_off = view.findViewById(R.id.btn_off_sub);
        radioButton_persian = view.findViewById(R.id.btn_persian_sub);


        switch (Subtitle.currentSubtitle) {
            case 0:
                radioButton_off.setChecked(true);
                break;
            case 1:
                radioButton_persian.setChecked(true);
                break;
        }
        radioGroup_subtitle.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int item) {
                if (item == R.id.btn_off_sub) {
                    radioButton_off.setChecked(true);
                    status = Status.OFF;
                    setSubtitle.clickItemToChooseSub(status);
                } else if (item == R.id.btn_persian_sub) {
                    radioButton_persian.setChecked(true);
                    status = Status.PERSIAN;
                    setSubtitle.clickItemToChooseSub(status);
                }
                dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.background_dialog);
        return alertDialog;
    }

    interface setSubtitle {
        void clickItemToChooseSub(Status status);
    }
}
