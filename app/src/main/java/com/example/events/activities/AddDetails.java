package com.example.events.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.events.beans.EventDetailsBean;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddDetails extends AppCompatActivity {

    TextView eventName, eventOrganiser, eventVenue, eventDescription, eventRegisterationURL, eventDate;
    Button submit;
    CircleImageView imageView;
    FirebaseFirestore db;
    public final int FILE_SELECT_CODE = 1;
    StorageReference storageReference;
    Uri uri = null;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_details);
        initialize();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void initialize() {
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String arr[] = email.split("@");
        String username = arr[0];
        eventName = findViewById(R.id.eventName);
        eventOrganiser = findViewById(R.id.eventOrg);
        eventVenue = findViewById(R.id.eventVenue);
        eventDescription = findViewById(R.id.eventDesc);
        submit = findViewById(R.id.submit);
        eventRegisterationURL = findViewById(R.id.eventUrl);
        eventDate = findViewById(R.id.eventDate);
        imageView = findViewById(R.id.eventImage);
        storageReference = FirebaseStorage.getInstance().getReference("events/images/");
        db = FirebaseFirestore.getInstance();


        imageView.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            try {
                startActivityForResult(Intent.createChooser(intent, "Choose File"), FILE_SELECT_CODE);
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(AddDetails.this, "Please install a File Manager.", Toast.LENGTH_SHORT).show();
            }
        });


        submit.setOnClickListener(v -> {
            if (uri != null)
                uploadImage(uri, username);
            else
                Toast.makeText(AddDetails.this, "Select an image", Toast.LENGTH_LONG).show();
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void uploadImage(Uri uri, String username) {
        if (uri != null && !(TextUtils.isEmpty(eventName.getText().toString()) || TextUtils.isEmpty(eventVenue.getText().toString()) ||
                TextUtils.isEmpty(eventDescription.getText().toString()) || TextUtils.isEmpty(eventOrganiser.getText().toString()))) {

            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.setMessage("Uploading...");
            dialog.show();

            storageReference = storageReference.child(Objects.requireNonNull(uri.getLastPathSegment()));
            storageReference.putFile(uri).addOnSuccessListener(taskSnapshot -> {
                storageReference.getDownloadUrl().addOnCompleteListener(task -> {
                    {
                        EventDetailsBean ev = new EventDetailsBean(eventName.getText().toString(),
                                eventOrganiser.getText().toString(), eventVenue.getText().toString()
                                , eventDescription.getText().toString(), task.getResult().toString()
                                , eventRegisterationURL.getText().toString(), username, eventDate.getText().toString());
                        db.collection("events")
                                .add(ev).addOnSuccessListener(documentReference -> {
                            dialog.dismiss();
                            Toast.makeText(AddDetails.this, "Uploaded", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(this, Events.class));
                            finish();
                        });
                    }
                });
            }).addOnFailureListener(e -> {
                Toast.makeText(AddDetails.this, e.getMessage(), Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }).addOnProgressListener(taskSnapshot -> {
                double progress = (100 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                dialog.setMessage("Uploaded " + (int) progress + "%");
            });
        } else {
            Toast.makeText(this, "Fill all details", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (data != null) {
                    if (!data.toString().contains("image")) {
                        Toast.makeText(this, "Only Image is allowed", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                if (resultCode == RESULT_OK && data != null && data.getData() != null) {
                    try {
                        uri = data.getData();
                        InputStream inputStream;
                        inputStream = getContentResolver().openInputStream(uri);
                        final Bitmap imageMap = BitmapFactory.decodeStream(inputStream);
                        imageView.setImageBitmap(imageMap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else Toast.makeText(this, "Error Importing Image", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, Events.class));
    }
}
