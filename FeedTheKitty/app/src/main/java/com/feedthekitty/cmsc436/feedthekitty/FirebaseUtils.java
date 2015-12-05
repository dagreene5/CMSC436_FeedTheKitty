package com.feedthekitty.cmsc436.feedthekitty;

import android.graphics.Bitmap;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;

/**
 * Created by davidgreene on 12/2/15.
 */
public class FirebaseUtils {

    private Firebase database = new Firebase(MainActivity.firebaseUrl);
    public static final String userRoot = "userList";
    public static final String eventRoot = "eventList";
    public Firebase eventList = database.child(eventRoot);
    public Firebase userList = database.child(userRoot);

    private static FirebaseUtils instance = null;

    private FirebaseUtils() {

    }

    // TODO store event keys, not fully copy
    /*
    public void storeEventUserList(EventData event, String uid) {
        storeEventUserList(generateMapFromEventData(event), uid);
    }
    public void storeEventUserList(HashMap<String, Object> data, String uid) {
        storeEventInPath(uid + "/eventList", data);
    }
    */

    public void createAccount(UserData userData) {
        userList.child(userData.getUserId()).setValue(generateMapFromUserData(userData));
    }

    public void createEventMasterList(EventData event) {
        eventList.push().setValue(generateMapFromEventData(event));
    }

    public HashMap<String, Object> generateMapFromUserData(UserData userData) {
        HashMap<String, Object> data = new HashMap<String, Object>();

        data.put("fullName", userData.getFullName());
        data.put("eventsAttending", userData.getEventsAttending());
        data.put("eventsInvitedTo", userData.getEventsInvitedTo());

        return data;
    }

    //TODO move to EventData class
    public HashMap<String, Object> generateMapFromEventData(EventData event) {
        HashMap<String, Object> data = new HashMap<String, Object>();

        data.put("title", event.getTitle());
        data.put("hashtag", event.getHashtag());
        data.put("description", event.getDescription());
        data.put("eventEndDate", event.getEventEndDate());
        data.put("eventStartDate", event.getEventStartDate());
        data.put("eventEndTime", event.getEventEndTime());
        data.put("eventStartTime", event.getEventStartTime());
        data.put("location", event.getLocation());
        data.put("isPrivate", event.getIsPrivate());
        data.put("amountNeeded", event.getAmountNeeded());
        data.put("peopleInvited", event.getPeopleInvited());
        data.put("peopleAttending", event.getPeopleAttending());

        /*
        data.put("eventImage", event.getEventImage());
        data.put("funds", event.getFunds());
        data.put("peopleAttending", event.getPeopleAttending());
        data.put("peopleInvited", event.getPeopleInvited());
        */

        return data;
    }

    //TODO
    public static Bitmap stringToBitmap(String image) {
        return null;
    }

    public void inviteToEvent(String personId, Long eventId) {
        eventList.child(eventId.toString()).child("peopleInvited").push().setValue(personId);
        userList.child(personId).child("eventsInvitedTo").push().setValue(eventId);
    }

    public void acceptInviteToEvent(String personId, Long eventId) {
        eventList.child(eventId.toString()).child("peopleInvited").child(personId).removeValue();
        eventList.child(eventId.toString()).child("peopleAttending").push().setValue(personId);
        userList.child(personId).child("eventsInvitedTo").child(eventId.toString()).removeValue();
    }

    public void addFundsToEvent(final String eventId, final Integer amount) {
        Query query = eventList.child(eventId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
             @Override
             public void onDataChange(DataSnapshot dataSnapshot) {
                 int prevFunds = (Integer) dataSnapshot.child("funds").getValue();
                 eventList.child(eventId).child("funds").setValue(prevFunds + amount);
             }

             @Override
             public void onCancelled(FirebaseError firebaseError) {

             }
        });
    }

    public static synchronized FirebaseUtils getInstance() {

        if (instance == null) {
            instance = new FirebaseUtils();
        }

        return instance;
    }

}
