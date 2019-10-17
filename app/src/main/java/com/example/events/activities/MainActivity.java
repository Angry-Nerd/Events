package com.example.events.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    Button login,guest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login = findViewById(R.id.login);
        guest = findViewById(R.id.guest);
        String username="";

        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            Intent intent = new Intent(MainActivity.this,Events.class);
            intent.putExtra("canUpdate",true);
            startActivity(intent);
            finish();
        }
        login.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this,Login.class);
            intent.putExtra("canUpdate",true);
            startActivity(intent);
            finish();
        });
        guest.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this,Events.class);
            intent.putExtra("canUpdate",false);
            startActivity(intent);
            finish();
        });

    }
}
