package com.example.infs3605_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AdminEventApprovalAdapter extends RecyclerView.Adapter<AdminEventApprovalAdapter.ViewHolder> {
    private Context context;
    private List<Event> allEvents;
    DatabaseConnector db;
    private static final String TAG = "AdminEventApprovalAdapter";
    EventApprovalInterface eventClickListener;

    public AdminEventApprovalAdapter(Context context, List<Event> allEvents, EventApprovalInterface eventClickListener) {
        this.context = context;
        this.allEvents = allEvents;
        this.eventClickListener = eventClickListener;
    }

    @NonNull
    @Override
    public AdminEventApprovalAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.admin_event_approvals_list_row, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminEventApprovalAdapter.ViewHolder holder, int position) {
        Event event = allEvents.get(position);
        db = new DatabaseConnector(context);
        holder.eventName.setText(event.getEventName());
        holder.eventDate.setText(formatEpoch(event.getEventDate()));
        holder.eventOrg.setText(db.getUserName(event.getEventOwner()));
        if (event.getEventIsApproved() == -1) {
            holder.statusImage.setImageResource(R.drawable.ic_copyright);
        } else if (event.getEventIsApproved() == 0) {
            holder.statusImage.setImageResource(R.drawable.ic_about);
        } else {
            holder.statusImage.setImageResource(R.drawable.ic_about_yellow);
        }

    }

    @Override
    public int getItemCount() {
        return allEvents.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView eventName, eventDate, eventOrg;
        ImageView statusImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.approvalEventNamePrint);
            eventDate = itemView.findViewById(R.id.approvalEventsDatePrint);
            eventOrg = itemView.findViewById(R.id.approvalEventsOrganiserPrint);
            statusImage = itemView.findViewById(R.id.eventApprovalImageView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            eventClickListener.onEventClick(getAdapterPosition());
        }
    }

    public interface EventApprovalInterface {
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
