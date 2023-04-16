package com.example.infs3605_app;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Random;

public class ViewParticipantsAdapter extends RecyclerView.Adapter<ViewParticipantsAdapter.ViewHolder> {
    Context context;
    private List<User> allUsers;
    DatabaseConnector db;
    private static final String TAG = "ViewParticipantsAdapter";

    public ViewParticipantsAdapter(Context context, List<User> allUsers) {
        this.context = context;
        this.allUsers = allUsers;
    }

    @NonNull
    @Override
    public ViewParticipantsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_participants_list_row, parent, false);
        return new ViewParticipantsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewParticipantsAdapter.ViewHolder holder, int position) {
        holder.studentName.setText(allUsers.get(position).getUserName());
        holder.studentEmail.setText(allUsers.get(position).getUserEmail());
        holder.studentLinkedIn.setText("LinkedIn");
    }

    @Override
    public int getItemCount() {
        return allUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView studentName, studentEmail, studentLinkedIn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Random rand = new Random();
            studentName = itemView.findViewById(R.id.studentParticipantName);
            studentEmail = itemView.findViewById(R.id.studentParticipantEmail);
            studentLinkedIn = itemView.findViewById(R.id.studentParticipantLinkedIn);
            studentLinkedIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(generateRandomLinkedIn(rand.nextInt(6)+1)));
                    context.startActivity(intent);
                }
            });
        }
    }

    public String generateRandomLinkedIn (int num) {
        if (num == 1) {
            return "https://www.linkedin.com/in/williamhgates/";
        } else if (num == 2) {
            return "https://www.linkedin.com/in/daniel-ricciardo/";
        } else if (num == 3) {
            return "https://www.linkedin.com/in/barackobama/";
        } else if (num == 4) {
            return "https://www.linkedin.com/in/melindagates/";
        } else if (num == 5) {
            return "https://www.linkedin.com/in/susan-wojcicki-b136a99/";
        } else {
            return "https://www.linkedin.com/in/rbranson/";
        }
    }


}
