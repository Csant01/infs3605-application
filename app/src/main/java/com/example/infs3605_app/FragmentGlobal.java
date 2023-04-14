package com.example.infs3605_app;

import android.content.Intent;
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

public class FragmentGlobal extends Fragment implements StudentFollowingAdapter.FollowingInterface {

    View v;
    RecyclerView followingRv;
    List<User> allUsers;
    DatabaseConnector db;

    public FragmentGlobal() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_global, container, false);
        followingRv = v.findViewById(R.id.globalFragmentRv);
        StudentFollowingAdapter adapter = new StudentFollowingAdapter(getContext(), allUsers, this);
        followingRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        followingRv.setAdapter(adapter);
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DatabaseConnector(getContext());
        allUsers = new ArrayList<>();
        allUsers = db.getNonStudents();
    }

    @Override
    public void onEventClick(int pos) {
        Intent intent = new Intent(getContext(), OrganiserPublicProfileActivity.class);
        intent.putExtra("ORGANISER_ID", allUsers.get(pos).getUserName());
        intent.putExtra("USER_TYPE", "student");
        intent.putExtra("PAGE", "StudentFollowing");
        intent.putExtra("FOLLOWING_CHECK", "1");
        startActivity(intent);
    }
}
