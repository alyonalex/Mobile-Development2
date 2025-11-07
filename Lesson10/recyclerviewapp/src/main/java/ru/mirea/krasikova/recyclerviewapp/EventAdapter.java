package ru.mirea.krasikova.recyclerviewapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private final List<HistoricalEvent> events;

    public EventAdapter(List<HistoricalEvent> events) {
        this.events = events;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_event, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        HistoricalEvent event = events.get(position);
        holder.textTitle.setText(event.getTitle());
        holder.textDescription.setText(event.getDescription());
        holder.imageEvent.setImageResource(event.getImageResId());
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        ImageView imageEvent;
        TextView textTitle, textDescription;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            imageEvent = itemView.findViewById(R.id.imageEvent);
            textTitle = itemView.findViewById(R.id.textTitle);
            textDescription = itemView.findViewById(R.id.textDescription);
        }
    }
}
