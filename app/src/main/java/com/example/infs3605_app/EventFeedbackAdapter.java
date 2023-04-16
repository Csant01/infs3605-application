package com.example.infs3605_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class EventFeedbackAdapter extends RecyclerView.Adapter<EventFeedbackAdapter.ViewHolder> {
    Context context;
    List<FeedbackAverage> allFeedback;
    DatabaseConnector db;

    public EventFeedbackAdapter(Context context, List<FeedbackAverage> allFeedback) {
        this.context = context;
        this.allFeedback = allFeedback;
    }

    @NonNull
    @Override
    public EventFeedbackAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.event_feedback_list_row, parent, false);
        return new EventFeedbackAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventFeedbackAdapter.ViewHolder holder, int position) {
        FeedbackAverage average = allFeedback.get(position);
        db = new DatabaseConnector(context);
        List<String> comments = db.getFeedbackComments(average.getEventName());
        holder.eventName.setText(average.getEventName());
        holder.satisfaction.setText(String.valueOf(average.getQ1()));
        holder.likeliness.setText(String.valueOf(average.getQ2()));
        holder.usefulness.setText(String.valueOf(average.getQ3()));
        holder.rating.setText(String.valueOf(average.getQ4()));
        FeedbackCommentsAdapter adapter = new FeedbackCommentsAdapter(context, comments);
        holder.comments.setLayoutManager(new LinearLayoutManager(context));
        holder.comments.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return allFeedback.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView eventName, satisfaction, likeliness, usefulness, rating;
        RecyclerView comments;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.feedbackEventNamePrint);
            satisfaction = itemView.findViewById(R.id.satisfactionAveragePrint);
            likeliness = itemView.findViewById(R.id.likelihoodOfReattendancePrint);
            usefulness = itemView.findViewById(R.id.usefullnessRatingPrint);
            rating = itemView.findViewById(R.id.eventRatingPrint);
            comments = itemView.findViewById(R.id.commentsRv);
        }
    }
}
