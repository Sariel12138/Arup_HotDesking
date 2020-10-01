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

    private EditText currDate;
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private static String deskKey, seat;

    private Calendar calendar = Calendar.getInstance();
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d/M/yyyy");
    private final String date = simpleDateFormat.format(calendar.getTime());

    private SimpleDateFormat sdfDay = new SimpleDateFormat("d");
    private final String day = sdfDay.format(calendar.getTime());

    private SimpleDateFormat sdfMonth = new SimpleDateFormat("M");
    private final String month = sdfMonth.format(calendar.getTime());

    private SimpleDateFormat sdfYear = new SimpleDateFormat("YYYY");
    private final String year = sdfYear.format(calendar.getTime());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        progressBar = findViewById(R.id.progressBar);
        currDate= findViewById(R.id.tv_date);
        currDate.setText(date);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        final String userID = firebaseAuth.getCurrentUser().getEmail();
        progressBar.setVisibility(View.VISIBLE);

        firebaseFirestore.collection("BookingRecords").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                String currUser;
                List<String> id = new ArrayList<>();
                List<String> email = new ArrayList<>();
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
                                String  verDay, verMonth, verYear, finMonth;
                                boolean contDay, contMonth, contYear;

                                DocumentSnapshot document = task.getResult();
                                ArrayList<String> collect = (ArrayList<String>) document.get("bookingRange");
                                String currRange[]= collect.toString().split(",");

                                    String currDay = currRange[35];
                                    String currMonth = currRange[10];
                                    String currYear = currRange[3];

                                    Log.d("TAG", "Day: "+currDay+" Month: "+currMonth+" Year: "+currYear);
                                    if (currMonth.contains("current")) {
                                        finMonth = currRange[11];
                                    } else{
                                        finMonth = currRange[10];
                                    }

                                    Log.d("TAG", "HERE 4");
                                    Log.d("TAG", "Current Day: " + currDay + " Current Month: " + finMonth + " Current Year: " + currYear);
                                    contDay = currDay.equals(" day=" + day);
                                    contMonth = finMonth.equals(" month=" + month);
                                    contYear = currYear.equals(" year=" + year);

                                    verDay = Boolean.toString(contDay);
                                    verMonth = Boolean.toString(contMonth);
                                    verYear = Boolean.toString(contYear);
                                    Log.d("TAG", "Date Check: " + verDay + " | " + verMonth + " | " + verYear);

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
                           // }
                        });
                        }

                } else {
                    Toast.makeText(Signin.this, "Error", Toast.LENGTH_SHORT).show();
                    Log.d("TAG", "Error: "+task.getException());
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
 /*   public void toScanner(){
        startActivity(new Intent(Signin.this, Scanner.class));
    }
    public void checkDate(String seatB, String desk, ArrayList collect){
        String  verDay, verMonth, verYear, finMonth;
        boolean contDay, contMonth, contYear;

        for(int z=0; z< collect.size(); z++){

            String[] splitDay = collect.get(z).toString().split(",");
            String currDay = splitDay[35];
            String currMonth= splitDay[10];
            String currYear= splitDay[3];

            if(currMonth.contains("current")){
                finMonth=splitDay[11];
            }else
                finMonth=splitDay[10];

            Log.d("TAG", "Current Day: "+ currDay+" Current Month: "+finMonth+" Current Year: "+currYear);
            contDay = currDay.equals(" day=" + day);
            contMonth = finMonth.equals(" month=" + month);
            contYear = currYear.equals(" year=" + year);

            verDay = Boolean.toString(contDay);
            verMonth = Boolean.toString(contMonth);
            verYear = Boolean.toString(contYear);
            Log.d("TAG", "Date Check: "+ verDay+" | "+verMonth+" | "+verYear);
            if (verDay.equals("true") && verMonth.equals("true") && verYear.equals("true")) {

                seat = seatB;
                deskKey = desk;
                finish();
                toScanner();

               // status=true;


                break;
            } else {
                finish();
                //status=false;
                startActivity(new Intent(Signin.this, MainActivity.class));
            }
        }
       // return status;
    }*/
}