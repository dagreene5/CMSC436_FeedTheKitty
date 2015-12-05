package com.feedthekitty.cmsc436.feedthekitty;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import java.util.ArrayList;

/**
 * Created by davidgreene on 12/5/15.
 */
public class UserListAdapter  extends BaseAdapter {

    private ArrayList<UserData> listItems;
    private ArrayList<String> keys;
    private Query dataRef;private LayoutInflater inflater;
    private int layout;
    private Activity activity;
    private ChildEventListener listener;
    public static final String TAG = "UserListAdapter";

    //TODO add ability to refine search as letters are added to serach term

    /**
     *
     * @param dataRef
     * @param layout
     * @param activity
     * @param searchTerm
     */
    public UserListAdapter(Query dataRef, int layout, Activity activity, final String searchTerm) {

        this.dataRef = dataRef;
        this.layout = layout;
        this.activity = activity;

        inflater = activity.getLayoutInflater();

        listItems = new ArrayList<UserData>();
        keys = new ArrayList<String>();

        // No name searched, display all users
        if (searchTerm == null || searchTerm.equals("")) {
            listener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String lastKey) {

                    UserData item = dataSnapshot.getValue(UserData.class);

                    grabItem(dataSnapshot, lastKey, item);
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
        } else {
            listener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String lastKey) {

                    UserData item = dataSnapshot.getValue(UserData.class);

                    if (item.getFullName().toLowerCase().contains(searchTerm.toLowerCase())) {
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

    public void grabItem(DataSnapshot dataSnapshot, String lastKey, UserData item) {
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

    public void populateListItem(View view, UserData data) {

        TextView fullName = (TextView) view.findViewById(R.id.text_full_name);
        //ImageView userImage = (ImageView) view.findViewById(R.id.image_user);

        fullName.setText(data.getFullName());
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

        UserData item = listItems.get(position);
        populateListItem(convertView, item);

        return convertView;
    }

}
