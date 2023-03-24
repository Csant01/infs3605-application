package com.example.infs3605_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class StudentAllEventsAdapter extends RecyclerView.Adapter<StudentAllEventsAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<String> eventName, eventDate, eventOrg;

    public StudentAllEventsAdapter(Context context, ArrayList<String> eventName, ArrayList<String> eventDate, ArrayList<String> eventOrg) {
        this.context = context;
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.eventOrg = eventOrg;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.student_all_events_list_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.eventName.setText(String.valueOf(eventName.get(position)));
        holder.eventDate.setText(String.valueOf(eventDate.get(position)));
        holder.eventOrg.setText(String.valueOf(eventOrg.get(position)));

    }

    @Override
    public int getItemCount() {
        return eventName.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView eventName, eventDate, eventOrg;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.eventNameText);
            eventDate = itemView.findViewById(R.id.eventDateText);
            eventOrg = itemView.findViewById(R.id.organisersEventText);
        }
    }
}