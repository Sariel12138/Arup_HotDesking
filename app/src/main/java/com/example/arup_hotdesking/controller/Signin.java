package com.example.arup_hotdesking.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.arup_hotdesking.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Signin extends AppCompatActivity {

    EditText currDate;
    ProgressBar progressBar;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    DocumentSnapshot result;
    static String deskKey, seat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);


        progressBar = findViewById(R.id.progressBar);
        currDate= findViewById(R.id.tv_date);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        final String userID = firebaseAuth.getCurrentUser().getEmail();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        final String date = simpleDateFormat.format(calendar.getTime());

        SimpleDateFormat sdfDay = new SimpleDateFormat("dd");
        final String day = sdfDay.format(calendar.getTime());

        SimpleDateFormat sdfMonth = new SimpleDateFormat("M");
        final String month = sdfMonth.format(calendar.getTime());

        SimpleDateFormat sdfYear = new SimpleDateFormat("YYYY");
        final String year = sdfYear.format(calendar.getTime());

        progressBar.setVisibility(View.VISIBLE);

        firebaseFirestore.collection("BookingRecords").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                String currUser;
                List<String> id = new ArrayList<>();
                List<String> email = new ArrayList<>();
                String[] records;
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        currUser = document.getString("email");

                        if (currUser.equals(userID)) {
                            email.add(document.getString("email"));
                            id.add(document.getId());
                        }
                    }

                    for (int i=0;i <id.size();i++) {
                        firebaseFirestore.collection("BookingRecords").document(id.get(i).toString()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                String currRange, verDay, verMonth, verYear;
                                boolean contDay, contMonth, contYear;

                                DocumentSnapshot document = task.getResult();
                                List<String> collect = (List<String>) document.get("bookingRange");

                                //current list index
                                currRange = collect.toString() + "\n";

                                contDay = currRange.contains("day=" + day);
                                contMonth = currRange.contains("month=" + month);
                                contYear = currRange.contains("year=" + year);

                                verDay = Boolean.toString(contDay);
                                verMonth = Boolean.toString(contMonth);
                                verYear = Boolean.toString(contYear);

                                if (verDay.equals("true") && verMonth.equals("true") && verYear.equals("true")) {

                                    seat = document.getString("deskTitle");
                                    deskKey = document.getString("deskID");
                                    Toast.makeText(Signin.this, "You got a booking today!", Toast.LENGTH_SHORT).show();
                                    finish();
                                    startActivity(new Intent(Signin.this, Scanner.class));

                                } else {

                                    finish();
                                    startActivity(new Intent(Signin.this, MainActivity.class));

                                }


                            }

                        });
                    }
                } else {
                    Toast.makeText(Signin.this, "Error", Toast.LENGTH_SHORT).show();
                    Log.d("TAG", "Error");
                }
            }
        });

    }
    public  static String getKeyID(){
        return deskKey;
    }
    public  static String getSeat(){
        return seat;
    }
}