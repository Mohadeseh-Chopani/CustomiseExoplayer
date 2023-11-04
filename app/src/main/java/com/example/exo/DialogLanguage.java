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

public class DialogLanguage extends DialogFragment {

    RadioGroup radioGroup_language;
    RadioButton radioButton_persian, radioButton_english;


    setLanguage setLanguage;
    int status;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        setLanguage = (setLanguage) context;
    }

    @SuppressLint("MissingInflatedId")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_language, null, false);
        builder.setView(view);


        radioGroup_language = view.findViewById(R.id.radioGroup_lan);
        radioButton_english = view.findViewById(R.id.btn_english_lan);
        radioButton_persian = view.findViewById(R.id.btn_persian_lan);


        switch (Language.currentLanguage){
            case 0:
                radioButton_english.setChecked(true);
                break;
            case 1:
                radioButton_persian.setChecked(true);
                break;
        }

        radioGroup_language.setOnCheckedChangeListener((radioGroup, item) -> {
            if (item == R.id.btn_english_lan) {
                radioButton_english.setChecked(true);
                status = 0;
                setLanguage.clickItemToChooseLan(status);
            } else if (item == R.id.btn_persian_lan) {
                radioButton_persian.setChecked(true);
                status = 1;
                setLanguage.clickItemToChooseLan(status);
            }
            dismiss();
        });

        return builder.create();
    }

    interface setLanguage {
        void clickItemToChooseLan(int status);
    }
}
