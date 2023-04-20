package com.example.infs3605_app;

import android.os.Bundle;
import android.util.Log;
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

public class FragmentUserPending extends Fragment {
    View v;
    RecyclerView userPendingRv;
    List<User> allUsers;
    DatabaseConnector db;

    public FragmentUserPending () {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_user_pending, container, false);
        userPendingRv = v.findViewById(R.id.userPendingRv);
        AdminUserApprovalAdapter adapter = new AdminUserApprovalAdapter(getContext(), allUsers);
        userPendingRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        userPendingRv.setAdapter(adapter);
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
            if (db.getUserApprovedStatus(tmpAllUsers.get(i).getUserID()) == 0) {
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
