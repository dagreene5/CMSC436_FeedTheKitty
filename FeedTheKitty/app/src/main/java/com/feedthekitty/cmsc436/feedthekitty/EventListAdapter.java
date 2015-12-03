package com.feedthekitty.cmsc436.feedthekitty;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import java.util.ArrayList;

/**
 * Created by davidgreene on 12/2/15.
 */
public class EventListAdapter extends BaseAdapter {

    private ArrayList<EventData> listItems;
    private ArrayList<String> keys;
    private Query dataRef;private LayoutInflater inflater;
    private int layout;
    private Activity activity;
    private ChildEventListener listener;
    public static final String HASHTAG_SEARCH = "hs";
    public static final String TITLE_SEARCH = "ts";
    public static final String TAG = "EventListAdapter";

    public EventListAdapter(Query dataRef, int layout, Activity activity, final String searchTerm,
                            final String searchType) {

        this.dataRef = dataRef;
        this.layout = layout;
        this.activity = activity;

        inflater = activity.getLayoutInflater();

        listItems = new ArrayList<EventData>();
        keys = new ArrayList<String>();

        if (searchType == null) {
            listener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String lastKey) {

                    Log.d(TAG, "In hashtag onChildAdded");
                    EventData item = dataSnapshot.getValue(EventData.class);

                    grabItem(dataSnapshot, lastKey, item);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    Log.d(TAG, "In hashtag onChildChanged");
                    // do nothing. List should be a snapshot of dataset
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    // do nothing
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                    // do nothing
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    // do nothing
                }
            };
        } else if (searchType.equals(HASHTAG_SEARCH)) {
            listener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String lastKey) {

                    Log.d(TAG, "In hashtag onChildAdded");
                    EventData item = dataSnapshot.getValue(EventData.class);

                    if (item.getHashtag().equals(searchTerm)) {
                        grabItem(dataSnapshot, lastKey, item);
                    }
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    Log.d(TAG, "In hashtag onChildChanged");
                    // do nothing. List should be a snapshot of dataset
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    // do nothing
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                    // do nothing
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    // do nothing
                }
            };
        } else if (searchType.equals(TITLE_SEARCH)) {
            listener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String lastKey) {

                    EventData item = dataSnapshot.getValue(EventData.class);
                    if (item.getTitle().equals(searchTerm)) {
                        grabItem(dataSnapshot, lastKey, item);
                    }
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    // do nothing. List should be a snapshot of dataset
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    // do nothing
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                    // do nothing
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    // do nothing
                }
            };
        }
        this.dataRef.addChildEventListener(listener);

    }

    public void close() {
        dataRef.removeEventListener(listener);
        listItems.clear();
        keys.clear();
        notifyDataSetChanged();
    }

    public void grabItem(DataSnapshot dataSnapshot, String lastKey, EventData item) {
        String key = dataSnapshot.getKey();

        if (lastKey == null) {
            listItems.add(0, item);
            keys.add(0, key);
        } else {
            int prevIndex = keys.indexOf(lastKey);
            int index = prevIndex + 1;

            if (index == listItems.size()) {
                listItems.add(item);
                keys.add(key);
            } else {
                listItems.add(index, item);
                keys.add(index, key);
            }
        }

        notifyDataSetChanged();
    }

    public void populateListItem(View view, EventData data) {

        TextView title = (TextView) view.findViewById(R.id.text_event_title);
        TextView hashtag = (TextView) view.findViewById(R.id.text_event_hashtag);

        title.setText(data.getTitle());
        hashtag.setText(data.getHashtag());
    }

    @Override
    public int getCount() {
        return listItems.size();
    }

    @Override
    public Object getItem(int position) {
        return listItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = inflater.inflate(layout, parent, false);
        }

        EventData item = listItems.get(position);
        populateListItem(convertView, item);
        return convertView;
    }
}
