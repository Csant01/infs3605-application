package com.example.infs3605_app;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
    DatabaseConnector db;
    private static final String TAG = "OrganiserProfileEventsAdapter";

    public OrganiserProfileEventsAdapter(Context context, List<Event> alLEvents, OnOrganiserEventClickListener eventClickListener) {
        this.alLEvents = alLEvents;
        this.context = context;
        this.eventClickListener = eventClickListener;
    }

    @NonNull
    @Override
    public OrganiserProfileEventsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.student_all_events_list_row, parent, false);
        return new ViewHolder(view, eventClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull OrganiserProfileEventsAdapter.ViewHolder holder, int position) {
        Event event = alLEvents.get(position);
        db = new DatabaseConnector(context);
        holder.eventName.setText(String.valueOf(event.getEventName()));
        holder.eventDate.setText(formatEpoch(event.getEventDate()));
        holder.eventOrg.setText(db.getUserName(event.getEventOwner()));
        if (db.checkUserGoing(User.currentlyLoggedIn.get(User.currentlyLoggedIn.size()-1), alLEvents.get(position).getEventId())) {
            holder.saveEvent.setImageResource(R.drawable.ic_filled_bookmark);
        } else {
            holder.saveEvent.setImageResource(R.drawable.ic_bookmark);
        }
    }

    @Override
    public int getItemCount() {
        return alLEvents.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageButton saveEvent;
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
            saveEvent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    db = new DatabaseConnector(context);
                    int userGoing = db.setUserGoing(User.currentlyLoggedIn.get(User.currentlyLoggedIn.size()-1),
                            alLEvents.get(getAdapterPosition()).getEventId());
                    if (userGoing == 0) {
                        Toast.makeText(context, "You are now RSVP'ed for " + alLEvents.get(getAdapterPosition()).getEventName(),
                                Toast.LENGTH_SHORT).show();
                        saveEvent.setImageResource(R.drawable.ic_filled_bookmark);
                    } else if (userGoing == 1) {
                        Toast.makeText(context, "The date has already passed for " + alLEvents.get(getAdapterPosition()).getEventName(),
                                Toast.LENGTH_SHORT).show();
                    } else {
                        saveEvent.setImageResource(R.drawable.ic_bookmark);
                        Toast.makeText(context, "You are no longer RSVP'ed for " + alLEvents.get(getAdapterPosition()).getEventName(),
                                Toast.LENGTH_SHORT).show();
                    }

                    Log.d(TAG, "onClick:" + userGoing);
                }
            });

        }

        @Override
        public void onClick(View view) {
            eventClickListener.onEventClick(getAdapterPosition());
        }
    }

    public interface OnOrganiserEventClickListener {
        void onEventClick (int position);

    }

    public String formatEpoch (long value) {
        String date = new SimpleDateFormat("dd/MM/yyyy").format(new Date(value));
        return date;
    }
}
