package com.example.infs3605_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class StudentFollowingAdapter extends RecyclerView.Adapter<StudentFollowingAdapter.ViewHolder> {
    private Context context;
    private List<User> allUsers;
    DatabaseConnector db;
    private FollowingInterface eventClickListener;

    public StudentFollowingAdapter(Context context, List<User> allUsers, FollowingInterface eventClickListener) {
        this.context = context;
        this.allUsers = allUsers;
        this.eventClickListener = eventClickListener;
    }

    @NonNull
    @Override
    public StudentFollowingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.student_following_list_row, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentFollowingAdapter.ViewHolder holder, int position) {
        holder.followingName.setText(allUsers.get(position).getUserName());
        holder.followingFaculty.setText(allUsers.get(position).getUserFaculty());
        if (allUsers.get(position).getUserImage() == null) {
            holder.followingImage.setImageResource(R.drawable.unsw_unite_logo);
        } else {
            holder.followingImage.setImageBitmap(ImageUtils.getImage(allUsers.get(position).getUserImage()));
        }
        db = new DatabaseConnector(context);
        if (db.getUserFollowingString(User.currentlyLoggedIn.get(User.currentlyLoggedIn.size()-1)).contains(allUsers.get(position).getUserID())) {
            holder.followButton.setImageResource(R.drawable.ic_filled_bookmark);
        } else {
            holder.followButton.setImageResource(R.drawable.ic_friend);
        }


    }

    @Override
    public int getItemCount() {
        return allUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView followingName, followingFaculty;
        ImageView followingImage;
        ImageButton followButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            followingName = itemView.findViewById(R.id.followingName);
            followingFaculty = itemView.findViewById(R.id.followingFaculty);
            followingImage = itemView.findViewById(R.id.followingImage);
            followButton = itemView.findViewById(R.id.followingImageButton);

            itemView.setOnClickListener(this);

            followButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    db = new DatabaseConnector(context);
                    String user = User.currentlyLoggedIn.get(User.currentlyLoggedIn.size()-1);
                    if (db.checkFollowing(user, allUsers.get(getAdapterPosition()).getUserID())) {
                        db.unsetUserFollowing(user, allUsers.get(getAdapterPosition()).getUserID());
                        Toast.makeText(context, "Unfollowed " + allUsers.get(getAdapterPosition()).getUserName(),
                                Toast.LENGTH_SHORT).show();
                        followButton.setImageResource(R.drawable.ic_friend);
                    } else {
                        db.setUserFollowing(user, allUsers.get(getAdapterPosition()).getUserID());
                        Toast.makeText(context, "Followed " + allUsers.get(getAdapterPosition()).getUserName(),
                                Toast.LENGTH_SHORT).show();
                        followButton.setImageResource(R.drawable.ic_filled_bookmark);
                    }
                }
            });

        }

        @Override
        public void onClick(View v) {
            eventClickListener.onEventClick(getAdapterPosition());
        }
    }

    public interface FollowingInterface {
        void onEventClick (int pos);
    }
}
