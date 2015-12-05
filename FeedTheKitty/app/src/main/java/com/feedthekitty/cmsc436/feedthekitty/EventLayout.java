package com.feedthekitty.cmsc436.feedthekitty;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Sa on 12/4/2015.
 */
public class EventLayout extends AppCompatActivity implements View.OnClickListener {

    TextView eventName;
    ImageView image;
    TextView moneyRaised;
    TextView description;
    Button donate;
    Button inviteUsers;
    TextView location;
    EventData eventData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_layout);

        /* Todo Link Each EventData object with this
         * Set variables to hold event data values
         */
        eventData = EventData.createFromIntent(getIntent());

        eventName = (TextView) findViewById(R.id.name);
        eventName.setText( eventData.getTitle() );

        image = (ImageView) findViewById(R.id.app_image);
        image.setImageBitmap( FirebaseUtils.stringToBitmap(eventData.getEventImage()) );

        moneyRaised = (TextView) findViewById(R.id.money_total);
        moneyRaised.setText( eventData.getFunds() + "out of" + eventData.getAmountNeeded() );

        description = (TextView) findViewById(R.id.description);
        description.setText( eventData.getDescription() );

        donate = (Button) findViewById(R.id.donation);
        donate.setOnClickListener(this);

        inviteUsers = (Button) findViewById(R.id.inviteUsers);
        inviteUsers.setOnClickListener(this);

        location = (TextView) findViewById(R.id.event_location);
        location.setText( eventData.getLocation() );
    }

    @Override
    public void onClick(View view) {
        if (view == donate) {
            //TODO link to Venmo
        }

        if (view == inviteUsers) {
            Intent intent = new Intent(this, UserSearchActivity.class);
            startActivityForResult(intent, UserSearchActivity.INVITE_LIST);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == UserSearchActivity.INVITE_LIST) {
            if (resultCode == RESULT_OK) {

                Bundle extras = data.getExtras();
                ArrayList<String> invitedList = (ArrayList<String>)
                        extras.get(UserSearchActivity.INVITED_USERS);

                for (String uid : invitedList) {
                    FirebaseUtils.getInstance().inviteToEvent(uid, eventData.getStackId());
                }

            }
        }
    }
}
