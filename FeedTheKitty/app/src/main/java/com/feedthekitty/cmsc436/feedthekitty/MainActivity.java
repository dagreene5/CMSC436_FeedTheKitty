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
import android.widget.Button;
import android.widget.Toast;

import com.facebook.*;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import com.firebase.client.Firebase;

import java.util.Arrays;


public class MainActivity extends AppCompatActivity {

    public static String firebaseURLKey = "f";
    public static String firebaseUrl = "https://amber-torch-7320.firebaseio.com/";
    public boolean loggedIn = false;
    public static final Integer REQUEST_LOGIN = 5;
    public static final String TAG = "MainActivity";
    Button eventSearchButton;
    Button addTestEventButton;
    Firebase database;

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

        if (requestCode == REQUEST_LOGIN) {

            if (resultCode == RESULT_OK) {

                Log.i(TAG, "AUTH_ID passed back to main activity: " +
                        data.getExtras().getString(FacebookActivity.AUTH_ID));
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
