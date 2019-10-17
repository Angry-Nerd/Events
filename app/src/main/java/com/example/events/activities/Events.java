package com.example.events.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.events.adapters.EventsRecyclerAdapter;
import com.example.events.beans.EventDetailsBean;
import com.example.events.beans.PastCard;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Events extends AppCompatActivity {

    private static final int REQUEST_CODE = 100;
    private DrawerLayout mDrawerLayout;
    private RecyclerView list;
    private ArrayList<EventDetailsBean> events = new ArrayList<>();
    private ProgressBar progressBar;
    private FirebaseFirestore db;
    private NavigationView navigationView;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        checkPermissions();
        init();

        recyclerLogic();

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            switch (id) {
                case R.id.logout:
                    if (checkNet()) {
                        final Snackbar snackbar = Snackbar.make(mDrawerLayout, "No Internet Connection", Snackbar.LENGTH_LONG);
                        snackbar.setAction("Dismiss",
                                view -> snackbar.dismiss()).show();
                        return false;
                    } else {
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                        break;
                    }
                case R.id.pastEvents: {
                    startActivity(new Intent(Events.this, PastEvents.class));
                    break;
                }
//                    case R.id.gallery:
//                        startActivity(new Intent(Events.this,GalleryList.class));

            }
            mDrawerLayout.closeDrawer(Gravity.START, true);
            return true;
        });
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void recyclerLogic() {
        EventsRecyclerAdapter recyclerAdapter = new EventsRecyclerAdapter(this, events);
        list.setAdapter(recyclerAdapter);
        recyclerAdapter.setOnItemClickListener(position -> {
            if (isNetworkAvailable()) {
                Intent intent = new Intent(Events.this, EventDetails.class);
                intent.putExtra("event", events.get(position));
                intent.putExtra("eventType", "ongoingEvent");
                startActivity(intent);
            } else {
                Toast.makeText(Events.this, "No Internet Connection", Toast.LENGTH_LONG).show();
            }

        });
        db.collection("events").get().addOnSuccessListener(docs -> {
            for (DocumentSnapshot ds : docs.getDocuments()) {
                try{
                    EventDetailsBean bean = ds.toObject(EventDetailsBean.class);
                    String eventDate = bean.getDateOfEvent();
                    Date date = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).parse(eventDate);
                    calendar.setTime(date);
                    LocalDate d1 = LocalDate.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH)+1, calendar.get(Calendar.DATE));
                    LocalDate d2 = LocalDate.now();
                    Period period = Period.between(d2, d1);
                    if(!period.isNegative())
                        events.add(bean);
                    else {
                        PastCard pc = getPastCard(bean);
                        db.collection("pastEvents").add(pc).addOnSuccessListener(documentReference -> db.collection("events").document(ds.getId()).delete());
                    }
                } catch(Exception e){
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
            recyclerAdapter.notifyDataSetChanged();
            progressBar.setVisibility(View.GONE);
        });

    }

    private PastCard getPastCard(EventDetailsBean bean) {
        return new PastCard(
                bean.getEventImageURL(),
                bean.getEventName(),
                bean.getEventDescription(),
                bean.getDateOfEvent(),
                bean.getEventOrganiser()
        );
    }

    private void init() {
        list = findViewById(R.id.recycler_events);
        progressBar = findViewById(R.id.events_progress_bar);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.nav_view);
        Button add = findViewById(R.id.add_event);
        db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(this));
        setSupportActionBar(toolbar);
        Intent prevIntent = getIntent();
        calendar = Calendar.getInstance();


        if (prevIntent.getBooleanExtra("canUpdate", false))
            add.setVisibility(View.VISIBLE);
        add.setOnClickListener(v -> {
            startActivity(new Intent(Events.this, AddDetails.class));
            finish();
        });
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
        }
        if (actionbar != null) {
            actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }

    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CODE);
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = null;
        if (connectivityManager != null) {
            activeNetworkInfo = connectivityManager
                    .getActiveNetworkInfo();
        }
        return activeNetworkInfo != null;
    }

    public boolean checkNet() {
        return !isNetworkAvailable();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE:
                if (!(permissions.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[1] == PackageManager.PERMISSION_GRANTED))
                    Toast.makeText(this, "Please Grant Permissions", Toast.LENGTH_LONG).show();
        }
    }
}
