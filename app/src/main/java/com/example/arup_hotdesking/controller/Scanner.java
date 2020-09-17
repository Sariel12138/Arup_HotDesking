package com.example.arup_hotdesking.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.example.arup_hotdesking.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Scanner extends AppCompatActivity {

    EditText resultData;
    CodeScanner codeScanner;
    CodeScannerView scannView;
    ProgressBar progressBar;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore db;

    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("hh:mm:ss a");
    final String date = simpleDateFormat.format(calendar.getTime());
    final String time = simpleTimeFormat.format(calendar.getTime());
    final String dateTime= ""+date+" "+time;
    final String seat =  Signin.getSeat();
    final Map<String, Object> details= new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        firebaseAuth = FirebaseAuth.getInstance();
        db= FirebaseFirestore.getInstance();
        resultData = findViewById(R.id.tv_result);
        scannView = findViewById(R.id.scanner_view);
        codeScanner = new CodeScanner(this, scannView);
        progressBar=findViewById(R.id.progressBar2);
        final String res = Signin.getKeyID();



        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        resultData.setText(result.getText());
                        String finRes = result.getText();
                        String attempt;

                        if (finRes.equals(res)) {
                            attempt= "Success";
                            progressBar.setVisibility(View.VISIBLE);
                            Toast.makeText(Scanner.this, "Booking Confirmed: You are signed in.", Toast.LENGTH_SHORT).show();
                            addDB(attempt);
                            finish();
                            startActivity(new Intent(Scanner.this, MainActivity.class));

                        } else {
                            attempt="Denied";
                            addDB(attempt);
                            Toast.makeText(Scanner.this, "Incorrect Desk! Please try again.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Scanner.this, MainActivity.class));
                            finish();
                        }

                    }
                });

            }

        });

        scannView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                codeScanner.startPreview();
            }
        });
    }

    public void addDB(String attempt){
        String id= UUID.randomUUID().toString();

        details.put("DateTime",  dateTime);
        details.put("Attempt", attempt);
        details.put("User",  firebaseAuth.getCurrentUser().getEmail());
        details.put("SeatName", seat);

        Log.d("TAG", "Seat: "+seat);
        Log.d("TAG", "User: "+ firebaseAuth.getCurrentUser().getEmail() );
        Log.d("TAG", "Attempt: "+attempt);
        Log.d("TAG", "Date/Time: "+ dateTime);


        db.collection("CheckinRecords").document(id).set(details).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(Scanner.this, "Added!", Toast.LENGTH_SHORT).show();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Scanner.this, "Error: "+e, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestForCamera();
    }

    private void requestForCamera() {
        Dexter.withActivity(this).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                codeScanner.startPreview();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {
                Toast.makeText(Scanner.this, "Camera permission required.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }).check();
    }
}