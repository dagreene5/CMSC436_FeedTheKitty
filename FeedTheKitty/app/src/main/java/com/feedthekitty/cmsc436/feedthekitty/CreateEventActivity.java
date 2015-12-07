package com.feedthekitty.cmsc436.feedthekitty;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Sara Leroy on 12/3/2015.
 */
public class CreateEventActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText getEventTime;
    private EditText getEventDate;
    private EditText getEventEndDate;
    private EditText getEventEndTime;

    private EditText defaultContribution;
    private EditText venmoName;

    private DatePickerDialog eventDate;
    private DatePickerDialog eventEndDate;

    private TimePickerDialog eventTime;
    private TimePickerDialog eventEndTime;

    private EditText eventName;
    private EditText location;
    private EditText description;
    private EditText hashTag;
    private EditText moneyToRaise;

    private String uid;

    private Button createEvent;
    SimpleDateFormat simpleDateFormat;
    DateFormat simpleTimeFormat;

    Calendar calendar = Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        venmoName = (EditText) findViewById(R.id.venmoName);
        defaultContribution = (EditText) findViewById(R.id.defaultContribution);
        createEvent = (Button) findViewById(R.id.done);
        createEvent.setOnClickListener(this);
        /** Initialize input fileds */
        eventName = (EditText) findViewById(R.id.event_name);
        eventName.setOnClickListener(this);
        location = (EditText) findViewById(R.id.location);
        location.setOnClickListener(this);
        description = (EditText) findViewById(R.id.description);
        description.setOnClickListener(this);
        hashTag = (EditText) findViewById(R.id.event_create_hashtags);
        hashTag.setOnClickListener(this);
        moneyToRaise = (EditText) findViewById(R.id.money_to_raise);
        moneyToRaise.setOnClickListener(this);

        /** Initialize Fields for datepicker and Timepicker */
        simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy", Locale.US);
        simpleTimeFormat = DateFormat.getTimeInstance();

        getEventTime = (EditText) findViewById(R.id.event_time);
        getEventTime.setOnClickListener(this);
        getEventTime.setInputType((InputType.TYPE_NULL));

        getEventEndTime = (EditText) findViewById(R.id.event_deadline_time);
        getEventEndTime.setOnClickListener(this);
        getEventEndTime.setInputType(InputType.TYPE_NULL);

        getEventDate = (EditText) findViewById(R.id.event_date);
        getEventDate.setOnClickListener(this);
        getEventDate.setInputType(InputType.TYPE_NULL);
        getEventDate.requestFocus();

        getEventEndDate = (EditText) findViewById(R.id.event_deadline_date);
        getEventEndDate.setOnClickListener(this);
        getEventEndDate.setInputType(InputType.TYPE_NULL);

        createCalendar();
        createTimePicker();
    }

    /** This sets up the time picker dialog */
    public void createTimePicker () {
        eventTime = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {

            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                Calendar time = Calendar.getInstance();
                time.set(Calendar.HOUR_OF_DAY, hourOfDay);
                time.set(Calendar.MINUTE, minute);
                getEventTime.setText(simpleTimeFormat.format(time.getTime()));
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);

        eventEndTime = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                Calendar time = Calendar.getInstance();
                time.set(Calendar.HOUR_OF_DAY, hourOfDay);
                time.set(Calendar.MINUTE, minute);
                getEventEndTime.setText(simpleTimeFormat.format(time.getTime()));
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);
    }

    /** this sets up the date picker dialog */
    public void createCalendar() {
        eventDate = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                Calendar date = Calendar.getInstance();
                date.set(year, monthOfYear, dayOfMonth);
                getEventDate.setText(simpleDateFormat.format(date.getTime()));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        eventEndDate = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                Calendar date = Calendar.getInstance();
                date.set(year, monthOfYear, dayOfMonth);
                getEventEndDate.setText(simpleDateFormat.format(date.getTime()));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));


    }


    public void onClick(View view) {
        if (view == getEventDate) {
            eventDate.show();
        } else if (view == getEventEndDate) {
            eventEndDate.show();
        } else if (view == getEventTime) {
            eventTime.show();
        } else if (view == getEventEndTime) {
            eventEndTime.show();
        } else if (view == createEvent) {

            EventData eventData = new EventData();
            uid = (String) getIntent().getExtras().get(MainActivity.UID_KEY);

            if ((getEventDate.getText().toString().matches("")) ||
                    (getEventEndDate.getText().toString().matches(""))
                || (getEventTime.getText().toString().matches("")) ||
                    (getEventEndTime.getText().toString().matches(""))
                || (defaultContribution.getText().toString().matches(""))
                    || (venmoName.getText().toString().matches(""))
                || (eventName.getText().toString().matches("")) ||
                    (hashTag.getText().toString().matches("")) ||
                    (moneyToRaise.getText().toString().matches(""))
                    || (description.getText().toString().matches(""))
                || (location.getText().toString().matches(""))
                    || (uid.matches(""))) {
                Toast.makeText(getApplicationContext(), "All fields must be initialized with valid inputs",
                        Toast.LENGTH_LONG).show();
            } else {
                eventData.setDefaultContribution(Integer.valueOf(defaultContribution.getText()
                        .toString()));
                eventData.setEventKey(uid);
                eventData.setEventStartDate(getEventDate.getText().toString());
                eventData.setEventEndDate(getEventEndDate.getText().toString());
                eventData.setEventStartTime(getEventTime.getText().toString());
                eventData.setEventEndTime(getEventEndTime.getText().toString());
                eventData.setDefaultContribution(Integer.valueOf(defaultContribution.getText()
                        .toString()));
                eventData.setVenmoName(venmoName.getText().toString());
                eventData.setTitle(eventName.getText().toString());
                eventData.setHashtag(hashTag.getText().toString());
                eventData.setAmountNeeded(moneyToRaise.getText().toString());
                eventData.setDescription(description.getText().toString());
                eventData.setLocation(location.getText().toString());

                if (eventData.isValid() == false) {
                    Toast.makeText(getApplicationContext(), "Invalid inputs",
                            Toast.LENGTH_LONG).show();
                } else {

                    this.setResult(RESULT_OK, eventData.packageIntoIntent());
                    finish();
                }
            }
        }

    }

}

