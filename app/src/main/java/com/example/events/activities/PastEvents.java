package com.example.events.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.example.events.adapters.CardsRecyclerAdapter;
import com.example.events.beans.PastCard;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class PastEvents extends AppCompatActivity {

    private RecyclerView list;
    private ArrayList<PastCard> events;
    private ProgressBar progressBar;
    private FirebaseFirestore db;
    private CardsRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_events);

        list = findViewById(R.id.past_events_list);
        progressBar = findViewById(R.id.past_events_progressbar);
        db = FirebaseFirestore.getInstance();
        events = new ArrayList<>();
        adapter = new CardsRecyclerAdapter(this, events);
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);
        adapter.setOnItemClickListener(position -> {
            Intent intent = new Intent(this, EventDetails.class);
            intent.putExtra("eventType", "PastEvent");
            intent.putExtra("event", events.get(position));
            startActivity(intent);
        });
        db.collection("pastEvents").get().addOnSuccessListener(docs -> {
            for (DocumentSnapshot ds : docs.getDocuments()) {
                PastCard pastCard = ds.toObject(PastCard.class);
                events.add(pastCard);
            }
            adapter.notifyDataSetChanged();
            progressBar.setVisibility(View.GONE);
        });

    }
}
