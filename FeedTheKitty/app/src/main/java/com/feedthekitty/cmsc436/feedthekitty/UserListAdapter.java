package com.feedthekitty.cmsc436.feedthekitty;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by davidgreene on 12/5/15.
 *
 */
public class UserListAdapter extends BaseAdapter {

    private ArrayList<UserData> originalList;
    private ArrayList<UserData> listItems;
    private LayoutInflater inflater;
    public static final String TAG = "EventListAdapter";

    public UserListAdapter(ArrayList<UserData> originalList, Activity activity) {
        this.originalList = originalList;
        this.listItems = originalList;

        inflater = activity.getLayoutInflater();
    }

    public void resetList() {
        this.listItems = originalList;
        notifyDataSetChanged();
    }

    //TODO first and last...
    public void filterByName(String name) {
        ArrayList<UserData> newList = new ArrayList<UserData>();

        for (UserData data : listItems) {
            if (data.getFullName().toLowerCase().contains(name.toLowerCase())) {
                newList.add(data);
            }
        }
        listItems = newList;
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
            convertView = inflater.inflate(R.layout.user_search_row, parent, false);
        }

        UserData item = listItems.get(position);
        populateListItem(convertView, item);
        return convertView;
    }
}