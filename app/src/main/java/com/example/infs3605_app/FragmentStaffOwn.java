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

public class FragmentStaffOwn extends Fragment implements StaffOwnEventsAdapter.StaffOwnEventsInterface{
    View v;
    RecyclerView staffEventsRv;
    List<Event> allEvents;
    DatabaseConnector db;

    public FragmentStaffOwn() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_staff_own, container, false);
        staffEventsRv = v.findViewById(R.id.staffOwnRv);
        StaffOwnEventsAdapter adapter = new StaffOwnEventsAdapter(getContext(), allEvents, this);
        staffEventsRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        staffEventsRv.setAdapter(adapter);
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DatabaseConnector(getContext());
        ArrayList<Event> tmpAllEvents = db.getEventInfo();
        String user = User.currentlyLoggedIn.get(User.currentlyLoggedIn.size()-1);
        allEvents = new ArrayList<>();
        for (int i = 0; i < tmpAllEvents.size(); i++) {
            if (tmpAllEvents.get(i).getEventIsApproved() > 0 && tmpAllEvents.get(i).getEventOwner().equals(db.getUserId(user))) {
                allEvents.add(tmpAllEvents.get(i));
            }
        }
    }

    @Override
    public void onEventClick(int position) {
        Intent intent = new Intent(getContext(), ManageEventDetailActivity.class);
        intent.putExtra("EVENT_ID", allEvents.get(position).getEventId());
        intent.putExtra("USER_TYPE", "organiser");
        intent.putExtra("PAGE", "StaffEvents");
        startActivity(intent);
    }
}
