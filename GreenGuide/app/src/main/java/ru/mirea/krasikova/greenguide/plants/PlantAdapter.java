package ru.mirea.krasikova.greenguide.plants;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import ru.mirea.krasikova.domain.model.Plant;
import ru.mirea.krasikova.greenguide.R;

public class PlantAdapter extends RecyclerView.Adapter<PlantAdapter.PlantViewHolder> {

    private final List<Plant> plants = new ArrayList<>();
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Plant plant);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void submitList(List<Plant> newPlants) {
        plants.clear();
        if (newPlants != null) {
            plants.addAll(newPlants);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PlantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_plant, parent, false);
        return new PlantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlantViewHolder holder, int position) {
        Plant plant = plants.get(position);
        holder.bind(plant);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(plant);
            }
        });
    }

    @Override
    public int getItemCount() {
        return plants.size();
    }

    static class PlantViewHolder extends RecyclerView.ViewHolder {
        private final ImageView plantImage;
        private final TextView plantName;

        public PlantViewHolder(@NonNull View itemView) {
            super(itemView);
            plantImage = itemView.findViewById(R.id.plantImage);
            plantName = itemView.findViewById(R.id.plantName);
        }

        public void bind(Plant plant) {
            plantName.setText(plant.getName());
            Glide.with(itemView.getContext())
                    .load(plant.getImageUrl())
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .into(plantImage);
        }
    }
}
