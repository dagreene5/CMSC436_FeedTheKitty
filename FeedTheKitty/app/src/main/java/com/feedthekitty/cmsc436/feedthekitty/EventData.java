package com.feedthekitty.cmsc436.feedthekitty;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import com.firebase.client.DataSnapshot;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by davidgreene on 11/20/15.
 *
 */
public class EventData {

    private String title;
    private String hashtag;
    private String location;
    private String amountNeeded;
    private String eventKey;
    //TODO
    private String eventStartTime;
    private String eventEndTime;
    private String eventStartDate;
    private String eventEndDate;
    private Long stackId;
    private Integer funds;
    private Integer defaultContribution;
    private String venmoName;
    private String description;
    private ArrayList<CharSequence> peopleAttending;

    public EventData() {
        funds = 0;
        peopleAttending = new ArrayList<CharSequence>();
        // other initialization here
    }

    public void removePersonAttending(String uid) {
        peopleAttending.remove(uid);
    }
    public void addFunds(Integer addAmount) {
        funds += addAmount;
    }

    public void setDefaultContribution(Integer defaultContribution) {
        this.defaultContribution = defaultContribution;
    }

    public Integer getDefaultContribution() {
        return defaultContribution;
    }

    public void setVenmoName(String venmoName) {
        this.venmoName = venmoName;
    }

    public String getVenmoName() {
        return venmoName;
    }
    public void setEventKey(String eventKey) {
        this.eventKey = eventKey;
    }

    public String getEventKey() {
        return eventKey;
    }

    public void setStackId(long stackId) {
        this.stackId = stackId;
    }

    public long getStackId() {
        return stackId;
    }

    public void setEventStartTime(String eventStartTime) {
        this.eventStartTime = eventStartTime;
    }

    public String getEventStartTime() {
        return eventStartTime;
    }

    public void setAmountNeeded(String amount) {
        this.amountNeeded = amount;
    }

    public String getAmountNeeded() {
        return amountNeeded;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setEventStartDate(String eventStartDate) {
        this.eventStartDate = eventStartDate;
    }

    public String getEventStartDate() {
        return eventStartDate;
    }

    public void setEventEndDate(String eventEndDate) {
        this.eventEndDate = eventEndDate;
    }

    public String getEventEndDate() {
        return eventEndDate;
    }

    public void setEventEndTime(String eventEndTime) {
        this.eventEndTime = eventEndTime;    }

    public String getEventEndTime() {
        return eventEndTime;
    }

    public void setHashtag(String hashtag) {
        this.hashtag = hashtag;
    }

    public String getHashtag() {
        return hashtag;
    }

    public void setFunds(Integer funds) {
        this.funds = funds;
    }

    public Integer getFunds() {
        return funds;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setPeopleAttending(ArrayList<CharSequence> peopleAttending) {
        this.peopleAttending = peopleAttending;
    }

    public ArrayList<CharSequence> getPeopleAttending() {
        return peopleAttending;
    }

    public Intent packageIntoIntent() {
        Intent intent = new Intent();

        intent.putExtra("eventKey", eventKey);
        intent.putExtra("title", title);
        intent.putExtra("hashtag", hashtag);
        intent.putExtra("description", description);
        intent.putExtra("eventEndDate", eventEndDate);
        intent.putExtra("eventStartDate", eventStartDate);
        intent.putExtra("eventEndTime", eventEndTime);
        intent.putExtra("eventStartTime", eventStartTime);
        intent.putExtra("location", location);
        intent.putExtra("amountNeeded", amountNeeded);
        intent.putExtra("funds", funds);
        intent.putExtra("venmoName", venmoName);
        intent.putExtra("defaultContribution", defaultContribution);
        intent.putExtra("peopleAttending", peopleAttending);

        return intent;
    }

    public Map<String, Object> packageIntoMap() {

        HashMap<String, Object> data = new HashMap<String, Object>();

        data.put("title", title);
        data.put("hashtag", hashtag);
        data.put("description", description);
        data.put("eventEndDate", eventEndDate);
        data.put("eventStartDate", eventStartDate);
        data.put("eventEndTime", eventEndTime);
        data.put("eventStartTime", eventStartTime);
        data.put("location", location);
        data.put("amountNeeded", amountNeeded);
        data.put("funds", funds);
        data.put("peopleAttending", peopleAttending);
        data.put("venmoName", venmoName);
        data.put("defaultContribution", defaultContribution);

        return data;
    }

    public static EventData createFromIntent(Intent intent) {

        Bundle extras = intent.getExtras();
        EventData eventData = new EventData();

        eventData.setEventKey(extras.getString("eventKey"));
        eventData.setTitle(extras.getString("title"));
        eventData.setHashtag(extras.getString("hashtag"));
        eventData.setDescription(extras.getString("description"));
        eventData.setEventEndDate(extras.getString("eventEndDate"));
        eventData.setEventStartDate(extras.getString("eventStartDate"));
        eventData.setEventEndTime(extras.getString("eventEndTime"));
        eventData.setEventStartTime(extras.getString("eventStartTime"));
        eventData.setLocation(extras.getString("location"));
        eventData.setAmountNeeded(extras.getString("amountNeeded"));
        eventData.setFunds(extras.getInt("funds"));
        eventData.setPeopleAttending(extras.getCharSequenceArrayList("peopleAttending"));
        eventData.setVenmoName(extras.getString("venmoName"));
        eventData.setDefaultContribution(extras.getInt("defaultContribution"));

        return eventData;
    }

    public boolean isValid() {

        return validStringField(title) && validStringField(location)
                && validStringField(amountNeeded) && validStringField(eventKey) &&
                validStringField(eventStartTime) && validStringField(eventEndTime) &&
                validStringField(eventStartDate) && validStringField(eventEndDate) &&
                validIntField(funds) && validIntField(defaultContribution) &&
                validStringField(venmoName) && validStringField(description);
    }

    public void addPersonAttending(String uid) {
        if (!peopleAttending.contains(uid)) {
            peopleAttending.add(uid);
        }
    }

    private boolean validStringField(String field) {
        //TODO other checks?
        return field != null &&  !field.equals("");
    }

    private boolean validIntField(Integer field) {
        //TODO other checks?
        return field != null;
    }

    public String toString() {
        return packageIntoMap().toString();
    }
}
