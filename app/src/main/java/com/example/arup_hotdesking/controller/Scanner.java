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
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class Scanner extends AppCompatActivity {

    EditText resultData;
    CodeScanner codeScanner;
    CodeScannerView scannView;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        resultData = findViewById(R.id.tv_result);
        scannView = findViewById(R.id.scanner_view);
        codeScanner = new CodeScanner(this, scannView);
        progressBar=findViewById(R.id.progressBar2);
        final String res = Signin.getKeyID();
        Log.i("TAG", res);


        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        resultData.setText(result.getText());
                        String finRes = result.getText();
                        if (finRes.equals(res)) {
                            progressBar.setVisibility(View.VISIBLE);
                            Toast.makeText(Scanner.this, "Booking Confirmed: You are signed in.", Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(new Intent(Scanner.this, MainActivity.class));

                        } else {
                            Toast.makeText(Scanner.this, "Incorrect Desk!", Toast.LENGTH_SHORT).show();
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