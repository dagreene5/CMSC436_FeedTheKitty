package com.feedthekitty.cmsc436.feedthekitty;

import com.firebase.client.Firebase;

import java.util.HashMap;

/**
 * Created by davidgreene on 12/2/15.
 */
public class FirebaseUtils {

    Firebase database = new Firebase(MainActivity.firebaseUrl);

    private static FirebaseUtils instance = null;

    private FirebaseUtils() {

    }

    public void storeEventUserList(EventData event, String uid) {
        storeEventUserList(generateMapFromEventData(event), uid);
    }
    public void storeEventUserList(HashMap<String, String> data, String uid) {
        storeEventInPath(uid + "/eventList", data);
    }
    public void storeEventMasterList(EventData event) {
        storeEventMasterList(generateMapFromEventData(event));
    }

    public HashMap<String, String> generateMapFromEventData(EventData event) {
        HashMap<String, String> data = new HashMap<String, String>();

        data.put("title", event.getTitle());
        data.put("hashtag", event.getHashtag());

        return data;
    }

    public void storeEventMasterList(HashMap<String, String> data) {
        storeEventInPath("masterList", data);
    }

    private void storeEventInPath(String path, HashMap<String, String> data) {
        database.child(path).push().setValue(data);
    }

    public static synchronized FirebaseUtils getInstance() {

        if (instance == null) {
            instance = new FirebaseUtils();
        }

        return instance;
    }

}
