package com.example.arup_hotdesking.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.arup_hotdesking.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.core.View;
import com.google.protobuf.StringValue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Export extends AppCompatActivity {

  FirebaseFirestore firebaseFirestore;
    final List<String> user = new ArrayList<>();
    final List<String> attempt = new ArrayList<>();
    final List<String> seatname = new ArrayList<>();
    final List<String> datetime = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);


        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("CheckinRecords").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        user.add(document.getString("User"));
                        attempt.add(document.getString("Attempt"));
                        seatname.add(document.getString("SeatName"));
                        datetime.add(document.getString("DateTime"));

                    }
                    export();
                    Log.d("TAG", user.toString());
                    Log.d("TAG", attempt.toString());
                    Log.d("TAG", seatname.toString());
                    Log.d("TAG", datetime.toString());

                } else {
                    Log.d("TAG", "Error"+task.getException());
                }
            }
        });

    }
        public void export() {

            StringBuilder data = new StringBuilder();
            data.append("User,Attempt,DateTime,Booked Seat");

            for (int i = 0; i < user.size(); i++) {
                data.append("\n" + user.get(i).toString() + "," + attempt.get(i).toString() + "," + datetime.get(i).toString() + "," + seatname.get(i).toString());
            }

            try {
                FileOutputStream out = openFileOutput("CheckinAttempts.csv", Context.MODE_PRIVATE);
                out.write(data.toString().getBytes());
                out.close();

                Context context = getApplicationContext();
                File fileLocation = new File(getFilesDir(), "CheckinAttempts.csv");
                Uri path = FileProvider.getUriForFile(context, "com.example.exportcsv.fileprovider", fileLocation);
                Intent fileIntent = new Intent(Intent.ACTION_SEND);
                fileIntent.setType("text/csv");
                fileIntent.putExtra(Intent.EXTRA_SUBJECT, "Checkin Attempt Report");
                fileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                fileIntent.putExtra(Intent.EXTRA_STREAM, path);
                startActivity(Intent.createChooser(fileIntent, "Send mail"));


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
