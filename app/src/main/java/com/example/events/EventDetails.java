package com.example.events;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EventDetails extends AppCompatActivity {

    DatabaseReference databaseReference;
    TextView event,org,venue,desc,link;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent i = getIntent();

        Toast.makeText(this,"Retrieving...",Toast.LENGTH_LONG).show();
        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        databaseReference = FirebaseDatabase.getInstance().getReference("events/"+i.getStringExtra("email")+"/"+i.getStringExtra("Event"));
        event = findViewById(R.id.name);
        org = findViewById(R.id.organizer);
        desc = findViewById(R.id.desc);
        venue = findViewById(R.id.venue);
        link = findViewById(R.id.link);
        imageView = findViewById(R.id.eventPoster);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                EventDetailsBean ev = dataSnapshot.getValue(EventDetailsBean.class);
                if (ev != null) {
                    Glide.with(EventDetails.this).asBitmap().load(ev.getUrl()).into(imageView);
                    event.setText(ev.getEvent());
                    String organizerString = "Organizer: "+ev.getOrg();
                    org.setText(organizerString);
                    desc.setText(ev.getDesc());
                    String venueString = "Venue: "+ev.getVenue();
                    venue.setText(venueString);
                    link.setText(ev.getRegLink());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventDetails.this,Register.class);
                intent.putExtra("link",link.getText().toString());
                startActivity(intent);
            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}

