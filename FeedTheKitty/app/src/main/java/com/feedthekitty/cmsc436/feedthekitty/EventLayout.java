package com.feedthekitty.cmsc436.feedthekitty;

import android.app.ListActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import com.feedthekitty.cmsc436.feedthekitty.VenmoLibrary;

import android.content.Intent;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Sa on 12/4/2015.
 */
public class EventLayout extends ListActivity implements View.OnClickListener {

    TextView eventName;
    TextView eventHashtag;
    TextView eventStartDate;
    TextView eventEndDate;
    TextView funds;//how much has been raised so far
    TextView description;
    TextView amountNeeded; //how much is needed in total
    TextView eventStartTime;
    TextView eventEndTime;
    TextView defaultContribution;
    Button donate;
    Button attendEvent;
    TextView location;
    EventData eventData;
    FirebaseUtils firebaseUtils = FirebaseUtils.getInstance();

    boolean attendingEvent;

    //Venmo Specific Variables
    private static final int REQUEST_CODE_VENMO_APP_SWITCH = 1;
    private static final String APP_ID = "3248";  //generated by Venmo 4 numbers
    private static final String APP_NAME = "FeedTheKitty";
    private static final String APP_SECRET = "vr7QR9gSEZb8jVuvJs6Xk4SBcr5Cbqnp"; //generated by Venmo

    public static final String TAG = "EventLayout";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_layout);

        eventData = EventData.createFromIntent(getIntent());

        // check if already attending. Only changes the text of the
        // attend event button to say remove from event, and change the
        // on click handling
        attendingEvent = eventData.getPeopleAttending().contains(MainActivity.uid);

        Log.i(TAG, "Recovered EventData in EventLayout: " + eventData.toString());

        eventName = (TextView) findViewById(R.id.event_layout_title);
        eventName.setText(eventData.getTitle());

        eventHashtag = (TextView) findViewById(R.id.event_layout_hashtag);
        eventHashtag.setText(eventData.getHashtag());

        description = (TextView) findViewById(R.id.event_layout_decription);
        description.setText(eventData.getDescription());

        funds = (TextView) findViewById(R.id.event_layout_funds);
        funds.setText(eventData.getFunds().toString());

        amountNeeded = (TextView) findViewById(R.id.event_layout_amount_needed);
        amountNeeded.setText(eventData.getAmountNeeded());

        eventStartTime = (TextView) findViewById(R.id.event_start_time);
        eventStartTime.setText(eventData.getEventStartTime());

        eventEndTime = (TextView) findViewById(R.id.event_end_time);
        eventEndTime.setText(eventData.getEventEndTime());

        eventStartDate = (TextView) findViewById(R.id.event_start_date);
        eventStartDate.setText(eventData.getEventStartDate());

        eventEndDate = (TextView) findViewById(R.id.event_end_date);
        eventEndDate.setText(eventData.getEventEndDate());

        location = (TextView) findViewById(R.id.event_layout_location);
        location.setText(eventData.getLocation());

        defaultContribution = (TextView) findViewById(R.id.event_layout_default_contribution);
        defaultContribution.setText(eventData.getDefaultContribution().toString());

        donate = (Button) findViewById(R.id.donation);
        donate.setOnClickListener(this);

        attendEvent = (Button) findViewById(R.id.attendEvent);
        attendEvent.setOnClickListener(this);

        if (attendingEvent) {
            attendEvent.setText("Remove Me From Event");
        } else {
            attendEvent.setText("Attend Event");
        }

        location = (TextView) findViewById(R.id.event_layout_location);
        location.setText( eventData.getLocation() );

        displayPeopleAttending();


    }

    @Override
    protected void onResume() {
        super.onResume();

        displayPeopleAttending();
    }

    private void displayPeopleAttending() {
        eventData = firebaseUtils.fetchEventData(eventData.getEventKey());
        ArrayList<CharSequence> peopleKeys = eventData.getPeopleAttending();
        ArrayList<UserData> people = new ArrayList<UserData>();

        for (CharSequence key : peopleKeys) {
            people.add(firebaseUtils.fetchUserData(key.toString()));
        }

        UserListAdapter userListAdapter = new UserListAdapter(people, this);
        setListAdapter(userListAdapter);
        userListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        if (view == donate) {
            if (VenmoLibrary.isVenmoInstalled(getApplicationContext()) == true ) {
                String message = "For event: " + eventData.getTitle();
                Intent venmoIntent = VenmoLibrary.openVenmoPayment(APP_ID,
                        APP_NAME, eventData.getVenmoName(),
                        eventData.getDefaultContribution().toString(), message, "pay");
                startActivityForResult(venmoIntent, REQUEST_CODE_VENMO_APP_SWITCH);
            } else {
                Toast.makeText(getApplicationContext(), "Please download Venmo from the Google Play Store.",
                        Toast.LENGTH_LONG).show();
            }
        }

        if (view == attendEvent) {

            if (attendingEvent) {
                firebaseUtils.removeUserFromEvent(MainActivity.uid, eventData.getEventKey());
                attendingEvent = false;
                attendEvent.setText("Attend Event");
            } else {
                firebaseUtils.addUserToEvent(MainActivity.uid, eventData.getEventKey());
                attendingEvent = true;
                attendEvent.setText("Remove Me From Event");
            }

            displayPeopleAttending();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch(requestCode) {
            case REQUEST_CODE_VENMO_APP_SWITCH: {
                if(resultCode == RESULT_OK) {
                    String signedrequest = data.getStringExtra("signedrequest");
                    if(signedrequest != null) {
                        VenmoLibrary.VenmoResponse response = (new VenmoLibrary()).validateVenmoPaymentResponse(signedrequest, APP_SECRET);
                        if(response.getSuccess().equals("1")) {
                            //Payment successful.  Use data from response object to display a success message
                            String note = response.getNote();
                            String amount = response.getAmount();
                            Toast.makeText(getApplicationContext(), "You successfully paid " + amount + "to "
                                    + eventData.getTitle(), Toast.LENGTH_LONG).show();
                            Double prevAmount = Double.valueOf(eventData.getFunds());
                            Double added = Double.valueOf(amount);
                            String newTotal = String.valueOf(prevAmount + added);
                            eventData.setFunds(newTotal);
                            firebaseUtils.updateEventData(eventData);
                            funds.setText(newTotal);
                        }
                    }
                    else {
                        String error_message = data.getStringExtra("error_message");
                        //An error ocurred.  Make sure to display the error_message to the user
                        Toast.makeText(getApplicationContext(), "Sorry an uknown error occured.", Toast.LENGTH_LONG).show();
                    }
                }
                else if(resultCode == RESULT_CANCELED) {
                    //The user cancelled the payment
                    Toast.makeText(getApplicationContext(), "Payment Canceled",
                            Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

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
            startActivityForResult(intent, 5);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
