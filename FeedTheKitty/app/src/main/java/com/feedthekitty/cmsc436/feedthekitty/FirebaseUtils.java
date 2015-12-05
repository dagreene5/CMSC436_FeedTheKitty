package com.feedthekitty.cmsc436.feedthekitty;

import android.graphics.Bitmap;
import android.util.Log;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * Created by davidgreene on 12/2/15.
 */
public class FirebaseUtils {

    private Firebase database = new Firebase(MainActivity.firebaseUrl);
    public static final String userRoot = "userList";
    public static final String eventRoot = "eventList";
    public static final HashMap<Long, EventData> eventDataMap = new HashMap<Long, EventData>();
    public static final HashMap<String, UserData> userDataMap = new HashMap<String, UserData>();
    public Firebase eventList = database.child(eventRoot);
    public Firebase userList = database.child(userRoot);
    private ChildEventListener userListener;
    private ChildEventListener eventListener;

    private static FirebaseUtils instance = null;

    public static final String TAG = "FirebaseUtils";

    public ArrayList<EventData> getAllEvents() {
        ArrayList<EventData> allEvents = new ArrayList<EventData>();
        allEvents.addAll(eventDataMap.values());
        return allEvents;
    }

    public ArrayList<UserData> getAllUsers() {
        ArrayList<UserData> allUsers = new ArrayList<UserData>();
        allUsers.addAll(userDataMap.values());
        return allUsers;
    }

    private FirebaseUtils() {

        userListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String uid = (String) dataSnapshot.getValue();
                UserData userData = UserData.createFromDataSnapshot(uid, dataSnapshot);
                Log.d(TAG, "Recovered: " + userData.toString());
                userDataMap.put(uid, userData);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "on child changed call for userListener");
                onChildAdded(dataSnapshot, s);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, "on child removed call for userListener");
                eventDataMap.remove((String) dataSnapshot.getValue());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        };
        eventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Long stackId = (Long) dataSnapshot.getValue();
                EventData eventData = EventData.createFromDataSnapshot(stackId, dataSnapshot);
                Log.d(TAG, "Recovered EventData: " + eventData.toString());
                eventDataMap.put(stackId, eventData);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "on child changed call for eventListener");
                onChildAdded(dataSnapshot, s);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, "on child removed call for eventListener");
                eventDataMap.remove((Long) dataSnapshot.getValue());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        };

        userList.addChildEventListener(userListener);
        eventList.addChildEventListener(eventListener);
    }

    public UserData fetchUserData(String uid) {
        return userDataMap.get(uid);
    }

    public EventData fetchEventData(Long eventId) {
        return eventDataMap.get(eventId);
    }

    //TODO make sure Listeners are picking this up
    public void storeUserData(UserData userData) {
        userList.child(userData.getUserId()).setValue(userData.packageIntoMap());
    }

    public void createEventData(EventData event) {
        eventList.push().setValue(event.packageIntoMap());
    }

    public void updateEventData(EventData event) {
        eventList.child(String.valueOf(event.getStackId())).setValue(event.packageIntoMap());
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
