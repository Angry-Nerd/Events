package com.example.events.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.events.beans.EventDetailsBean;
import com.example.events.beans.PastCard;

public class EventDetails extends AppCompatActivity {

    TextView eventName, eventOrganizer, eventVenue, eventDescription, eventRegisterationURL, eventDate;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        String typeOfEvent = intent.getStringExtra("typeOfEvent");
        if (typeOfEvent.equals("ongoingEvent")) {
            EventDetailsBean eventObject = intent.getParcelableExtra("event");
            Toast.makeText(this, "Retrieving...", Toast.LENGTH_LONG).show();
            if (getSupportActionBar() != null)
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            eventName = findViewById(R.id.name);
            eventOrganizer = findViewById(R.id.organizer);
            eventDescription = findViewById(R.id.desc);
            eventVenue = findViewById(R.id.venue);
            eventRegisterationURL = findViewById(R.id.link);
            imageView = findViewById(R.id.eventPoster);
            eventDate = findViewById(R.id.showEventDate);


            eventName.setText(eventObject.getEventName());
            eventOrganizer.setText(eventObject.getEventOrganiser());
            eventDescription.setText(eventObject.getEventDescription());
            eventVenue.setText(eventObject.getEventVenue());
            eventRegisterationURL.setText(eventObject.getEventRegisterationLink());
            eventDate.setText(eventObject.getDateOfEvent());
            Glide.with(this).asBitmap().load(eventObject.getEventImageURL()).into(imageView);


        } else if (typeOfEvent.equals("pastEvent")) {
            PastCard eventObject = intent.getParcelableExtra("event");

            Toast.makeText(this, "Retrieving...", Toast.LENGTH_LONG).show();
            if (getSupportActionBar() != null)
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            eventName = findViewById(R.id.name);
            eventOrganizer = findViewById(R.id.organizer);
            eventDescription = findViewById(R.id.desc);
            eventVenue = findViewById(R.id.venue);
            eventRegisterationURL = findViewById(R.id.link);
            imageView = findViewById(R.id.eventPoster);
            eventDate = findViewById(R.id.showEventDate);


            eventName.setText(eventObject.getCardTitle());
            eventOrganizer.setText(eventObject.getCardOrganizer());
            eventDescription.setText(eventObject.getCardDescription());
            eventVenue.setVisibility(View.GONE);
            eventRegisterationURL.setVisibility(View.GONE);
            eventDate.setText(eventObject.getDateOfEvent());
            Glide.with(this).asBitmap().load(eventObject.getCardImageURL()).into(imageView);

        }

//        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                EventDetailsBean ev = dataSnapshot.getValue(EventDetailsBean.class);
//                if (ev != null) {
//                    Glide.with(EventDetails.this).asBitmap().load(ev.getUrl()).into(imageView);
//                    event.setText(ev.getEvent());
//                    String organizerString = "Organizer: "+ev.getOrg();
//                    org.setText(organizerString);
//                    desc.setText(ev.getDesc());
//                    String venueString = "Venue: "+ev.getVenue();
//                    venue.setText(venueString);
//                    eventRegisterationURL.setText(ev.getRegLink());
//                }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
        eventRegisterationURL.setOnClickListener(v -> {
            Intent intent1 = new Intent(EventDetails.this, RegisterWebView.class);
            intent1.putExtra("link", eventRegisterationURL.getText().toString());
            startActivity(intent1);
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}

