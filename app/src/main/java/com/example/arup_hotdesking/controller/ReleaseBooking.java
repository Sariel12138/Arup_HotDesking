package com.example.arup_hotdesking.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.arup_hotdesking.R;
import com.example.arup_hotdesking.model.ReleaseBookingAdapter;
import com.example.arup_hotdesking.model.ReleaseBookingRecords;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ReleaseBooking extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;
    private Button release;
    private ListView listViewC;
    private TextView time;
    private ArrayList<String> id = new ArrayList<>();
    private ArrayList<String> attempt = new ArrayList<>();
    private ArrayList<String> dateArray = new ArrayList<>();
    private ArrayList<String> desktitle = new ArrayList<>();
    private ArrayList<String> email = new ArrayList<>();
    private ArrayList<String> desktitleBook = new ArrayList<>();
    private ArrayList<String> emailBook = new ArrayList<>();
    private ArrayList<ReleaseBookingRecords> complete = new ArrayList<>();
    private ArrayList<String> dateRelease = new ArrayList<>();
    private ArrayList<String> newemail = new ArrayList<>();
    private ArrayList<String> newdesktitle = new ArrayList<>();
    private ArrayList<String> newbookingRange = new ArrayList<>();
    //private SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    private SimpleDateFormat dateFormat = new SimpleDateFormat("d/M/yyyy");
    private SimpleDateFormat dateFormat3 = new SimpleDateFormat("HH:mm a");
    private String setTime= "09:30:50";
    //private String setTime2= "08:30:10";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_booking);

        firebaseFirestore = FirebaseFirestore.getInstance();
        release = findViewById(R.id.release);
        listViewC = findViewById(R.id.listview_release);
        time= findViewById(R.id.texview_count);

        Date dateA = Calendar.getInstance().getTime();
        String strDateB = dateFormat3.format(dateA);
        time.setText(strDateB);

        firebaseFirestore.collection("CheckinRecords").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                String currDate;
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {

                        currDate= document.getString("DateTime");

                        Date date = Calendar.getInstance().getTime();
                        String sysDate = dateFormat.format(date);

                        try {
                            Date date1=dateFormat.parse(currDate);
                            String recDate= dateFormat.format(date1);

                            if(sysDate.compareTo(recDate) == 0){

                                attempt.add(document.getString("Attempt"));
                                email.add(document.getString("User"));
                                desktitle.add(document.getString("deskTitle"));
                                dateArray.add(document.getString("DateTime"));

                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }
                }

            }

        });


        firebaseFirestore.collection("BookingRecords").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        id.add(document.getId());
                        desktitleBook.add(document.getString("deskTitle"));
                        emailBook.add(document.getString("email"));
                    }

                    for (int i = 0; i < id.size(); i++) {
                        firebaseFirestore.collection("BookingRecords").document(id.get(i).toString()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                String currDeskTitle, currEmail;
                                DocumentSnapshot document = task.getResult();
                                ArrayList<String> currBooking;

                                currBooking = (ArrayList<String>) document.get("bookingRange");
                                currEmail = document.getString("email");

                                currDeskTitle = document.getString("deskTitle");
                                add(currEmail, currDeskTitle, currBooking);
                                //firebaseFirestore.collection("BookingRecords").document().delete()
                            }
                        });

                    }
                }

            }

        });

        release.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("TAG", "New Email Count: "+ newemail.size());

            }
        });
    }


    public void add(String emailB, String deskname, ArrayList collect){

        String[] CESplit;
        String day, month, year;
        Date date1 = Calendar.getInstance().getTime();
        //format String date to complete date and time
        String strDate2 = dateFormat.format(date1);

        for (int z = 0; z < collect.size(); z++) {
                      String newMonth, finMonth, finDay;
            CESplit = collect.get(z).toString().split(",");

            year = CESplit[3].substring(6, 10);
            month = CESplit[10];
            day = CESplit[35];

            if (month.contains("tD")) {
                newMonth = CESplit[11];
            } else {
                newMonth = CESplit[10];
            }

            if (newMonth.length() == 8) {
                finMonth = newMonth.substring(7, 8);
            } else {
                finMonth = newMonth.substring(7, 9);
            }

            if (day.length() == 7) {
                finDay = day.substring(5, 7);
            } else finDay = day.substring(5, 6);

            String date = finDay + "/" + finMonth + "/" + year;
            Date dateObj = null;
            Date recordTime = null;
            try {
                //String to Date format
                dateObj = dateFormat.parse(date);
                recordTime = dateFormat.parse(strDate2);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar cal = Calendar.getInstance();
            Calendar currDate = Calendar.getInstance();

            cal.setTime(dateObj);
            currDate.setTime(recordTime);

            if (date.compareTo(strDate2) == 0) {
                for(int a=0; a < attempt.size(); a++){
                     if(!(emailB.equals(emailBook.get(a)) && deskname.equals(desktitleBook.get(a)))){
                        Log.d("TAG", "No Checkin");
                        Log.d("TAG", "Email: "+ emailB+" Desk: "+deskname+" Date: "+date);
                        newemail.add(emailB);
                        newdesktitle.add(deskname);
                        dateRelease.add(date);
                         ReleaseBookingRecords record= new ReleaseBookingRecords(emailB, deskname, "Not Signed In");
                           complete.add(record);

                         ReleaseBookingAdapter adapter = new ReleaseBookingAdapter(this, R.layout.activity_release_booking_item, complete);
                         listViewC.setAdapter(adapter);
                        break;
                    }
                }
                }

            }
        }

}