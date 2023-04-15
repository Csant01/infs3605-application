package com.example.infs3605_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class StaffEventsAdapter extends RecyclerView.Adapter<StaffEventsAdapter.ViewHolder> {
    private Context context;
    private List<Event> allEvents;
    DatabaseConnector db;
    private StaffAllEventsInterface eventClickListener;

    public StaffEventsAdapter(Context context, List<Event> allEvents, StaffAllEventsInterface eventClickListener) {
        this.context = context;
        this.allEvents = allEvents;
        this.eventClickListener = eventClickListener;
    }

    @NonNull
    @Override
    public StaffEventsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.student_all_events_list_row, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull StaffEventsAdapter.ViewHolder holder, int position) {
        db = new DatabaseConnector(context);
        holder.eventName.setText(allEvents.get(position).getEventName());
        holder.eventDate.setText(formatEpoch(allEvents.get(position).getEventDate()));
        holder.eventOrg.setText(db.getUserName(allEvents.get(position).getEventOwner()));
        holder.saveEvent.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return allEvents.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView eventName, eventDate, eventOrg;
        ImageButton saveEvent;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.allEventsNameText);
            eventDate = itemView.findViewById(R.id.allEventsDate);
            eventOrg = itemView.findViewById(R.id.allOrganisersEventText);
            saveEvent = itemView.findViewById(R.id.allEventsSaveButton);
            itemView.setOnClickListener(this);

        }
        @Override
        public void onClick(View v) {
            eventClickListener.onEventClick(getAdapterPosition());
        }
    }

    public interface StaffAllEventsInterface {
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
