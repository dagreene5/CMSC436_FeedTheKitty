package com.feedthekitty.cmsc436.feedthekitty;

import android.app.ListActivity;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.firebase.client.Firebase;
import com.firebase.client.Query;

import java.util.ArrayList;


public class MainActivity extends ListActivity {

    public static String firebaseURLKey = "f";
    public static String firebaseUrl = "https://amber-torch-7320.firebaseio.com/";
    public static final Integer REQUEST_LOGIN = 5;
    static private final int GET_EVENT_REQUEST_CODE = 1;
    public static final String TAG = "MainActivity";
    public static final String UID_KEY = "uidk";
    Button browseBtn, createBtn;
    Firebase database;
    FirebaseUtils firebaseUtils;
    EventListAdapter eventListAdapter;
    private ListView listView;

    public ListView userList;

    public static String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestLogin();

        Firebase.setAndroidContext(this);

        database = new Firebase(MainActivity.firebaseUrl);

        // Grab reference to FirebaseUtils to store data
        firebaseUtils = FirebaseUtils.getInstance();

        //If logged in via FB
        setContentView(R.layout.homeview);

        browseBtn = (Button) findViewById(R.id.browse_btn);
        createBtn = (Button) findViewById(R.id.create_btn);

        browseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent intent = new Intent(MainActivity.this, browse.class);
                // startActivity(intent);
                Intent intent = new Intent(MainActivity.this, EventLayout.class);
                startActivity(intent);
            }
        });

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateEventActivity.class);
                intent.putExtra(UID_KEY, uid);
                startActivityForResult(intent, GET_EVENT_REQUEST_CODE);
            }
        });

        listView = getListView();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final EventData data = (EventData) eventListAdapter.getItem(position);

                if (data != null) {
                    Intent intent = data.packageIntoIntent();
                    intent.setClass(MainActivity.this, EventLayout.class);
                    startActivity(intent);
                }
            }
        });
        displayUserData();

    }

    private void displayUserData() {
        if (uid != null) {
            UserData userData = firebaseUtils.fetchUserData(uid);
            ArrayList<CharSequence> eventKeys = userData.getEventsAttending();
            ArrayList<EventData> events = new ArrayList<EventData>();

            for (CharSequence key : eventKeys) {
                events.add(firebaseUtils.fetchEventData(key.toString()));
            }

            eventListAdapter = new EventListAdapter(events, this);
            setListAdapter(eventListAdapter);
            eventListAdapter.notifyDataSetChanged();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        displayUserData();
    }
    private void requestLogin() {
        if (uid == null) {
            Intent intent = new Intent(this, FacebookActivity.class);
            startActivityForResult(intent, REQUEST_LOGIN);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_LOGIN) {

            if (resultCode == RESULT_OK) {

                uid = data.getExtras().getString(FacebookActivity.AUTH_ID);
                Log.i(TAG, "AUTH_ID passed back to main activity: " + uid);

            } else if (resultCode == RESULT_CANCELED ||
                    resultCode == FacebookActivity.RESULT_ERROR) {

                // Failed/canceled. Ask to login again
                requestLogin();
            }
        }

        if (requestCode == GET_EVENT_REQUEST_CODE) {

            if (resultCode == RESULT_OK) {
                EventData eventData = EventData.createFromIntent(data);
                firebaseUtils.createEventData(eventData);
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