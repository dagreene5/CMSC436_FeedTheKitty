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

    private DatePickerDialog eventDate;
    private DatePickerDialog eventEndDate;

    private TimePickerDialog eventTime;
    private TimePickerDialog eventEndTime;

    private EditText eventName;
    private EditText location;
    private CheckBox isPrivate;
    private EditText description;
    private EditText hashTag;

    private Button createEvent;
    SimpleDateFormat simpleDateFormat;
    DateFormat simpleTimeFormat;

    Intent data = new Intent();
    Calendar calendar = Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        createEvent = (Button) findViewById(R.id.done);
        createEvent.setOnClickListener(this);
        /** Initialize input fileds */
        eventName = (EditText) findViewById(R.id.event_name);
        eventName.setOnClickListener(this);
        location = (EditText) findViewById(R.id.location);
        location.setOnClickListener(this);
        isPrivate = (CheckBox) findViewById(R.id.set_privacy);
        isPrivate.setOnClickListener(this);
        description = (EditText) findViewById(R.id.description);
        description.setOnClickListener(this);
        hashTag = (EditText) findViewById(R.id.event_create_hashtags);
        hashTag.setOnClickListener(this);

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
            //data.putExtra("eventStartDate", getEventDate.getText());
        } else if (view == getEventEndDate) {
            eventEndDate.show();
            //data.putExtra("eventEndDate", getEventEndDate.getText());
        } else if (view == getEventTime) {
            eventTime.show();
            //data.putExtra("eventStartTime", getEventTime.getText());
        } else if (view == getEventEndTime) {
            eventEndTime.show();
            //data.putExtra("eventEndTime", getEventEndTime.getText());
        /*} else if (view == eventName) {
            data.putExtra("eventName", eventName.getText());
        } else if (view == location) {
            data.putExtra("location", location.getText());
        } else if (view == isPrivate) {
            data.putExtra("isPrivate", isPrivate.isChecked());
        } else if (view == description) {
            data.putExtra("description", description.getText());
        } else if (view == hashTag) {
            data.putExtra("hashtag", hashTag.getText());*/
        } else if (view == createEvent) {
            this.setResult(RESULT_OK, data);
            data.putExtra("eventStartDate", getEventDate.getText().toString());
            data.putExtra("eventEndDate", getEventEndDate.getText().toString());
            data.putExtra("eventStartTime", getEventTime.getText().toString());
            data.putExtra("eventEndTime", getEventEndTime.getText().toString());
            data.putExtra("location", location.getText().toString());
            data.putExtra("isPrivate", isPrivate.isChecked());
            data.putExtra("description", description.getText().toString());
            data.putExtra("hashtag", hashTag.getText().toString());
            // /finish activity
            finish();
        }

    }

}

