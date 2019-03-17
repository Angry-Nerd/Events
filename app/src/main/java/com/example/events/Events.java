package com.example.events;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class Events extends AppCompatActivity {

    private static final int REQUEST_CODE = 100;
    private DrawerLayout mDrawerLayout;
    RecyclerView list;
    ArrayList<String> names=new ArrayList<>();
    ArrayList<String> images=new ArrayList<>();
    ArrayList<String> keys = new ArrayList<>();
    ArrayList<String> mail = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        checkPermissions();
        list= findViewById(R.id.recycler_semester_contents);
        list.setHasFixedSize(true);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        NavigationView navigationView = findViewById(R.id.nav_view);
        Button add = findViewById(R.id.add);
        String username = " ";
        Toast.makeText(this,"Retrieving..",Toast.LENGTH_LONG).show();
        if(getIntent().getBooleanExtra("canUpdate",false))
            add.setVisibility(View.VISIBLE);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Events.this,AddDetails.class));
                finish();
            }
        });
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
        }
        if (actionbar != null) {
            actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("events");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                names = new ArrayList<>();
                images = new ArrayList<>();
                keys = new ArrayList<>();
                for(DataSnapshot ds:dataSnapshot.getChildren()) {
                    for(DataSnapshot dataSnapshot1:ds.getChildren()) {
                        EventDetailsBean ev = dataSnapshot1.getValue(EventDetailsBean.class);
                        names.add(ev.getEvent());
                        images.add(ev.getUrl());
                        keys.add(dataSnapshot1.getKey());
                        mail.add(ds.getKey());
                    }
                }
                RecyclerAdapter recyclerAdapter=new RecyclerAdapter(Events.this,names,images){
                    @Override
                    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
                        super.onBindViewHolder(holder, position);
                        final String pos = keys.get(position);
                        final String email = mail.get(position);
                        Toast.makeText(Events.this,pos,Toast.LENGTH_LONG).show();
                        holder.layout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (isNetworkAvailable()) {
                                    Intent i =new Intent(Events.this,EventDetails.class);
                                    i.putExtra("Event",pos);
                                    i.putExtra("email",email);
                                    startActivity(i);
                                } else {
                                    final Snackbar snackbar = Snackbar.make(v, "No Internet Connection", Snackbar.LENGTH_LONG);
                                    snackbar.setAction("Dismiss",
                                            new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    snackbar.dismiss();
                                                }
                                            }).show();
                                }
                            }
                        });
                    }
                };

                list.setLayoutManager(new LinearLayoutManager(Events.this));
                list.setAdapter(recyclerAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id=item.getItemId();
                switch (id)
                {
                    case R.id.logout: {
                        if (checkNet()) {
                            final Snackbar snackbar = Snackbar.make(mDrawerLayout, "No Internet Connection", Snackbar.LENGTH_LONG);
                            snackbar.setAction("Dismiss",
                                    new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            snackbar.dismiss();
                                        }
                                    }).show();
                            return false;
                        } else {
                            FirebaseAuth.getInstance().signOut();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                            break;
                        }
                    }
//                    case R.id.seeAnnouncement:{
//                        startActivity(new Intent(Events.this,SeeAnnouncements.class));
//                        break;
//                    }
//                    case R.id.gallery:
//                        startActivity(new Intent(Events.this,GalleryList.class));

                }
                mDrawerLayout.closeDrawer(Gravity.START,true);
                return true;
            }

        });
    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},
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
    public boolean checkNet() { return !isNetworkAvailable(); }
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode){
            case REQUEST_CODE:
                if(!(permissions.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED &&
                        grantResults[1]==PackageManager.PERMISSION_GRANTED))
                    Toast.makeText(this,"Please Grant Permissions",Toast.LENGTH_LONG).show();
        }
    }
}
