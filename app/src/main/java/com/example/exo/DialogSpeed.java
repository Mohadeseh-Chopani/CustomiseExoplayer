package com.example.exo;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.exo.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class DialogSpeed extends DialogFragment {
    RadioButton radio_50, radio_75, radio_1, radio_125, radio_150;
    ListenerSpeed listenerSpeed;

    RadioGroup radioGroup;
    Button confirm;

    enum Status {
        SPEED50,
        SPEED75,
        SPEED1,
        SPEED125,
        SPEED150
    }
    Status status ;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listenerSpeed = (ListenerSpeed) context;
    }

    @SuppressLint("MissingInflatedId")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_speed, null, false);
        builder.setView(view);


        radio_50 = view.findViewById(R.id.radio_50);
        radio_75 = view.findViewById(R.id.radio_75);
        radio_1 = view.findViewById(R.id.radio_normal);
        radio_125 = view.findViewById(R.id.radio_125);
        radio_150 = view.findViewById(R.id.radio_150);
        radioGroup = view.findViewById(R.id.radioGroup);
        confirm = view.findViewById(R.id.confirm_speed);

        switch ((int) ((MainActivity.currentSpeed) * 100)){
            case 1:
                radio_1.setChecked(true);
                break;
            case 50:
                radio_50.setChecked(true);
                break;
            case 75:
                radio_75.setChecked(true);
                break;
            case 125:
                radio_125.setChecked(true);
                break;
            case 150:
                radio_150.setChecked(true);
                break;
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radio_50) {
                    radio_50.setBackgroundColor(R.drawable.shape_item_checked);
                    status = Status.SPEED50;
                } else if (checkedId == R.id.radio_normal) {
                    radio_1.setBackgroundColor(R.drawable.shape_item_checked);
                    status = Status.SPEED1;
                } else if (checkedId == R.id.radio_75) {
                    radio_75.setBackgroundColor(R.drawable.shape_item_checked);
                    status = Status.SPEED75;
                } else if (checkedId == R.id.radio_125) {
                    radio_125.setBackgroundColor(R.drawable.shape_item_checked);
                    status = Status.SPEED125;
                } else if (checkedId == R.id.radio_150) {
                    radio_150.setBackgroundColor(R.drawable.shape_item_checked);
                    status = Status.SPEED150;
                }
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status == Status.SPEED1) {
                    listenerSpeed.itemClick(1.0f);
                }
                else if (status == Status.SPEED50) {
                    listenerSpeed.itemClick(0.5f);
                }
                else if (status == Status.SPEED75) {
                    listenerSpeed.itemClick(0.75f);
                }
                else if (status == Status.SPEED125) {
                    listenerSpeed.itemClick(1.25f);
                }
                else if (status == Status.SPEED150) {
                    listenerSpeed.itemClick(1.5f);
                }

                dismiss();
            }
        });

        return builder.create();
    }
    interface ListenerSpeed {
        void itemClick(Float speed);
    }
}
