package com.example.infs3605_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FeedbackCommentsAdapter extends RecyclerView.Adapter<FeedbackCommentsAdapter.ViewHolder> {
    Context context;
    List<String> comments;

    public FeedbackCommentsAdapter(Context context, List<String> comments) {
        this.context = context;
        this.comments = comments;
    }

    @NonNull
    @Override
    public FeedbackCommentsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.additional_comments_list_row, parent, false);
        return new FeedbackCommentsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedbackCommentsAdapter.ViewHolder holder, int position) {
        holder.additionalComments.setText(comments.get(position));
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView additionalComments;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            additionalComments = itemView.findViewById(R.id.additionalComments);
        }
    }
}
