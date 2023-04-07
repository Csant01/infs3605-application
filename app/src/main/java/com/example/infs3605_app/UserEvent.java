package com.example.infs3605_app;

public class UserEvent {
    private String userEventId;
    private int userFavourited;
    private int userAttended;
    private String userId;
    private String eventId;
    private String userFeedbackId;
    private int feedbackCompleted;

    public UserEvent (String userEventId, int userFavourited, int userAttended, String userId,
                     String eventId, String userFeedbackId, int feedbackCompleted) {
        this.userEventId = userEventId;
        this.userFavourited = userFavourited;
        this.userAttended = userAttended;
        this.userId = userId;
        this.eventId = eventId;
        this.userFeedbackId = userFeedbackId;
        this.feedbackCompleted = feedbackCompleted;
    }

    public String getUserEventId() {
        return userEventId;
    }

    public void setUserEventId(String userEventId) {
        this.userEventId = userEventId;
    }

    public int getUserFavourited() {
        return userFavourited;
    }

    public void setUserFavourited(int userFavourited) {
        this.userFavourited = userFavourited;
    }

    public int getUserAttended() {
        return userAttended;
    }

    public void setUserAttended(int userAttended) {
        this.userAttended = userAttended;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getUserFeedbackId() {
        return userFeedbackId;
    }

    public void setUserFeedbackId(String userFeedbackId) {
        this.userFeedbackId = userFeedbackId;
    }

    public int getFeedbackCompleted() {
        return feedbackCompleted;
    }

    public void setFeedbackCompleted(int feedbackCompleted) {
        this.feedbackCompleted = feedbackCompleted;
    }
}
