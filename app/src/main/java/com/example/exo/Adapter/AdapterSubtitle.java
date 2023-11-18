package com.example.exo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exo.MainActivity;
import com.example.exo.R;
import com.google.android.exoplayer2.MediaItem;

import java.util.ArrayList;
import java.util.List;

public class AdapterSubtitle extends RecyclerView.Adapter<AdapterSubtitle.ViewHolder> {

    List<MediaItem.SubtitleConfiguration> list = new ArrayList<>();
    setSubtitle setSubtitle;
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
        holder.bind(list.get(position).language, list.get(position).label);
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

        void bind(String subtitleId, String subtitleName) {
            radioButton.setText(subtitleName);
            for (int i = 0; i < list.size(); i++) {
                String index = subtitleId;
                if (index.equals(MainActivity.statusSubtitle)) {
                    radioButton.setChecked(true);
                }
            }

            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    radioButton.setChecked(true);
                    setSubtitle.clickItemToChooseSub(subtitleId);
                }
            });
        }
    }

    public interface setSubtitle {
        void clickItemToChooseSub(String status);
    }
}
