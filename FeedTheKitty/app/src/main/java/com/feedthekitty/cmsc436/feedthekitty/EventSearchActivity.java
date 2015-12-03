package com.feedthekitty.cmsc436.feedthekitty;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;

import com.firebase.client.Firebase;
import com.firebase.client.Query;

import java.util.ArrayList;

/**
 * Created by davidgreene on 12/1/15.
 */
public class EventSearchActivity extends ListActivity {

    Button searchButton;
    Button showAllButton;
    EditText searchText;
    RadioButton radioButtonHashtag;
    RadioButton radioButtonTitle;

    boolean checkHashtag = false;
    boolean checkTitle = false;

    EventListAdapter adapter = null;
    Firebase database;

    String TAG = "EventSearchActivity";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_event_search);
        database = new Firebase(MainActivity.firebaseUrl);

        // View initialization
        searchButton = (Button) findViewById(R.id.button_search);
        showAllButton = (Button) findViewById(R.id.button_show_all);
        searchText = (EditText) findViewById(R.id.text_event_search);
        radioButtonHashtag = (RadioButton) findViewById(R.id.radioButton_search_hashtag);
        radioButtonTitle = (RadioButton) findViewById(R.id.radioButton_search_title);

        radioButtonHashtag.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkHashtag = isChecked;
            }
        });

        radioButtonTitle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkTitle = isChecked;
            }
        });

        //TODO remove this button
        showAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (adapter != null) {
                    adapter.close();
                }

                Query query = database.child("masterList");
                adapter = new EventListAdapter(query, R.layout.event_search_row,
                        EventSearchActivity.this, null, null);

                setListAdapter(adapter);
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, "In search button listener");

                // get new search string
                String text = searchText.getText().toString();

                if (adapter != null) {
                    adapter.close();
                }

                if (text == null || text.equals("")) {
                    return; // TODO clear list on empty search?
                }

                //TODO only allow one to be checked at a time

                if (checkHashtag) {
                    Log.d(TAG, "Searching for hashtag: " + text);
                    Query query = database.child("masterList");
                    adapter = new EventListAdapter(query, R.layout.event_search_row,
                            EventSearchActivity.this, text, EventListAdapter.HASHTAG_SEARCH);

                } else if (checkTitle) {
                    Log.d(TAG, "Searching for title: " + text);

                    Query query = database.child("masterList");
                    adapter = new EventListAdapter(query, R.layout.event_search_row,
                            EventSearchActivity.this, text, EventListAdapter.TITLE_SEARCH);
                }

                setListAdapter(adapter);
            }
        });
    }
}
