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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StudentPastEventsAdapter extends RecyclerView.Adapter<StudentPastEventsAdapter.ViewHolder> implements Filterable {
    private Context context;
    private List<Event> filteredEvents;
    private List<Event> allEvents;
    private OnPastEventClickListener eventClickListener;

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
        TextView eventName, eventDate, eventOrg;
        Button feedbackButton;
        OnPastEventClickListener eventClickListener;
        public ViewHolder(@NonNull View itemView, OnPastEventClickListener eventClickListener) {
            super(itemView);
            eventImage = itemView.findViewById(R.id.pastEventsImage);
            eventDate = itemView.findViewById(R.id.pastEventsDate);
            eventName = itemView.findViewById(R.id.pastEventsNameText);
            eventOrg = itemView.findViewById(R.id.pastOrganisersEventText);
            feedbackButton = itemView.findViewById(R.id.feedbackButton);
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
        String date = new SimpleDateFormat("dd/MM/yyyy").format(new Date(value));
        return date;
    }
}
