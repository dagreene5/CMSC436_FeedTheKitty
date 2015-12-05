package com.feedthekitty.cmsc436.feedthekitty;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by Sa on 12/4/2015.
 */
public class EventLayout extends AppCompatActivity implements View.OnClickListener {

    TextView eventName;
    ImageView image;
    TextView moneyRaised;
    TextView description;
    Button donate;
    TextView location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_layout);

        /* Todo Link Each EventData object with this
         * Set variables to hold event data values
         */
        eventName = (TextView) findViewById(R.id.name);
        //eventName.setText( EventData.getTitle() );
        image = (ImageView) findViewById(R.id.app_image);
        //image.setImageBitmap( EventData.getEventImage() );
        moneyRaised = (TextView) findViewById(R.id.money_total);
        //moneyRaised.setText( EventData.getFunds + "out of" + EventData.getAmountNeeded() );
        description = (TextView) findViewById(R.id.description);
        //description.setText( EventData.getDescription() );
        donate = (Button) findViewById(R.id.donation);
        donate.setOnClickListener(this);

        location = (TextView) findViewById(R.id.event_location);
        //location.setText( EventData.getLocation() );
    }

    @Override
    public void onClick(View view) {
        if (view == donate) {
            //TODO link to Venmo
        }
    }
}
