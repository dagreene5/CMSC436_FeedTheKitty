package com.feedthekitty.cmsc436.feedthekitty;

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
    public Firebase masterList = database.child("masterList");
    public Firebase userList = database.child("userList");

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
        masterList.push().setValue(generateMapFromEventData(event));
    }

    public HashMap<String, Object> generateMapFromUserData(UserData userData) {
        HashMap<String, Object> data = new HashMap<String, Object>();

        data.put("fullName", userData.getFullName());
        data.put("eventsAttending", userData.getEventsAttending());
        data.put("eventsInvitedTo", userData.getEventsInvitedTo());

        return data;
    }

    public HashMap<String, Object> generateMapFromEventData(EventData event) {
        HashMap<String, Object> data = new HashMap<String, Object>();

        data.put("title", event.getTitle());
        data.put("hashtag", event.getHashtag());
        data.put("description", event.getDescription());
        data.put("eventImage", event.getEventImage());
        data.put("funds", event.getFunds());
        data.put("peopleAttending", event.getPeopleAttending());
        data.put("peopleInvited", event.getPeopleInvited());

        return data;
    }

    public void inviteToEvent(String personId, String eventId) {
        masterList.child(eventId).child("peopleInvited").push().setValue(personId);
        userList.child(personId).child("eventsInvitedTo").push().setValue(eventId);
    }

    public void acceptInviteToEvent(String personId, String eventId) {
        masterList.child(eventId).child("peopleInvited").child(personId).removeValue();
        userList.child(personId).child("eventsInvitedTo").child(eventId).removeValue();
    }

    public void addFundsToEvent(final String eventId, final Integer amount) {
        Query query = masterList.child(eventId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
             @Override
             public void onDataChange(DataSnapshot dataSnapshot) {
                 int prevFunds = (Integer) dataSnapshot.child("funds").getValue();
                 masterList.child(eventId).child("funds").setValue(prevFunds + amount);
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
