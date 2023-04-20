package com.example.infs3605_app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class FragmentUserRejected extends Fragment {
    View v;
    RecyclerView userRejectedRv;
    List<User> allUsers;
    DatabaseConnector db;

    public FragmentUserRejected () {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_user_rejected, container, false);
        userRejectedRv = v.findViewById(R.id.userRejectedRv);
        AdminUserApprovalAdapter adapter = new AdminUserApprovalAdapter(getContext(), allUsers);
        userRejectedRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        userRejectedRv.setAdapter(adapter);
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DatabaseConnector(getContext());
        ArrayList<User> tmpAllUsers = db.getUserInfo();
        ArrayList<String> pendingUserIds = new ArrayList<>();
        allUsers = new ArrayList<>();
        for (int i = 0; i < tmpAllUsers.size(); i++) {
            if (db.getUserApprovedStatus(tmpAllUsers.get(i).getUserID()) == -1) {
                pendingUserIds.add(tmpAllUsers.get(i).getUserID());
            }
        }

        for (User user : tmpAllUsers) {
            if (pendingUserIds.contains(user.getUserID())) {
                allUsers.add(user);
            }
        }

    }
}
