package com.example.infs3605_app;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StudentAllEventsAdapter extends RecyclerView.Adapter<StudentAllEventsAdapter.ViewHolder> implements Filterable {
    private Context context;
    private List<Event> filteredEvents;
    private List<Event> allEvents;
    private OnAllEventClickListener eventClickListener;
    DatabaseConnector db;
    private static final String TAG = "StudentAllEventsAdapter";

    public StudentAllEventsAdapter(Context context, List<Event> allEvents, OnAllEventClickListener eventClickListener) {
        this.context = context;
        this.allEvents = allEvents;
        filteredEvents = allEvents;
        this.eventClickListener = eventClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.student_all_events_list_row, parent, false);
        return new ViewHolder(view, eventClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Event event = filteredEvents.get(position);
        db = new DatabaseConnector(context);

        holder.eventName.setText(String.valueOf(event.getEventName()));
        holder.eventDate.setText(formatEpoch(event.getEventDate()));
        holder.eventOrg.setText(db.getUserName(event.getEventOwner()));
        if (db.checkUserGoing(User.currentlyLoggedIn.get(User.currentlyLoggedIn.size()-1), filteredEvents.get(position).getEventId())) {
            holder.saveEvent.setImageResource(R.drawable.ic_filled_bookmark);
        } else {
            holder.saveEvent.setImageResource(R.drawable.ic_bookmark);
        }

    }

    @Override
    public int getItemCount() {
        return filteredEvents.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String query = charSequence.toString();
                if (query.isEmpty()) {
                    filteredEvents = allEvents;
                } else {
                    List<Event> eventFilteredList = new ArrayList<>();
                    for (Event event: allEvents) {
                        if (event.getEventName().toLowerCase().startsWith(query.toLowerCase())) {
                            eventFilteredList.add(event);
                        }
                    }
                    filteredEvents = eventFilteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredEvents;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredEvents = (List<Event>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView eventName, eventDate, eventOrg;
        ImageButton saveEvent;
        OnAllEventClickListener eventClickListener;
        public ViewHolder(@NonNull View itemView, OnAllEventClickListener eventClickListener) {
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
                            filteredEvents.get(getAdapterPosition()).getEventId());
                    if (userGoing == 0) {
                        Toast.makeText(context, "You are now RSVP'ed for " + filteredEvents.get(getAdapterPosition()).getEventName(),
                                Toast.LENGTH_SHORT).show();
                        saveEvent.setImageResource(R.drawable.ic_filled_bookmark);
                    } else if (userGoing == 1) {
                        Toast.makeText(context, "The date has already passed for " + filteredEvents.get(getAdapterPosition()).getEventName(),
                                Toast.LENGTH_SHORT).show();
                    } else {
                        saveEvent.setImageResource(R.drawable.ic_bookmark);
                        Toast.makeText(context, "You are no longer RSVP'ed for " + filteredEvents.get(getAdapterPosition()).getEventName(),
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

    public interface OnAllEventClickListener {
        void onEventClick (int position);
    }

    public String formatEpoch (long value) {
        String date = new SimpleDateFormat("dd/MM/yyyy").format(new Date(value));
        return date;
    }
}