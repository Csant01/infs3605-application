package com.example.infs3605_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

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

    public StudentAllEventsAdapter(Context context, List<Event> allEvents) {
        this.context = context;
        this.allEvents = allEvents;
        filteredEvents = allEvents;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.student_all_events_list_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Event event = filteredEvents.get(position);
        holder.eventName.setText(String.valueOf(event.getEventName()));
        holder.eventDate.setText(formatEpoch(event.getEventEndDate()));
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

//    private Filter filteredData = new Filter() {
//        @Override
//        protected FilterResults performFiltering(CharSequence constraint) {
//            Log.d("Search Query", constraint.toString());
//            String query = constraint.toString();
//            if (query.isEmpty()) {
//                Log.d("Search Query", "Adding all entries");
//                eventList = eventListFull;
//                    Log.d("Search Query", "eventList size - " + eventList.size());
//                    Log.d("Search Query", "eventListFull size - " + eventListFull.size());
//            } else {
//                List<Event> eventFilteredList = new ArrayList<>();
//                for (Event event: eventListFull) {
//                    if (event.getEventName().startsWith(query.toLowerCase().trim())) {
//                        eventFilteredList.add(event);
//                    }
//                }
//                eventList = (ArrayList) eventFilteredList;
//            }
//            FilterResults results = new FilterResults();
//            results.values = eventList;
//            return results;

//            ArrayList<Event> filteredEventList = new ArrayList<>();
////            Log.d("Search Query", constraint.toString());
//            if (constraint == null || constraint.length() == 0) {
//                Log.d("Search Query", "Adding all entries");
//                filteredEventList.addAll(eventListFull);
//            } else {
//                String filterString = constraint.toString().toLowerCase().trim();
//                Log.d("Search Query", filterString);
//                Log.d("Search Query", "eventList size - " + eventList.size());
//                Log.d("Search Query", "eventListFull size - " + eventListFull.size());
//
//                for (Event event : eventListFull) {
//                    Log.d("Search Query", "Event Name - " + event.getEventName());
//                    if (event.getEventName().toLowerCase().startsWith(filterString)) {
//                        Log.d("Filtered Event Name", event.getEventName());
//                        filteredEventList.add(event);
//                    }
//                }
//            }
//
//            FilterResults results = new FilterResults();
//            results.values = filteredEventList;
//
//            return results;
//        }
//
//        @Override
//        protected void publishResults(CharSequence constraint, FilterResults results) {
//            eventList.clear();
//            eventList.addAll((ArrayList)results.values);
//            notifyDataSetChanged();
//        }
//    };

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView eventName, eventDate, eventOrg;
        ImageView eventImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.eventNameText);
            eventDate = itemView.findViewById(R.id.eventDateText);
            eventOrg = itemView.findViewById(R.id.organisersEventText);
            eventImage = itemView.findViewById(R.id.allEventsImage);
        }
    }

    public String formatEpoch (long value) {
        String date = new SimpleDateFormat("dd/MM/yyyy").format(new Date(value*1000));
        return date;

    }
}