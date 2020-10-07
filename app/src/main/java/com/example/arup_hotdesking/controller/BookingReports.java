package com.example.arup_hotdesking.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.google.protobuf.StringValue;

import org.w3c.dom.Element;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class BookingReports extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private Button export;
    private ListView listViewB;
    private ArrayList<String> id = new ArrayList<>();
    private ArrayList<String> email = new ArrayList<>();
    private ArrayList<String> desktitle = new ArrayList<>();
    private ArrayList<String> bookingRange = new ArrayList<>();
    private ArrayList<String> newemail = new ArrayList<>();
    private ArrayList<String> newdesktitle = new ArrayList<>();
    private ArrayList<String> newbookingRange = new ArrayList<>();
    private ArrayList<BookinRecords> complete = new ArrayList<>();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_reports);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        export = findViewById(R.id.btn_booking_export);
        listViewB = findViewById(R.id.listview_booking);

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
                                final String floorSelected= BookingDateRange.getFloor();
                                String currDeskTitle, currEmail;

                                DocumentSnapshot document = task.getResult();
                                ArrayList<String> currBooking;

                                currBooking = (ArrayList<String>) document.get("bookingRange");
                                currEmail = document.getString("email");
                                currDeskTitle = document.getString("deskTitle");
                                if(currDeskTitle.startsWith(floorSelected)){
                                    add(currEmail, currDeskTitle, currBooking);
                                }

                            }
                        });

                    }

                } else {
                    Toast.makeText(BookingReports.this, "Error" + task.getException(), Toast.LENGTH_SHORT).show();

                }
                export.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String currEmail = firebaseAuth.getCurrentUser().getEmail().toString();
                        StringBuilder data = new StringBuilder();
                        data.append("User,Desk,Booking Details");

                        for (int i = 0; i < newemail.size(); i++) {
                            data.append("\n" + newemail.get(i).toString() + "," + newdesktitle.get(i).toString() + "," + newbookingRange.get(i).toString());
                        }

                        try {

                            FileOutputStream out = openFileOutput("BookingReport.csv", Context.MODE_PRIVATE);
                            out.write(data.toString().getBytes());
                            out.close();

                            Context context = getApplicationContext();
                            File fileLocation = new File(getFilesDir(), "BookingReport.csv");
                            Uri path = FileProvider.getUriForFile(context, "com.example.exportcsv.fileprovider", fileLocation);

                            Intent fileIntent = new Intent(Intent.ACTION_SEND);
                            fileIntent.setType("text/csv");

                            fileIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{currEmail});
                            fileIntent.putExtra(Intent.EXTRA_SUBJECT, "Booking Reservation Report");
                            fileIntent.putExtra(Intent.EXTRA_TEXT, "Hi, " + "\n\n" + "Attached is the booking reservation reports." + "\n\n" + "Regards," + "\n");
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
        });

    }
            public void add(String emailB, String deskname, ArrayList collect) {

                final String toCalendar = BookingDateRange.getBookingTo();
                final String fromCalendar = BookingDateRange.getBookingFrom();

                String[] CESplit;
                String day, month, year;


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

                        newemail.add(emailB);
                        newdesktitle.add(deskname);
                        newbookingRange.add(date);
                        BookinRecords record = new BookinRecords(emailB, deskname, date);
                        complete.add(record);

                        BookinListAdapter adapter = new BookinListAdapter(this, R.layout.bookin_item, complete);
                        listViewB.setAdapter(adapter);
                    }

                }
                if (complete.size() < 1) {
                    Toast.makeText(BookingReports.this, "No Data Retrieved for this filter." , Toast.LENGTH_SHORT).show();
                }
            }

        }
