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

    private String eventImage;
    private String eventText;
    private String TAG = "EventData";

    public void setEventImage(String eventImage) {
        this.eventImage = eventImage;
    }

    public String getEventImage() {
        return eventImage;
    }

    public void setEventText(String eventText) {
        this.eventText = eventText;
    }

    public String getEventText() {
        return eventText;
    }

    public void setEventImage(Bitmap image) {

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.PNG, 100, baos);
            image.recycle();
            byte[] byteArray = baos.toByteArray();
            eventImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
        } catch (Exception e) {
            Log.e(TAG, "Event data unable to convert image format");
        }
    }

    public Bitmap getEventImageBitmap() {
        byte[] byteArray = Base64.decode(eventImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }
}
