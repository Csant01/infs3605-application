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

public class FragmentUserApproved extends Fragment {
    View v;
    RecyclerView userApprovedRv;
    List<User> allUsers;
    DatabaseConnector db;

    public FragmentUserApproved () {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_user_approved, container, false);
        userApprovedRv = v.findViewById(R.id.userApprovedRv);
        AdminUserApprovalAdapter adapter = new AdminUserApprovalAdapter(getContext(), allUsers);
        userApprovedRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        userApprovedRv.setAdapter(adapter);
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DatabaseConnector(getContext());
        ArrayList<User> tmpAllUsers = db.getUserInfo();
        allUsers = new ArrayList<>();
        ArrayList<String> pendingUserIds = new ArrayList<>();
        for (int i = 0; i < tmpAllUsers.size(); i++) {
            if (db.getUserApprovedStatus(tmpAllUsers.get(i).getUserID()) == 1) {
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
