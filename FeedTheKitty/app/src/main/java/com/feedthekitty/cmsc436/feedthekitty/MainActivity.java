package com.feedthekitty.cmsc436.feedthekitty;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.app.SearchManager;
import android.content.Context;
import android.support.v7.widget.SearchView;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ListView;

import com.facebook.*;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import com.firebase.client.Firebase;
import com.firebase.client.Query;

import java.util.Arrays;


public class MainActivity extends AppCompatActivity {

    public static String firebaseURLKey = "f";
    public static String firebaseUrl = "https://amber-torch-7320.firebaseio.com/";
    public boolean loggedIn = false;
    public static final Integer REQUEST_LOGIN = 5;
    static private final int GET_EVENT_REQUEST_CODE = 1;
    public static final String TAG = "MainActivity";
    Button eventSearchButton, addTestEventButton, browseBtn, createBtn;
    Firebase database;
    FirebaseUtils firebaseUtils;

    public ListView userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Firebase.setAndroidContext(this);

        Intent intent = new Intent(this, FacebookActivity.class);
        startActivityForResult(intent, REQUEST_LOGIN);

        eventSearchButton = (Button) findViewById(R.id.button_search_all_events);
        addTestEventButton = (Button) findViewById(R.id.button_add_test_events);

        addTestEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TestCreateEventActivity.class);
                startActivity(intent);
            }
        });

        eventSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EventSearchActivity.class);
                startActivity(intent);
            }
        });

        //If logged in via FB
        /*
        setContentView(R.layout.homeview);

        browseBtn = (Button) findViewById(R.id.browse_btn);
        createBtn = (Button) findViewById(R.id.create_btn);

        browseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Intent intent = new Intent(MainActivity.this, browse.class);
               // startActivity(intent);
            }
        });

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(MainActivity.this, CreateEventActivity.class);
               startActivityForResult(intent, GET_EVENT_REQUEST_CODE);
            }
        });

        database = new Firebase(MainActivity.firebaseUrl);
        Query query = database.child("masterList"); //change this to userList later
        EventListAdapter adapter = new EventListAdapter(query, R.layout.event_search_row, this, null, null);
        userList = (ListView) findViewById(R.id.list);
        userList.setAdapter(adapter);
        */

        // Test Storing data
        /*
        Intent intent = new Intent(this, TestCreateEventActivity.class);
        startActivity(intent);
        */

        // Test Searching for list of events
        /*
        Intent intent = new Intent(this, EventSearchActivity.class);
        startActivity(intent);
         */
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {

            if (requestCode == REQUEST_LOGIN) {

                Log.i(TAG, "AUTH_ID passed back to main activity: " +
                        data.getExtras().getString(FacebookActivity.AUTH_ID));
            }

            if (requestCode == GET_EVENT_REQUEST_CODE) {
                // access data like this ? data.getExtras().get("event"));
                EventData eventData = new EventData();
                firebaseUtils = FirebaseUtils.getInstance();
                Bundle extras = data.getExtras();
                eventData.setTitle(extras.getString("eventName"));
                eventData.setHashtag(extras.getString("hashtag"));
                eventData.setDescription(extras.getString("description"));
                eventData.setEventEndDate(extras.getString("eventEndDate"));
                eventData.setEventStartDate(extras.getString("eventStartDate"));
                eventData.setEventEndTime(extras.getString("eventEndTime"));
                eventData.setEventStartTime(extras.getString("eventStartTime"));
                eventData.setLocation(extras.getString("location"));
                eventData.setIsPrivate(extras.getBoolean("isPrivate"));
                firebaseUtils.createEventMasterList(eventData);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        // Inflate menu to add items to action bar if it is present.
        inflater.inflate(R.menu.menu_main, menu);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_home) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivityForResult(intent, REQUEST_LOGIN);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
