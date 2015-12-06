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

    private ArrayList<EventData> originalList;
    private ArrayList<EventData> listItems;
    private LayoutInflater inflater;
    public static final String TAG = "EventListAdapter";

    public EventListAdapter(ArrayList<EventData> originalList, Activity activity) {
        this.originalList = originalList;
        this.listItems = originalList;

        inflater = activity.getLayoutInflater();
    }

    public void resetList() {
        this.listItems = originalList;
        notifyDataSetChanged();
    }

    public void filterByHashtag(String hashtag) {
        ArrayList<EventData> newList = new ArrayList<EventData>();

        for (EventData data : listItems) {
            if (data.getHashtag().toLowerCase().contains(hashtag.toLowerCase())) {
                newList.add(data);
            }
        }
        listItems = newList;
        notifyDataSetChanged();
    }

    public void filterByTitle(String title) {
        ArrayList<EventData> newList = new ArrayList<EventData>();

        for (EventData data : listItems) {
            if (data.getTitle().toLowerCase().contains(title.toLowerCase())) {
                newList.add(data);
            }
        }
        listItems = newList;
        notifyDataSetChanged();
    }

    public void populateListItem(View view, EventData data) {

        TextView title = (TextView) view.findViewById(R.id.text_event_title);
        TextView hashtag = (TextView) view.findViewById(R.id.text_event_hashtag);
        TextView description = (TextView) view.findViewById(R.id.event_decription);

        title.setText(data.getTitle());
        hashtag.setText(data.getHashtag());
        description.setText(data.getDescription());
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
            convertView = inflater.inflate(R.layout.event_search_row, parent, false);
        }

        EventData item = listItems.get(position);
        populateListItem(convertView, item);
        return convertView;
    }
}
