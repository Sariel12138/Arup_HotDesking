package com.example.arup_hotdesking.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.arup_hotdesking.R;
import com.example.arup_hotdesking.model.CheckinListAdapter;
import com.example.arup_hotdesking.model.CheckinRecords;
import com.example.arup_hotdesking.model.checkingDateRange;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class CheckinReports extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private Button export;
    private ListView listView;
    private ArrayList<String> attempt = new ArrayList<>();
    private ArrayList<String> datetime = new ArrayList<>();
    private ArrayList<String> seatname = new ArrayList<>();
    private ArrayList<String> user = new ArrayList<>();
    private ArrayList<String> newattempt = new ArrayList<>();
    private ArrayList<String> newdatetime = new ArrayList<>();
    private ArrayList<String> newseatname = new ArrayList<>();
    private ArrayList<String> newuser = new ArrayList<>();
    private ArrayList<CheckinRecords> complete = new ArrayList<>();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("d/M/yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkin_reports);

        firebaseAuth= FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        export = findViewById(R.id.btn_export);
        listView = (ListView) findViewById(R.id.listView_checkins);

        final String toCalendar= checkingDateRange.getCheckinTo();
        final String fromCalendar= checkingDateRange.getCheckinFrom();

    firebaseFirestore.collection("CheckinRecords").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
        @Override
        public void onComplete(@NonNull Task<QuerySnapshot> task) {

            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {

                    final String floorSelected = checkingDateRange.getFloor();
                    String currDesk = document.getString("SeatName");

                    if (currDesk.startsWith(floorSelected)) {
                        attempt.add(document.getString("Attempt"));
                        datetime.add(document.getString("DateTime"));
                        seatname.add(document.getString("SeatName"));
                        user.add(document.getString("User"));
                    }

                }

                    for (int i = 0; i < datetime.size(); i++) {

                        String date = datetime.get(i);

                        Date dateObj = null;
                        Date fromObj = null;
                        Date toObj = null;
                        try {
                            dateObj = dateFormat.parse(date);
                            fromObj = dateFormat.parse(fromCalendar);
                            toObj = dateFormat.parse(toCalendar);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        Calendar cal = Calendar.getInstance();
                        Calendar fromCal = Calendar.getInstance();
                        Calendar toCal = Calendar.getInstance();
                        cal.setTime(dateObj);
                        fromCal.setTime(fromObj);
                        toCal.setTime(toObj);

                        fromCal.add(Calendar.DAY_OF_MONTH, -1);
                        toCal.add(Calendar.DAY_OF_MONTH, 1);

                        if (cal.before(toCal) && cal.after(fromCal)) {
                            add(attempt.get(i).toString(), datetime.get(i).toString(), seatname.get(i).toString(), user.get(i).toString());
                            newattempt.add(attempt.get(i));
                            newdatetime.add(datetime.get(i));
                            newseatname.add(seatname.get(i));
                            newuser.add(user.get(i));
                        }

                    }

            } else {
                Toast.makeText(CheckinReports.this, "Error: " + task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                Log.d("TAG", "Error: " + task.getException());
            }

        }

    });
        export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = firebaseAuth.getCurrentUser().getEmail().toString();
                StringBuilder data = new StringBuilder();
                data.append("User,Attempt,DateTime,Booked Seat");

                for (int i = 0; i < newattempt.size(); i++) {
                    data.append("\n" + newuser.get(i).toString() + "," + newattempt.get(i).toString() + "," + newdatetime.get(i).toString() + "," + newseatname.get(i).toString());
                }

                try {
                    FileOutputStream out = openFileOutput("CheckinReport.csv", Context.MODE_PRIVATE);
                    out.write(data.toString().getBytes());
                    out.close();

                    Context context = getApplicationContext();
                    File fileLocation = new File(getFilesDir(), "CheckinReport.csv");
                    Uri path = FileProvider.getUriForFile(context, "com.example.exportcsv.fileprovider", fileLocation);

                    Intent fileIntent = new Intent(Intent.ACTION_SEND);
                    fileIntent.setType("text/csv");

                    fileIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
                    fileIntent.putExtra(Intent.EXTRA_SUBJECT, "Checkin Attempt Report");
                    fileIntent.putExtra(Intent.EXTRA_TEXT, "Hi, " + "\n\n" + "Attached is the check in attempts reports." + "\n\n" + "Regards," + "\n");
                    fileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    fileIntent.putExtra(Intent.EXTRA_STREAM, path);
                    startActivity(Intent.createChooser(fileIntent, "Send mail"));

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            });

    }

        public void add (String attempt, String datetime, String seat, String user) {

            CheckinRecords record = new CheckinRecords(attempt, datetime, seat, user);
                complete.add(record);
            CheckinListAdapter adapter = new CheckinListAdapter(this, R.layout.checkin_item, complete);
              listView.setAdapter(adapter);

        }
    }
