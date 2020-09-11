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
    static String deskKey;

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
        final String dateTime = simpleDateFormat.format(calendar.getTime());

        SimpleDateFormat sdfDay = new SimpleDateFormat("dd");
        final String day = sdfDay.format(calendar.getTime());

        final SimpleDateFormat sdfMonth = new SimpleDateFormat("M");
        final String month = sdfMonth.format(calendar.getTime());

        SimpleDateFormat sdfYear = new SimpleDateFormat("YYYY");
        final String year = sdfYear.format(calendar.getTime());

        Toast.makeText(Signin.this, "Checking booking.", Toast.LENGTH_SHORT).show();
        progressBar.setVisibility(View.VISIBLE);
        currDate.setText(dateTime);

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
                            Log.d("TAG", "Sucessful");
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
                                Log.d("myTag", currRange);

                                contDay = currRange.contains("day="+day);
                                contMonth = currRange.contains("month="+month);

                                verDay = Boolean.toString(contDay);
                                verMonth = Boolean.toString(contMonth);

                                if (verDay.equals("true") && verMonth.equals("true") ) {

                                    deskKey = document.getString("deskID");
                                    Toast.makeText(Signin.this, "You got a booking today!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(Signin.this, Scanner.class));

                                } else {
                                     // Toast.makeText(Signin.this, "No more bookings today!", Toast.LENGTH_SHORT).show();
                                    //startActivity(new Intent(Signin.this, MainActivity.class));
                                    finish();
                                }



                            }
                        });
                    }


                } else {
                    Log.d("TAG", "Error");


                }
            }
        });

    }
    public  static String getKeyID(){
        return deskKey;
    }
}