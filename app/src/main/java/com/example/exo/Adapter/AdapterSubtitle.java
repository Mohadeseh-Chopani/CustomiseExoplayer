package com.example.exo.Adapter;

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
import com.google.android.exoplayer2.MediaItem;

import java.util.ArrayList;
import java.util.List;

public class AdapterSubtitle extends RecyclerView.Adapter<AdapterSubtitle.ViewHolder> {

    List<MediaItem.SubtitleConfiguration> list = new ArrayList<>();
    setSubtitle setSubtitle;
    private List<RadioButton> radioButtonList = new ArrayList<>();
    DataPlayer data_player = new DataPlayer();
    int previousPosition = data_player.statusSubtitle;
    Context context;
    public AdapterSubtitle(List<MediaItem.SubtitleConfiguration> list, Context context, setSubtitle setSubtitle) {
        this.list = list;
        this.context = context;
        this.setSubtitle = setSubtitle;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AdapterSubtitle.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.items, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RadioButton radioButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            radioButton = itemView.findViewById(R.id.btn_item);
        }

        void bind(MediaItem.SubtitleConfiguration dataPlayer) {

            radioButton.setText(dataPlayer.label);
            radioButtonList.add(radioButton);
            String index = dataPlayer.language;
                if (index.equals(MainActivity.statusSubtitle)) {
                    radioButton.setChecked(true);
                }

            for (int i = 0; i < radioButtonList.size(); i++) {
                if (radioButtonList.get(i).isChecked())
                    previousPosition = i;
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
                    data_player.statusSubtitle = previousPosition;
                    setSubtitle.clickItemToChooseSub(dataPlayer.language);
                }
            });
        }
    }

    public interface setSubtitle {
        void clickItemToChooseSub(String status);
    }
}
