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
import com.example.exo.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterLanguage extends RecyclerView.Adapter<AdapterLanguage.ViewHolder> {

    public List<String> listName = new ArrayList<>();
    public List<String> listId = new ArrayList<>();
    setLanguage setLanguage;
    Context context;

    public AdapterLanguage(List<String> listName, List<String> listId, Context context, setLanguage setLanguage) {
        this.listName = listName;
        this.listId = listId;
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
        holder.bind(listName.get(position), listId.get(position));
    }

    @Override
    public int getItemCount() {
        return (listName.size());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RadioButton radioButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            radioButton = itemView.findViewById(R.id.btn_item);
        }

        void bind(String nameLanguage, String idLanguage) {
            radioButton.setText(nameLanguage);
            if (radioButton.getText().toString().equals(listName.get(listId.indexOf(MainActivity.statusLanguage)))) {
                radioButton.setChecked(true);
            }

            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    radioButton.setChecked(true);
                    setLanguage.clickItemToChooseLan(idLanguage);
                }
            });
        }
    }

    public interface setLanguage {
        void clickItemToChooseLan(String status);
    }
}
