package com.example.exo.Speed;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.example.exo.MainActivity;
import com.example.exo.R;

public class SpeedDialog extends DialogFragment {
    RadioButton radio_50, radio_75, radio_1, radio_150, radio_200;
    ListenerSpeed listenerSpeed;
    Switch switchContinuePlaying;
    RadioGroup radioGroup;
    Button confirm;

    public static boolean statusSwitch = false;

    enum Status {
        SPEED50, SPEED75, SPEED1, SPEED150, SPEED200
    }

    Status status;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listenerSpeed = (ListenerSpeed) context;
    }

    @SuppressLint({"MissingInflatedId", "ResourceAsColor"})
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.MyCustomTheme);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_speed, null, false);
        builder.setView(view);


        radio_50 = view.findViewById(R.id.radio_50);
        radio_75 = view.findViewById(R.id.radio_75);
        radio_1 = view.findViewById(R.id.radio_normal);
        radio_150 = view.findViewById(R.id.radio_150);
        radio_200 = view.findViewById(R.id.radio_200);
        radioGroup = view.findViewById(R.id.radioGroup);
        confirm = view.findViewById(R.id.confirm_speed);
        switchContinuePlaying = view.findViewById(R.id.switch_continue);


        switchContinuePlaying.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    // Change the thumb tint color when the Switch is clicked
                    int color = ContextCompat.getColor(getContext(), R.color.PrimaryColor);
                    switchContinuePlaying.getThumbDrawable().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);

                }
                return false;
            }
        });

        switchContinuePlaying.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                statusSwitch = !statusSwitch;
                if (statusSwitch) {
                    int color = ContextCompat.getColor(getContext(), R.color.PrimaryColor);
                    switchContinuePlaying.getThumbDrawable().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
                } else {
                    int color = ContextCompat.getColor(getContext(), R.color.white);
                    switchContinuePlaying.getThumbDrawable().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
                }
            }
        });

        if (MainActivity.statusRepetition) {
            int color = ContextCompat.getColor(getContext(), R.color.PrimaryColor);
            switchContinuePlaying.getThumbDrawable().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
            switchContinuePlaying.setChecked(MainActivity.statusRepetition);
        }

        switch ((int) ((MainActivity.currentSpeed) * 100)) {
            case 1:
                radio_1.setChecked(true);
                break;
            case 50:
                radio_50.setChecked(true);
                break;
            case 75:
                radio_75.setChecked(true);
                break;
            case 150:
                radio_150.setChecked(true);
                break;
            case 200:
                radio_200.setChecked(true);
                break;
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radio_50) {
                    status = Status.SPEED50;
                } else if (checkedId == R.id.radio_normal) {
                    status = Status.SPEED1;
                } else if (checkedId == R.id.radio_75) {
                    status = Status.SPEED75;
                } else if (checkedId == R.id.radio_150) {
                    status = Status.SPEED150;
                } else if (checkedId == R.id.radio_200) {
                    status = Status.SPEED200;
                }
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status == Status.SPEED1) {
                    listenerSpeed.itemClick(1.0f);
                } else if (status == Status.SPEED50) {
                    listenerSpeed.itemClick(0.5f);
                } else if (status == Status.SPEED75) {
                    listenerSpeed.itemClick(0.75f);
                } else if (status == Status.SPEED150) {
                    listenerSpeed.itemClick(1.5f);
                } else if (status == Status.SPEED200) {
                    listenerSpeed.itemClick(2.0f);
                } else
                    listenerSpeed.itemClick(MainActivity.currentSpeed);

                dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.background_dialog);
        return alertDialog;
    }

    public interface ListenerSpeed {
        void itemClick(Float speed);
    }
}
