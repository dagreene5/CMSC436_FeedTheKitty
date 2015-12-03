package com.feedthekitty.cmsc436.feedthekitty;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;

/**
 * Created by davidgreene on 11/20/15.
 *
 */
public class EventData {

    private String TAG = "EventData";
    private String eventImage;
    private String eventText;
    private String title;
    private String hashtag;
    private Long stackId;
    private Integer money;

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

    public String toString() {
        return "Event title: " + title + ", hashtag: " + hashtag;
    }
}
