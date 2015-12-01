package com.feedthekitty.cmsc436.feedthekitty;

import android.content.Intent;
import android.hardware.camera2.params.Face;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.app.SearchManager;
import android.content.Context;
import android.support.v7.widget.SearchView;
import android.view.MenuInflater;

import com.firebase.client.Firebase;


public class MainActivity extends AppCompatActivity {

    public static String firebaseURLKey = "f";
    private String firebaseUrl = "https://amber-torch-7320.firebaseio.com/";
    public boolean loggedIn = false;
    public static final Integer REQUEST_LOGIN = 5;
    public static final String TAG = "MainActivity";
    Firebase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Firebase.setAndroidContext(this);
        //database = new Firebase(firebaseUrl);

        // Test Firebase
        /*Intent intent = new Intent(this, FirebaseTestActivity.class);
        intent.putExtra(firebaseURLKey, firebaseUrl);
        startActivity(intent);
        */

        // Test Facebook Login
        /*Intent intent = new Intent(this, FacebookLogin.class);
        startActivityForResult(intent, REQUEST_LOGIN);
        */


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_LOGIN) {

            if (resultCode == RESULT_OK) {

                Log.i(TAG, "AUTH_ID passed back to main activity: " +
                        data.getExtras().getString(FacebookLogin.AUTH_ID));
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
