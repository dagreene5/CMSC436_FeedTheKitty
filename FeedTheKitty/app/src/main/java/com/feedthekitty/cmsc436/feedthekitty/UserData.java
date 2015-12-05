package com.feedthekitty.cmsc436.feedthekitty;

import java.util.List;

/**
 * Created by davidgreene on 12/3/15.
 */
public class UserData {

    String userId;
    String fullName;
    List<String> eventsAttending;
    List<String> eventsInvitedTo;

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setEventsAttending(List<String> eventsAttending) {
        this.eventsAttending = eventsAttending;
    }

    public List<String> getEventsAttending() {
        return eventsAttending;
    }

    public void setEventsInvitedTo(List<String> eventsInvitedTo) {
        this.eventsInvitedTo = eventsInvitedTo;
    }

    public List<String> getEventsInvitedTo() {
        return eventsInvitedTo;
    }





}
