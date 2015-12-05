package com.feedthekitty.cmsc436.feedthekitty;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.Firebase;

import java.util.HashMap;

/**
 * Created by davidgreene on 12/2/15.
 *
 */
public class TestCreateEventActivity extends Activity {

    Button storeButton;
    EditText title;
    EditText hashtag;
    FirebaseUtils firebaseUtils;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_test_create_event);

        storeButton = (Button) findViewById(R.id.button_store);
        title = (EditText) findViewById(R.id.text_store_title);
        hashtag = (EditText) findViewById(R.id.text_store_hashtag);

        firebaseUtils = FirebaseUtils.getInstance();

        storeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String titleText = title.getText().toString();
                String hashtagText = hashtag.getText().toString();

                if (titleText.equals("") || hashtagText.equals("")) {
                    return;
                }

                EventData eventData = new EventData();
                eventData.setTitle(titleText);
                eventData.setTitle(hashtagText);

                firebaseUtils.createEventMasterList(eventData);
            }
        });
    }
}
