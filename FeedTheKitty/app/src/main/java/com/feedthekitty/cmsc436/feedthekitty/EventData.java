package com.feedthekitty.cmsc436.feedthekitty;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * Created by davidgreene on 11/20/15.
 *
 */
public class EventData {

    private String eventImage;
    private String title;
    private String hashtag;
    private Long stackId;
    private Integer funds;
    private String description;
    private List<String> peopleInvited;
    private List<String> peopleAttending;

    public void setStackId(long stackId) {
        this.stackId = stackId;
    }
    public long getStackId() {
        return stackId;
    }
    public void setEventImage(String eventImage) {
        this.eventImage = eventImage;
    }
    public String getEventImage() {
        return eventImage;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getTitle() {
        return title;
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
    public void setPeopleInvited(List<String> peopleInvited) {
        this.peopleInvited = peopleInvited;
    }
    public List<String> getPeopleInvited() {
        return peopleInvited;
    }
    public void setPeopleAttending(List<String> peopleAttending) {
        this.peopleAttending = peopleAttending;
    }
    public List<String> getPeopleAttending() {
        return peopleAttending;
    }


    public String toString() {
        return "Event title: " + title + ", hashtag: " + hashtag;
    }
}
