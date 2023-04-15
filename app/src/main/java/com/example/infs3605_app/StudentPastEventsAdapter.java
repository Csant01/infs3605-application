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

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StudentPastEventsAdapter extends RecyclerView.Adapter<StudentPastEventsAdapter.ViewHolder> implements Filterable {
    private Context context;
    private List<Event> filteredEvents;
    private List<Event> allEvents;
    private OnPastEventClickListener eventClickListener;
    private DatabaseConnector db;


    public StudentPastEventsAdapter(Context context, List<Event> allEvents, OnPastEventClickListener eventClickListener) {
        this.context = context;
        this.allEvents = allEvents;
        filteredEvents = allEvents;
        this.eventClickListener = eventClickListener;
    }

    @NonNull
    @Override
    public StudentPastEventsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.student_past_events_list_row, parent, false);
        return new ViewHolder(view, eventClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentPastEventsAdapter.ViewHolder holder, int position) {
        Event event = filteredEvents.get(position);
        db = new DatabaseConnector(context);

        holder.eventName.setText(String.valueOf(event.getEventName()));
        holder.eventDate.setText(formatEpoch(event.getEventDate()));
        holder.eventOrg.setText(db.getUserName(event.getEventOwner()));
        if (event.getEventImage() != null) {
            holder.eventImage.setImageBitmap(ImageUtils.getImage(event.getEventImage()));
        } else {
            holder.eventImage.setImageResource(R.drawable.unsw_unite_logo);
        }
        holder.eventCity.setText(event.getEventCity());

        ArrayList<UserEvent> userEvents = db.getUserEvents(User.currentlyLoggedIn.get(User.currentlyLoggedIn.size()-1));
        for (int i = 0; i < userEvents.size(); i++) {
            if (userEvents.get(i).getEventId().equals(event.getEventId())) {
                if (userEvents.get(i).getFeedbackCompleted() != 1) {
                    holder.feedbackPending.setText("Feedback Pending");
                    holder.feedbackButton.setText("Complete Feedback");
                } else {
                    holder.feedbackPending.setText("Feedback Completed");
                    holder.feedbackButton.setText("Feedback Completed");
                }
            }
        }




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

    @Override
    public int getItemCount() {
        return filteredEvents.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView eventImage;
        TextView eventName, eventDate, eventOrg, eventCity, feedbackPending;
        Button feedbackButton;
        OnPastEventClickListener eventClickListener;
        public ViewHolder(@NonNull View itemView, OnPastEventClickListener eventClickListener) {
            super(itemView);
            eventImage = itemView.findViewById(R.id.pastEventsImage);
            eventDate = itemView.findViewById(R.id.pastEventsDate);
            eventName = itemView.findViewById(R.id.pastEventsNameText);
            eventOrg = itemView.findViewById(R.id.pastOrganisersEventText);
            eventCity = itemView.findViewById(R.id.pastEventCity);
            feedbackButton = itemView.findViewById(R.id.feedbackButton);
            feedbackPending = itemView.findViewById(R.id.feedbackStatusLabel);
            this.eventClickListener = eventClickListener;

            itemView.setOnClickListener(this);

            feedbackButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(view.getContext(), "Opening feedback page.",
                            Toast.LENGTH_SHORT).show();
                }
            });



        }

        @Override
        public void onClick(View view) {
            eventClickListener.onEventClick(getAdapterPosition());
        }
    }

    public interface OnPastEventClickListener {
        void onEventClick (int position);
    }

    public String formatEpoch (long value) {
        Instant instant = Instant.ofEpochMilli(value);
        LocalDate date = instant.atZone(ZoneId.systemDefault()).toLocalDate();
        String formattedDate = date.format(DateTimeFormatter.ofPattern("d' 'MMM' 'uuuu"));
        String daySuffix = getDaySuffix(date.getDayOfMonth());
        String finalDate = formattedDate + daySuffix;
        return finalDate;
    }

    public static String getDaySuffix(int day) {
        switch (day % 10) {
            case 1:
                if (day != 11) {
                    return "st";
                }
                break;
            case 2:
                if (day != 12) {
                    return "nd";
                }
                break;
            case 3:
                if (day != 13) {
                    return "rd";
                }
                break;
            default:
                break;
        }
        return "th";
    }
}
