package com.feedthekitty.cmsc436.feedthekitty;

import android.content.Intent;
import android.os.Bundle;

import com.firebase.client.DataSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by davidgreene on 12/3/15.
 */
public class UserData {

    String userId;
    String fullName;
    ArrayList<CharSequence> eventsAttending;

    public UserData() {
        eventsAttending = new ArrayList<CharSequence>();
        fullName = "name not set";
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setFullName(String fullName) {
        if (fullName != null) {
            this.fullName = fullName;
        }
    }

    public String getFullName() {
        return fullName;
    }

    public void setEventsAttending(ArrayList<CharSequence> eventsAttending) {
        this.eventsAttending = eventsAttending;
    }

    public ArrayList<CharSequence> getEventsAttending() {
        return eventsAttending;
    }

    public Intent packageIntoIntent() {
        Intent intent = new Intent();

        intent.putExtra("userId", userId);
        intent.putExtra("fullName", fullName);
        intent.putCharSequenceArrayListExtra("eventsAttending", eventsAttending);

        return intent;
    }

    public static UserData createFromIntent(Intent intent) {

        UserData userData = new UserData();
        Bundle extras = intent.getExtras();

        userData.setUserId(extras.getString("userId"));
        userData.setFullName(extras.getString("fullName"));
        userData.setEventsAttending(extras.getCharSequenceArrayList("eventsAttending"));

        return userData;
    }

    public void setAttendingEvent(String eventId) {
        if (!eventsAttending.contains(eventId)) {
            eventsAttending.add(eventId);
        }
    }

    public void removeEventFromAttending(String eventId) {
        eventsAttending.remove(eventId);
    }

    public HashMap<String, Object> packageIntoMap() {
        HashMap<String, Object> data = new HashMap<String, Object>();

        data.put("fullName", fullName);
        if (eventsAttending.size() != 0) {
            data.put("eventsAttending", eventsAttending);
        }

        return data;
    }

    @SuppressWarnings("unchecked")
    public static UserData createFromDataSnapshot(String uid, DataSnapshot dataSnapshot) {

        UserData userData = new UserData();

        userData.setUserId(uid);
        userData.setFullName((String) dataSnapshot.child("fullName").getValue());
        userData.setEventsAttending((ArrayList<CharSequence>) dataSnapshot.child("eventsAttending")
                .getValue());

        return userData;
    }

    public String toString() {
        return packageIntoMap().toString();
    }



}
