package com.example.exo;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
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
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_subtitle, null, false);
        builder.setView(view);

        radioGroup_subtitle = view.findViewById(R.id.radioGroup_sub);
        radioButton_off = view.findViewById(R.id.btn_off_sub);
        radioButton_persian = view.findViewById(R.id.btn_persian_sub);


        radioGroup_subtitle.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int item) {
                if (item == R.id.btn_off_sub) {
                    radioButton_off.setBackgroundColor(R.drawable.shape_item_checked);
                    radioButton_off.setChecked(true);
                    status = Status.OFF;
                    setSubtitle.clickItemToChooseSub(status);
                } else if (item == R.id.btn_persian_sub) {
                    radioButton_persian.setBackgroundColor(R.drawable.shape_item_checked);
                    radioButton_persian.setChecked(true);
                    status = Status.PERSIAN;
                    setSubtitle.clickItemToChooseSub(status);
                }
                dismiss();
            }
        });

        return builder.create();
    }

    interface setSubtitle {
        void clickItemToChooseSub(Status status);
    }
}
