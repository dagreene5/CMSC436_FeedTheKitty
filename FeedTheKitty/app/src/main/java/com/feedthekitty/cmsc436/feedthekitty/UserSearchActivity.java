package com.feedthekitty.cmsc436.feedthekitty;

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
import java.util.HashSet;

/**
 * Created by davidgreene on 12/5/15.
 */
public class UserSearchActivity extends ListActivity {

    Button searchButton;
    Button finishInvitingButton;
    EditText searchText;
    ListView listView;

    UserListAdapter adapter = null;
    Firebase database;
    private ArrayList<String> invitedUsers = new ArrayList<String>();

    public static final Integer INVITE_LIST = 5;
    public static final String INVITED_USERS = "iu";

    String TAG = "UserSearchActivity";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user_search);
        database = new Firebase(MainActivity.firebaseUrl);

        // View initialization
        searchButton = (Button) findViewById(R.id.button_search);
        finishInvitingButton = (Button) findViewById(R.id.button_finish);
        searchText = (EditText) findViewById(R.id.text_user_search);
        listView = getListView();

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, "In search button listener");

                // get new search string
                String text = searchText.getText().toString();

                if (adapter != null) {
                    adapter.close();
                }

                Query query = database.child(FirebaseUtils.userRoot);
                adapter = new UserListAdapter(query, R.layout.user_search_row,
                        UserSearchActivity.this, text);

                setListAdapter(adapter);
            }
        });

        finishInvitingButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                Intent intent = new Intent();

                HashSet<String> invitedSet = new HashSet<String>(invitedUsers);
                invitedUsers.clear();
                invitedUsers.addAll(invitedSet);

                intent.putStringArrayListExtra(INVITED_USERS, invitedUsers);

                setResult(RESULT_OK, intent);
                finish();
            }

        });

        /* Initially display all users */
        Query query = database.child(FirebaseUtils.userRoot);
        adapter = new UserListAdapter(query, R.layout.user_search_row,
                UserSearchActivity.this, null);

        setListAdapter(adapter);



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final UserData data = (UserData) adapter.getItem(position);

                if (data != null) {

                    String name = data.getFullName();

                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:

                                    invitedUsers.add(data.getUserId());
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    //No button clicked
                                    break;
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(UserSearchActivity.this);
                    builder.setMessage("Invite " + name + "?").setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();

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

    // TODO this might cause issues
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
