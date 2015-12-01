package com.feedthekitty.cmsc436.feedthekitty;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by davidgreene on 11/20/15.
 */
public class FirebaseTestActivity extends ListActivity {

    Firebase database;
    Intent intent;
    Button storeButton;
    Button fetchButton;
    EditText textBox;
    Bitmap stockImage;
    ArrayList<EventData> listItems = new ArrayList<EventData>();
    MyAdapter adapter;
    String IMAGE_KEY = "I";
    String TEXT_KEY = "T";
    String TAG = "FirebaseTestActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_test);

        Firebase.setAndroidContext(this);
        intent = getIntent();

        adapter = new MyAdapter(this, listItems);
        setListAdapter(adapter);

        Firebase.setAndroidContext(this);
        database = new Firebase(intent.getStringExtra(MainActivity.firebaseURLKey));

        storeButton = (Button) findViewById(R.id.storeButton);
        fetchButton = (Button) findViewById(R.id.fetchButton);
        textBox = (EditText)  findViewById(R.id.textBox);

        Drawable stockDrawable = getResources().getDrawable(R.drawable.stock_image);
        stockImage = ((BitmapDrawable) stockDrawable).getBitmap();



        storeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String text = textBox.getText().toString();

                if (text == null || text.equals("")) {
                    return;
                }


                EventData data = new EventData();
                data.setEventImage(stockImage);
                data.setEventText(text);

                database.child("eventData").child("eventText").setValue(data.getEventText());//.setValue(data);
                database.child("eventData").child("eventImage").setValue(data.getEventImage());
                Log.i(TAG, "Successfully wrote to firebase");
            }
        });

        fetchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                database.child("eventData").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        String eventText = (String) dataSnapshot.child("eventText").getValue();
                        String imageText = (String) dataSnapshot.child("eventImage").getValue();
                        Log.i(TAG, "Successfully read from firebase");

                        Log.i(TAG, "Read text: " + eventText + ", " + imageText);
                        EventData data = new EventData();
                        data.setEventText(eventText);
                        data.setEventImage(imageText);
                        listItems.add(data);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
            }
        });
    }

    private class MyAdapter extends BaseAdapter {

        private LayoutInflater inflater;
        private ArrayList<EventData> data;
        private Activity activity;

        public MyAdapter(Activity a, ArrayList<EventData> d) {
            activity = a;
            data = d;
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }
        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return (data.size() > position ? data.get(position) : null);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View vi = convertView;

            if (convertView == null) {
                Log.d(TAG, "Inflating view");
                vi = inflater.inflate(R.layout.single_row, null);
            } else {
                Log.d(TAG, "convertView not null? " + convertView.toString());
            }

            TextView text = (TextView) vi.findViewById(R.id.textView);
            ImageView image = (ImageView) vi.findViewById(R.id.imageView);

            EventData data = (EventData) getItem(position);

            text.setText(data.getEventText());
            image.setImageBitmap(data.getEventImageBitmap());

            return vi;
        }
    }
}