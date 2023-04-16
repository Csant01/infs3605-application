package com.example.infs3605_app;

public class Event {
    private String eventId;
    private String eventName;
    private String eventLocation;
    private String eventDescription;
    private String eventOwner;
    private String eventCountry;
    private String eventCity;
    private String eventCategory;
    private byte[] eventImage;
    private long eventDate;
    private String eventStartTime;
    private String eventEndTime;
    private int eventIsDeleted;
    private int eventIsApproved;
    private int eventPredAttn;
    private int eventTicketed;
    private int eventActAttn;
    private double eventCost;
    private int eventStaffing;
    private String eventFacility;




    // Constructor
    public Event(String eventId, String eventName, String eventLocation, String eventDescription,
                 String eventOwner, String eventCountry, String eventCity, String eventCategory,
                 int eventPredAttn, int eventActAttn, double eventCost, int eventTicketed, byte[] eventImage,
                 long eventDate, String eventStartTime, String eventEndTime,
                 int eventIsDeleted, int eventIsApproved, int eventStaffing, String eventFacility) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.eventLocation = eventLocation;
        this.eventDescription = eventDescription;
        this.eventOwner = eventOwner;
        this.eventCountry = eventCountry;
        this.eventCity = eventCity;
        this.eventCategory = eventCategory;
        this.eventImage = eventImage;
        this.eventDate = eventDate;
        this.eventStartTime = eventStartTime;
        this.eventEndTime = eventEndTime;
        this.eventIsDeleted = eventIsDeleted;
        this.eventIsApproved = eventIsApproved;
        this.eventCost = eventCost;
        this.eventActAttn = eventActAttn;
        this.eventPredAttn = eventPredAttn;
        this.eventTicketed = eventTicketed;
        this.eventStaffing = eventStaffing;
        this.eventFacility = eventFacility;
    }

    public String getEventFacility() {
        return eventFacility;
    }

    public void setEventFacility(String eventFacility) {
        this.eventFacility = eventFacility;
    }

    public int getEventStaffing() {
        return eventStaffing;
    }

    public void setEventStaffing(int eventStaffing) {
        this.eventStaffing = eventStaffing;
    }

    public long getEventDate() {
        return eventDate;
    }

    public void setEventDate(long eventDate) {
        this.eventDate = eventDate;
    }

    public int getEventTicketed() {
        return eventTicketed;
    }

    public void setEventTicketed(int eventTicketed) {
        this.eventTicketed = eventTicketed;
    }
    // Getter and Setter methods for all fields
    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getEventOwner() {
        return eventOwner;
    }

    public void setEventOwner(String eventOwner) {
        this.eventOwner = eventOwner;
    }

    public String getEventCountry() {
        return eventCountry;
    }

    public void setEventCountry(String eventCountry) {
        this.eventCountry = eventCountry;
    }

    public String getEventCity() {
        return eventCity;
    }

    public void setEventCity(String eventCity) {
        this.eventCity = eventCity;
    }

    public String getEventCategory() {
        return eventCategory;
    }

    public void setEventCategory(String eventCategory) {
        this.eventCategory = eventCategory;
    }

    public int getEventPredAttn() {
        return eventPredAttn;
    }

    public void setEventPredAttn(int eventPredAttn) {
        this.eventPredAttn = eventPredAttn;
    }

    public int getEventActAttn() {
        return eventActAttn;
    }

    public void setEventActAttn(int eventActAttn) {
        this.eventActAttn = eventActAttn;
    }

    public byte[] getEventImage() {
        return eventImage;
    }

    public void setEventImage(byte[] eventImage) {
        this.eventImage = eventImage;
    }

    public String getEventStartTime() {
        return eventStartTime;
    }

    public void setEventStartTime(String eventStartTime) {
        this.eventStartTime = eventStartTime;
    }

    public String getEventEndTime() {
        return eventEndTime;
    }

    public void setEventEndTime(String eventEndTime) {
        this.eventEndTime = eventEndTime;
    }

    public int getEventIsDeleted() {
        return eventIsDeleted;
    }

    public void setEventIsDeleted(int eventIsDeleted) {
        this.eventIsDeleted = eventIsDeleted;
    }

    public int getEventIsApproved() {
        return eventIsApproved;
    }

    public void setEventIsApproved(int eventIsApproved) {
        this.eventIsApproved = eventIsApproved;
    }

    public double getEventCost() {
        return eventCost;
    }

    public void setEventCost(double eventCost) {
        this.eventCost = eventCost;
    }
}