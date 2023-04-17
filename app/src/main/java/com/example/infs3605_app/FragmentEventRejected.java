package com.example.infs3605_app;

import android.content.Intent;
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

public class FragmentEventRejected extends Fragment implements AdminEventApprovalAdapter.EventApprovalInterface {
    View v;
    RecyclerView eventRejectedRv;
    List<Event> allEvents;
    DatabaseConnector db;
    private static final String TAG = "FragmentEventRejected";

    public FragmentEventRejected () {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_event_rejected, container, false);
        eventRejectedRv = v.findViewById(R.id.eventRejectedRv);
        AdminEventApprovalAdapter adapter = new AdminEventApprovalAdapter(getContext(), allEvents, this);
        eventRejectedRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        eventRejectedRv.setAdapter(adapter);
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DatabaseConnector(getContext());
        allEvents = new ArrayList<>();
        ArrayList<Event> tmpEventList = db.getEventInfo();
        for (int i = 0; i < tmpEventList.size(); i++) {
            if (tmpEventList.get(i).getEventIsApproved() == -1) {
                allEvents.add(tmpEventList.get(i));
            }
        }

        Log.d(TAG, "allEvents Size: " + allEvents.size());
    }


    @Override
    public void onEventClick(int position) {
        Intent intent = new Intent(getContext(), AdminEventApprovalsDetail.class);
        intent.putExtra("EVENT_ID", allEvents.get(position).getEventId());
        intent.putExtra("USER_TYPE", "admin");
        intent.putExtra("PAGE", "EventApproval");
        startActivity(intent);

    }
}
