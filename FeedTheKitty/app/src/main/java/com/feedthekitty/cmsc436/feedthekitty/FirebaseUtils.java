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
    public static final HashMap<String, EventData> eventDataMap = new HashMap<String, EventData>();
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
                UserData userData = dataSnapshot.getValue(UserData.class);
                userData.setUserId(dataSnapshot.getKey());
                Log.d(TAG, "Recovered: " + userData.toString());
                userDataMap.put(userData.getUserId(), userData);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "on child changed call for userListener");
                onChildAdded(dataSnapshot, s);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, "on child removed call for userListener");
                eventDataMap.remove(dataSnapshot.getKey());
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

                EventData eventData = dataSnapshot.getValue(EventData.class);
                eventData.setEventKey(dataSnapshot.getKey());
                Log.d(TAG, "Recovered EventData: " + eventData.toString());
                Log.d(TAG, "Previous key: " + s);
                Log.d(TAG, "datasnapshot: " + dataSnapshot.getKey());
                Log.d(TAG, eventData.getDescription());
                eventDataMap.put(eventData.getEventKey(), eventData);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "on child changed call for eventListener");
                onChildAdded(dataSnapshot, s);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, "on child removed call for eventListener");
                eventDataMap.remove(dataSnapshot.getKey());
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

    public EventData fetchEventData(String eventId) {
        return eventDataMap.get(eventId);
    }

    public void storeUserData(UserData userData) {
        userList.child(userData.getUserId()).setValue(userData.packageIntoMap());
    }

    public void createEventData(EventData event) {
        eventList.push().setValue(event.packageIntoMap());
    }

    public void updateEventData(EventData event) {
        eventList.child(String.valueOf(event.getStackId())).setValue(event.packageIntoMap());
    }

    public void addUserToEvent(String personId, String eventId) {
        UserData userData = fetchUserData(personId);
        EventData eventData = fetchEventData(eventId);
        userData.setAttendingEvent(eventId);
        eventData.addPersonAttending(personId);

        updateEventData(eventData);
        storeUserData(userData);
    }

    public void removeUserFromEvent(String personId, String eventId) {
        UserData userData = fetchUserData(personId);
        EventData eventData = fetchEventData(eventId);
        userData.removeEventFromAttending(eventId);
        eventData.removePersonAttending(personId);

        updateEventData(eventData);
        storeUserData(userData);
    }

    public void addFundsToEvent(String eventId, final Integer amount) {
        EventData eventData = fetchEventData(eventId);
        eventData.addFunds(amount);
        updateEventData(eventData);
    }

    public static synchronized FirebaseUtils getInstance() {

        if (instance == null) {
            instance = new FirebaseUtils();
        }

        return instance;
    }

}
