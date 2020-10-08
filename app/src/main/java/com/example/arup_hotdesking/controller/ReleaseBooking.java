package com.example.arup_hotdesking.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.arup_hotdesking.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ReleaseBooking extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private Button release;
    private ListView listViewC;
    private ArrayList<String> id = new ArrayList<>();
    private ArrayList<String> email = new ArrayList<>();
    private ArrayList<String> desktitle = new ArrayList<>();
    private  Calendar cal= Calendar.getInstance();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    private String setTime= "09:30:00";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_booking);


        try {
            //String to Date
            Date start= dateFormat.parse(setTime);
            Log.d("TAG","Set Time: " + start);

            //Get Current Time in 'Date' format
            Date currTime = new Date();
            String textTime= dateFormat.format(currTime);
            Date finalTime= dateFormat.parse(textTime);

            Log.d("TAG","Current Time: " + finalTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        release = findViewById(R.id.release);
        listViewC = findViewById(R.id.listview_release);

        firebaseFirestore.collection("BookingRecords").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {

                for (QueryDocumentSnapshot document : task.getResult()) {

                    id.add(document.getId());
                    desktitle.add(document.getString("deskTitle"));
                    email.add(document.getString("email"));
                }

                    for (int i = 0; i < id.size(); i++) {
                        firebaseFirestore.collection("BookingRecords").document(id.get(i).toString()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                String currDeskTitle, currEmail;
                                DocumentSnapshot document = task.getResult();

                                currEmail = document.getString("email");
                                currDeskTitle = document.getString("deskTitle");


                            }
                        });


                    }
                } else {
                    Toast.makeText(ReleaseBooking.this, "Error: " + task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}