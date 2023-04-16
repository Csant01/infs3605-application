package com.example.infs3605_app;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SwipeCardAdapter extends BaseAdapter {
    private ArrayList<Event> allEvents;
    private Context context;
    DatabaseConnector db;
    User organiser;
    byte[] orgBytes;
    private static final String TAG = "SwipeCardAdapter";


    public SwipeCardAdapter (ArrayList<Event> allEvents, Context context) {
        this.allEvents = allEvents;
        this.context = context;

    }

    @Override
    public int getCount() {
        return allEvents.size();
    }

    @Override
    public Object getItem(int position) {
        return allEvents.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)  {
        View v = convertView;
        if (v == null) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_home_swipe_card, parent, false);
        }
        db = new DatabaseConnector(context);
        ArrayList<User> allUsers = db.getUserInfo();

        for (int i = 0; i < allUsers.size(); i++) {
            if (allUsers.get(i).getUserID().equals(allEvents.get(position).getEventOwner())) {
                organiser = allUsers.get(i);
            }
        }


        orgBytes = db.retrieveOrganiserImageDirect(allEvents.get(position).getEventOwner());
        Log.d(TAG, "Event Owner: " + allEvents.get(position).getEventOwner());
        

        ((TextView) v.findViewById(R.id.namePrint)).setText(allEvents.get(position).getEventName());
        ((TextView) v.findViewById(R.id.organiserPrint)).setText(db.getUserName(allEvents.get(position).getEventOwner()));
        ((TextView) v.findViewById(R.id.descriptionPrint)).setText(allEvents.get(position).getEventDescription());
        ((TextView) v.findViewById(R.id.cityPrint)).setText(allEvents.get(position).getEventCity());
        ((TextView) v.findViewById(R.id.categoryPrint)).setText(allEvents.get(position).getEventCategory());
        ((TextView) v.findViewById(R.id.datePrint)).setText(formatEpoch(allEvents.get(position).getEventDate()));
        ((TextView) v.findViewById(R.id.startTimePrint)).setText(allEvents.get(position).getEventStartTime());
        ((TextView) v.findViewById(R.id.endTimePrint)).setText(allEvents.get(position).getEventEndTime());
        ((ImageView) v.findViewById(R.id.cardImage)).setImageBitmap(ImageUtils.getImage(allEvents.get(position).getEventImage()));
        ((ImageView) v.findViewById(R.id.homePageProfPic)).setImageBitmap(ImageUtils.getImage(orgBytes));
        ((TextView) v.findViewById(R.id.homePageFaculty)).setText(organiser.getUserFaculty());
        ((TextView) v.findViewById(R.id.homePageEmail)).setText(organiser.getUserEmail());
        ((TextView) v.findViewById(R.id.homePageFacility)).setText(allEvents.get(position).getEventFacility());



        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EventDetailActivity.class);
                intent.putExtra("EVENT_ID", allEvents.get(position).getEventId());
                intent.putExtra("USER_TYPE", "student");
                intent.putExtra("PAGE", "StudentHomePage");
                context.startActivity(intent);
            }
        });
        return v;

    }

    public String formatEpoch (long value) {
        String date = new SimpleDateFormat("dd/MM/yyyy").format(new Date(value));
        return date;

    }



}
