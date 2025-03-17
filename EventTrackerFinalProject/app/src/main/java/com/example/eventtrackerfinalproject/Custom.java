package com.example.eventtrackerfinalproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Custom extends RecyclerView.Adapter<Custom.MyViewHolder> {

    private Context context;
    private ArrayList<Reminder> reminders;
    private Activity activity;
    private OnItemClickListener listener;

    // Interface for item click events
    public interface OnItemClickListener {
        void onItemClick(int position);
        void onEditClick(int position);
    }

    public Custom(Activity activity, Context context, ArrayList<Reminder> reminders, OnItemClickListener listener) {
        this.activity = activity;
        this.context = context;
        this.reminders = reminders;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.event_table_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Reminder reminder = reminders.get(position);

        holder.id.setText(String.valueOf(reminder.getId()));
        holder.title.setText(reminder.getTitle());
        holder.description.setText(reminder.getDescription());
        holder.date.setText(reminder.getDate());
        holder.time.setText(reminder.getTime());

        // Set click listeners
        holder.layout.setOnClickListener(v -> listener.onItemClick(position));
        holder.itemView.findViewById(R.id.edit_button).setOnClickListener(v -> listener.onEditClick(position));
    }

    @Override
    public int getItemCount() {
        return reminders.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView id, title, description, date, time;
        LinearLayout layout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.event_id);
            title = itemView.findViewById(R.id.event_title);
            description = itemView.findViewById(R.id.event_description);
            date = itemView.findViewById(R.id.date_to_fire);
            time = itemView.findViewById(R.id.time_to_fire);
            layout = itemView.findViewById(R.id.mainLayout);
        }
    }
}
