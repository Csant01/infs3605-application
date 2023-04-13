package com.example.infs3605_app;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StudentSavedEventsAdapter extends RecyclerView.Adapter<StudentSavedEventsAdapter.ViewHolder> implements Filterable {
    private Context context;
    private List<Event> allEvents;
    private List<Event> filteredEvents;
    DatabaseConnector db;
    private OnSavedEventClickListener eventClickListener;

    public StudentSavedEventsAdapter(Context context, List<Event> allEvents, OnSavedEventClickListener eventClickListener) {
        this.context = context;
        this.allEvents = allEvents;
        filteredEvents = allEvents;
        this.eventClickListener = eventClickListener;
    }

    @NonNull
    @Override
    public StudentSavedEventsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.student_saved_events_list_row, parent, false);
        return new ViewHolder(view, eventClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentSavedEventsAdapter.ViewHolder holder, int position) {
        Event event = filteredEvents.get(position);
        holder.eventName.setText(String.valueOf(event.getEventName()));
        holder.eventDate.setText(formatEpoch(event.getEventDate()));
        holder.eventOrg.setText(String.valueOf(event.getEventOwner()));
        if (event.getEventImage() != null) {
            holder.eventImage.setImageBitmap(ImageUtils.getImage(event.getEventImage()));
        } else {
            holder.eventImage.setImageResource(R.drawable.unsw_unite_logo);
        }
        holder.eventCity.setText(event.getEventCity());
        db = new DatabaseConnector(context);
        if (db.checkUserGoing(User.currentlyLoggedIn.get(User.currentlyLoggedIn.size()-1), filteredEvents.get(position).getEventId())) {
            holder.unsaveButton.setImageResource(R.drawable.ic_filled_bookmark);
        } else {
            holder.unsaveButton.setImageResource(R.drawable.ic_bookmark);
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
        ImageView eventImage;
        TextView eventName, eventDate, eventOrg, eventCity;
        ImageButton unsaveButton;
        OnSavedEventClickListener eventClickListener;
        public ViewHolder(@NonNull View itemView, OnSavedEventClickListener eventClickListener) {
            super(itemView);
            eventImage = itemView.findViewById(R.id.savedEventsImage);
            eventDate = itemView.findViewById(R.id.savedEventsDate);
            eventName = itemView.findViewById(R.id.savedEventsName);
            eventOrg = itemView.findViewById(R.id.savedOrganisersEvent);
            eventCity = itemView.findViewById(R.id.savedEventsCity);
            unsaveButton = itemView.findViewById(R.id.unsaveButton);
            this.eventClickListener = eventClickListener;

            itemView.setOnClickListener(this);

            unsaveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    db = new DatabaseConnector(context);
                    int userGoing = db.setUserGoing(User.currentlyLoggedIn.get(User.currentlyLoggedIn.size()-1),
                            filteredEvents.get(getAdapterPosition()).getEventId());
                    if (userGoing == 0) {
                        Toast.makeText(context, "You are now RSVP'ed for " + filteredEvents.get(getAdapterPosition()).getEventName(),
                                Toast.LENGTH_SHORT).show();
                        unsaveButton.setImageResource(R.drawable.ic_filled_bookmark);
                    } else if (userGoing == 1) {
                        Toast.makeText(context, "The date has already passed for " + filteredEvents.get(getAdapterPosition()).getEventName(),
                                Toast.LENGTH_SHORT).show();
                    } else {
                        unsaveButton.setImageResource(R.drawable.ic_bookmark);
                        Toast.makeText(context, "You are no longer RSVP'ed for " + filteredEvents.get(getAdapterPosition()).getEventName(),
                                Toast.LENGTH_SHORT).show();
                    }

                }
            });

        }

        @Override
        public void onClick(View view) {
            eventClickListener.onEventClick(getAdapterPosition());
        }
    }

    public interface OnSavedEventClickListener {
        void onEventClick (int position);
    }

    public String formatEpoch (long value) {
        Instant instant = Instant.ofEpochMilli(value);
        LocalDate date = instant.atZone(ZoneId.systemDefault()).toLocalDate();
        String formattedDate = date.format(DateTimeFormatter.ofPattern("d' 'MMM' 'uuuu"));
        String daySuffix = StudentPastEventsAdapter.getDaySuffix(date.getDayOfMonth());
        String finalDate = formattedDate + daySuffix;
        return finalDate;
    }
}
