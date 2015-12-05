package com.feedthekitty.cmsc436.feedthekitty;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;

import com.firebase.client.Firebase;
import com.firebase.client.Query;

import java.util.ArrayList;

/**
 * Created by davidgreene on 12/1/15.
 */
public class EventSearchActivity extends ListActivity {

    Button searchButton;
    EditText searchText;
    RadioButton radioButtonHashtag;
    RadioButton radioButtonTitle;
    FirebaseUtils firebaseUtils = FirebaseUtils.getInstance();

    boolean checkHashtag = false;
    boolean checkTitle = false;

    EventListAdapter adapter = null;
    Firebase database;

    ListView listView;

    String TAG = "EventSearchActivity";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_event_search);
        database = new Firebase(MainActivity.firebaseUrl);

        adapter = new EventListAdapter(firebaseUtils.getAllEvents(), this);
        adapter.notifyDataSetChanged();

        listView = getListView();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final EventData data = (EventData) adapter.getItem(position);

                if (data != null) {
                    Intent intent = data.packageIntoIntent();
                    intent.setClass(EventSearchActivity.this, EventLayout.class);
                    startActivity(intent);
                }
            }
        });

        // View initialization
        searchButton = (Button) findViewById(R.id.button_search);
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

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, "In search button listener");

                // get new search string
                String text = searchText.getText().toString();

                if (text == null || text.equals("")) {
                    adapter.resetList();
                    return;
                }

                if (checkHashtag) {
                    adapter.filterByHashtag(text);
                } else if (checkTitle) {
                    adapter.filterByTitle(text);
                }
            }
        });
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

    //TODO this might cause issues
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_home) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivityForResult(intent, 5);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
