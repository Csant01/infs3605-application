package com.example.infs3605_app;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class StaffOwnEventsAdapter extends RecyclerView.Adapter<StaffOwnEventsAdapter.ViewHolder> {
    private Context context;
    private List<Event> allEvents;
    DatabaseConnector db;
    private StaffOwnEventsInterface eventClickListener;

    public StaffOwnEventsAdapter(Context context, List<Event> allEvents, StaffOwnEventsInterface eventClickListener) {
        this.context = context;
        this.allEvents = allEvents;
        this.eventClickListener = eventClickListener;
    }

    @NonNull
    @Override
    public StaffOwnEventsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.organiser_own_events_list_row, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull StaffOwnEventsAdapter.ViewHolder holder, int position) {
        db = new DatabaseConnector(context);
        holder.eventName.setText(allEvents.get(position).getEventName());
        holder.eventDate.setText(formatEpoch(allEvents.get(position).getEventDate()));
        holder.eventFac.setText(db.getUserName(allEvents.get(position).getEventFacility()));
        ArrayList<User> participants = db.getParticipants(allEvents.get(position).getEventId());
        holder.eventRsvp.setText(String.valueOf(participants.size()));

    }

    @Override
    public int getItemCount() {
        return allEvents.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView eventName, eventDate, eventFac, eventRsvp;
        Button viewParticipants;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.ownEventsEventName);
            eventDate = itemView.findViewById(R.id.ownEventsDate);
            eventFac = itemView.findViewById(R.id.ownEventsFacility);
            eventRsvp = itemView.findViewById(R.id.ownEventsParticipantNumberPrint);
            viewParticipants = itemView.findViewById(R.id.viewParticipantsButton);

            viewParticipants.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ViewParticipantsActivity.class);
                    intent.putExtra("ORGANISER_NAME", allEvents.get(getAdapterPosition()).getEventOwner());
                    intent.putExtra("EVENT_ID", allEvents.get(getAdapterPosition()).getEventId());
                    intent.putExtra("USER_TYPE", "organiser");
                    intent.putExtra("PAGE", "OrganiserOwnEvents");
                    context.startActivity(intent);
                }
            });
            itemView.setOnClickListener(this);

        }
        @Override
        public void onClick(View v) {
            eventClickListener.onEventClick(getAdapterPosition());
        }
    }

    public interface StaffOwnEventsInterface {
        void onEventClick (int position);
    }

    public String formatEpoch (long value) {
        Instant instant = Instant.ofEpochMilli(value);
        LocalDate date = instant.atZone(ZoneId.systemDefault()).toLocalDate();
        String formattedDate = date.format(DateTimeFormatter.ofPattern("d' 'MMM' 'uuuu"));
        String finalDate = formattedDate;
        return finalDate;
    }
}
