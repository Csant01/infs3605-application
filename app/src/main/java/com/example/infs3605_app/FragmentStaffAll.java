package com.example.infs3605_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class FragmentStaffAll extends Fragment implements StaffEventsAdapter.StaffAllEventsInterface{
    View v;
    RecyclerView staffEventsRv;
    List<Event> allEvents;
    DatabaseConnector db;
    private static final String TAG = "FragmentStaffAll";
    String user = User.currentlyLoggedIn.get(User.currentlyLoggedIn.size()-1);

    public FragmentStaffAll() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_staff_all, container, false);
        staffEventsRv = v.findViewById(R.id.staffAllRv);
        StaffEventsAdapter adapter = new StaffEventsAdapter(getContext(), allEvents, this);
        staffEventsRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        staffEventsRv.setAdapter(adapter);
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DatabaseConnector(getContext());
        allEvents = new ArrayList<>();
        ArrayList<Event> tmpEvents  = db.getEventInfo();
        Log.d(TAG, "allEventsSize: " + allEvents.size());
        allEvents = new ArrayList<>();
        for (int i = 0; i < tmpEvents.size(); i++) {
//            if (tmpEvents.get(i).getEventIsApproved() > 0) {
                allEvents.add(tmpEvents.get(i));
//            }
        }
    }

    @Override
    public void onEventClick(int position) {
        String userId = db.getUserId(user);
        if (allEvents.get(position).getEventOwner().equals(userId)) {
            Log.d(TAG, "onEventClick: EventOwner" + allEvents.get(position).getEventOwner());
            Log.d(TAG, "onEventClick: Owner" + userId);
            Intent intent = new Intent(getContext(), ManageEventDetailActivity.class);
            intent.putExtra("EVENT_ID", allEvents.get(position).getEventId());
            intent.putExtra("USER_TYPE", "organiser");
            intent.putExtra("PAGE", "StaffEvents");
            startActivity(intent);
        } else {
            Toast.makeText(getContext(), "You don't have access to view this event's detais.", Toast.LENGTH_SHORT).show();
        }
    }
}
