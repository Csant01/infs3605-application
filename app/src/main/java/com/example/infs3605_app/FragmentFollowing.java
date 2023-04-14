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

public class FragmentFollowing extends Fragment implements StudentFollowingAdapter.FollowingInterface {
    View v;
    RecyclerView followingRv;
    List<User> allUsers;
    DatabaseConnector db;

    public FragmentFollowing() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_following, container, false);
        followingRv = v.findViewById(R.id.followingFragmentRv);
        StudentFollowingAdapter adapter = new StudentFollowingAdapter(getContext(), allUsers, this);
        followingRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        followingRv.setAdapter(adapter);
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DatabaseConnector(getContext());
        String user = User.currentlyLoggedIn.get(User.currentlyLoggedIn.size()-1);
        allUsers = new ArrayList<>();
        allUsers = db.getUserFollowingUser(user);
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

    public void refreshRecyclerView (String user) {
        allUsers.clear();
        allUsers.addAll(db.getUserFollowingUser(user));

        if (followingRv != null && followingRv.getAdapter() != null) {
            followingRv.getAdapter().notifyDataSetChanged();
        }
    }
}
