package com.feedthekitty.cmsc436.feedthekitty;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import java.util.ArrayList;

/**
 * Created by davidgreene on 12/2/15.
 */
public class FirebaseListAdapter<T> extends BaseAdapter {

    Intent intent;
    Firebase database;
    ChildEventListener lastTitleListener = null;
    ChildEventListener lastHashtagListener = null;
    public static final Integer SEARCH_FOR_HASHTAG_REQUEST = 5;
    public static final String HASHTAG_SEARCH = "hs";
    public static final Integer SEARCH_FOR_TITLE_REQUEST = 4;
    public static final String SEARCH_KEYWORD = "sk";
    public static final String EVENT_KEYS_KEY = "ek";
    public static final String TITLE_KEY = "t";
    public static final String HASHTAG_KEY = "h";

    private ArrayList<T> listItems;
    private ArrayList<String> keys;
    private Query dataRef;
    private Class<T> clazz;
    private LayoutInflater inflater;
    private int layout;
    private Activity activity;
    private ChildEventListener listener;


    public FirebaseListAdapter(Query dataRef, Class<T> clazz, int layout, Activity activity) {
        this.dataRef = dataRef;
        this.clazz = clazz;
        this.layout = layout;
        this.activity = activity;

        inflater = activity.getLayoutInflater();

        listItems = new ArrayList<T>();
        keys = new ArrayList<String>();

        listener = this.dataRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                T item = dataSnapshot.getValue(FirebaseListAdapter.this.clazz);
                String key = dataSnapshot.getKey();

                if (s == null) {
                    listItems.add(0, item);
                    keys.add(0, key);
                } else {
                    int prevIndex = keys.indexOf(s);
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
        });


    }

    public void close() {
        dataRef.removeEventListener(listener);
        listItems.clear();
        keys.clear();
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

        T item = listItems.get(position);
        populateListItem(convertView, item);
        return convertView;
    }

    public void populateListItem(View view, T item) {

        // implement this

    }
}
