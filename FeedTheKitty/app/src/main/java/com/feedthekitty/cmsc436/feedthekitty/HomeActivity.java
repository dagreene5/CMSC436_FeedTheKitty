package com.feedthekitty.cmsc436.feedthekitty;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;

/**
 *  Homescreen of the app. User will be taken to this view when they're logged in via FB.
 *
 *  @Author     Sang Lee
 *  @Version    1.0
 */

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button browse_btn = (Button) findViewById(R.id.browse_btn);
        Button create_btn = (Button) findViewById(R.id.create_btn);

        //Listener on browse button
        browse_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Intent browse = new Intent(this, BrowseActivity.class);
                //startActivity(browse);
            }
        });

        //Listener on create button
        create_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Intent create = new Intent(this, CreateActivity.class);
                //startActivity(create);
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
}
