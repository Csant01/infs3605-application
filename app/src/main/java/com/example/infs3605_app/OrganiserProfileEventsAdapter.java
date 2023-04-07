package com.example.infs3605_app;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class OrganiserProfileEventsAdapter extends RecyclerView.Adapter<OrganiserProfileEventsAdapter.ViewHolder> {
    List<Event> alLEvents;
    Context context;
    OnOrganiserEventClickListener eventClickListener;
    ImageButton saveEvent;

    public OrganiserProfileEventsAdapter(Context context, List<Event> alLEvents, OnOrganiserEventClickListener eventClickListener) {
        this.alLEvents = alLEvents;
        this.context = context;
        this.eventClickListener = eventClickListener;
    }

    @NonNull
    @Override
    public OrganiserProfileEventsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull OrganiserProfileEventsAdapter.ViewHolder holder, int position) {
        Event event = alLEvents.get(position);
        holder.eventName.setText(String.valueOf(event.getEventName()));
        holder.eventDate.setText(formatEpoch(event.getEventDate()));
        holder.eventOrg.setText(String.valueOf(event.getEventOwner()));
    }

    @Override
    public int getItemCount() {
        return alLEvents.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView saveEvent;
        TextView eventName, eventDate, eventOrg;
        OnOrganiserEventClickListener eventClickListener;
        public ViewHolder(@NonNull View itemView, OnOrganiserEventClickListener eventClickListener) {
            super(itemView);
            eventName = itemView.findViewById(R.id.allEventsNameText);
            eventDate = itemView.findViewById(R.id.allEventsDate);
            eventOrg = itemView.findViewById(R.id.allOrganisersEventText);
            saveEvent = itemView.findViewById(R.id.allEventsSaveButton);
            this.eventClickListener = eventClickListener;

            itemView.setOnClickListener(this);
            saveEvent.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            eventClickListener.onEventClick(getAdapterPosition());
            eventClickListener.onSaveClick(getAdapterPosition());
        }
    }

    public interface OnOrganiserEventClickListener {
        void onEventClick (int position);
        void onSaveClick (int position);

    }

    public String formatEpoch (long value) {
        String date = new SimpleDateFormat("dd/MM/yyyy").format(new Date(value));
        return date;
    }
}
