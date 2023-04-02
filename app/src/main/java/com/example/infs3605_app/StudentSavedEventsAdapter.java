package com.example.infs3605_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StudentSavedEventsAdapter extends RecyclerView.Adapter<StudentSavedEventsAdapter.ViewHolder> implements Filterable {
    private Context context;
    private List<Event> allEvents;
    private List<Event> filteredEvents;
    DatabaseConnector db;

    public StudentSavedEventsAdapter(Context context, List<Event> allEvents) {
        this.context = context;
        this.allEvents = allEvents;
        filteredEvents = allEvents;
    }

    @NonNull
    @Override
    public StudentSavedEventsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.student_saved_events_list_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentSavedEventsAdapter.ViewHolder holder, int position) {
        Event event = filteredEvents.get(position);
        holder.eventName.setText(String.valueOf(event.getEventName()));
        holder.eventDate.setText(formatEpoch(event.getEventDate()));
        holder.eventOrg.setText(String.valueOf(event.getEventOwner()));
        if (event.getEventCategory().equals("Network")) {
            holder.eventImage.setImageResource(R.drawable.ic_networking);
        } else if (event.getEventCategory().equals("Careers")) {
            holder.eventImage.setImageResource(R.drawable.ic_career);
        } else if (event.getEventCategory().equals("Social")) {
            holder.eventImage.setImageResource(R.drawable.ic_social);
        } else if (event.getEventCategory().equals("Travel")) {
            holder.eventImage.setImageResource(R.drawable.ic_travel);
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView eventImage;
        TextView eventName, eventDate, eventOrg;
        Button unsaveButton, rsvpButton, detailsButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            eventImage = itemView.findViewById(R.id.savedEventsImage);
            eventDate = itemView.findViewById(R.id.savedEventsDate);
            eventName = itemView.findViewById(R.id.savedEventsName);
            eventOrg = itemView.findViewById(R.id.savedOrganisersEvent);
            unsaveButton = itemView.findViewById(R.id.unsaveButton);
            rsvpButton = itemView.findViewById(R.id.rsvpButton);
            detailsButton = itemView.findViewById(R.id.eventDetailButton);

            unsaveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    db = new DatabaseConnector(view.getContext());
                    // method here to unsave an event;
                    Toast.makeText(view.getContext(), eventName.getText().toString() + " unsaved.",
                            Toast.LENGTH_SHORT).show();

                }
            });

            rsvpButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    db = new DatabaseConnector(view.getContext());
                    // method here to rsvp an event;
                    Toast.makeText(view.getContext(), eventName.getText().toString() + " rsvp.",
                            Toast.LENGTH_SHORT).show();
                }
            });

            detailsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // code here to switch pages.
                }
            });
        }
    }

    public String formatEpoch (long value) {
        String date = new SimpleDateFormat("dd/MM/yyyy").format(new Date(value));
        return date;
    }
}
