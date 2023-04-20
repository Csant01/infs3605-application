package com.example.infs3605_app;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdminUserApprovalAdapter extends RecyclerView.Adapter<AdminUserApprovalAdapter.ViewHolder> {
    private Context context;
    private List<User> allUsers;
    DatabaseConnector db;

    public AdminUserApprovalAdapter(Context context, List<User> allUsers) {
        this.context = context;
        this.allUsers = allUsers;
    }

    private static final String TAG = "AdminUserApprovalAdapter";
    @NonNull
    @Override
    public AdminUserApprovalAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.admin_user_approvals_list_row, parent, false);
        return new AdminUserApprovalAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminUserApprovalAdapter.ViewHolder holder, int position) {
        db = new DatabaseConnector(context);
        User user = allUsers.get(position);
        holder.userName.setText(user.getUserName());
        holder.userFaculty.setText(user.getUserFaculty());
        holder.userType.setText(db.getUserType(Integer.parseInt(user.getUserType())));
        if (db.getUserApprovedStatus(user.getUserID()) == 1) {
            holder.approveUser.setVisibility(View.GONE);
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams)  holder.rejectUser.getLayoutParams();
            layoutParams.horizontalBias = 0.5f;
            holder.rejectUser.setLayoutParams(layoutParams);
        } else if (db.getUserApprovedStatus(user.getUserID()) == -1) {
            holder.rejectUser.setVisibility(View.GONE);
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams)  holder.approveUser.getLayoutParams();
            layoutParams.horizontalBias = 0.5f;
            holder.approveUser.setLayoutParams(layoutParams);
        }
        byte[] blob = db.retrieveOrganiserImageFromDatabaseFiltered(user.getUserID());
        if (blob == null) {
            blob = db.retrieveOrganiserImageDirect(user.getUserID());
        }
        holder.userImage.setImageBitmap(ImageUtils.getImage(blob));

    }

    @Override
    public int getItemCount() {
        return allUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView userImage;
        TextView userName, userType, userFaculty;
        ImageButton approveUser, rejectUser;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userImage = itemView.findViewById(R.id.adminUserApprovalImage);
            userName = itemView.findViewById(R.id.adminApprovalUserNamePrint);
            userType = itemView.findViewById(R.id.adminApprovalsUserTypePrint);
            userFaculty = itemView.findViewById(R.id.adminApprovalsUserFacultyPrint);
            approveUser = itemView.findViewById(R.id.approveUserButton);
            rejectUser = itemView.findViewById(R.id.rejectUserButton);

            approveUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    db = new DatabaseConnector(context);
                    db.setUserApproval(allUsers.get(getAdapterPosition()).getUserID());
                    Toast.makeText(context.getApplicationContext(), allUsers.get(getAdapterPosition()).getUserName() + " is approved.",
                            Toast.LENGTH_SHORT).show();
                }
            });

            rejectUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    db = new DatabaseConnector(context);
                    db.rejectUser(allUsers.get(getAdapterPosition()).getUserID());
                    Toast.makeText(context.getApplicationContext(), allUsers.get(getAdapterPosition()).getUserName() + " is rejected.",
                            Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
