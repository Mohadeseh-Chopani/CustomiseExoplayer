package com.example.exo.Adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exo.MainActivity;
import com.example.exo.Model.DataPlayer;
import com.example.exo.R;
import com.google.android.exoplayer2.Format;

import java.util.ArrayList;
import java.util.List;

public class AdapterLanguage extends RecyclerView.Adapter<AdapterLanguage.ViewHolder> {

    public List<Format> list = new ArrayList<>();
    private List<RadioButton> radioButtonList = new ArrayList<>();
    setLanguage setLanguage;
    DataPlayer dataPlayer = new DataPlayer();
    int previousPosition = dataPlayer.statusLanguage;
    Context context;

    public AdapterLanguage(List<Format> list, Context context, setLanguage setLanguage) {
        this.list=list;
        this.context = context;
        this.setLanguage = setLanguage;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.items, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return (list.size());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RadioButton radioButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            radioButton = itemView.findViewById(R.id.btn_item);
        }

        void bind(Format format) {
            radioButton.setText(format.id);
            radioButtonList.add(radioButton);
            String index = format.language;
            if (index.equals(MainActivity.statusLanguage)) {
                radioButton.setChecked(true);
            }

            for (int i = 0; i < radioButtonList.size(); i++) {
                if (radioButtonList.get(i).isChecked()){
                    previousPosition = i;
                }
            }

            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int currentPosition = getAdapterPosition();

                    if (previousPosition != -1 && previousPosition < radioButtonList.size()) {
                        radioButtonList.get(previousPosition).setChecked(false);
                    }
                    radioButton.setChecked(true);
                    previousPosition = currentPosition;
                    dataPlayer.statusLanguage = previousPosition;
                    setLanguage.clickItemToChooseLan(format.language);
                }
            });
        }
    }

    public interface setLanguage {
        void clickItemToChooseLan(String status);
    }
}
