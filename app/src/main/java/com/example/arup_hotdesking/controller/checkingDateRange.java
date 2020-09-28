package com.example.arup_hotdesking.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arup_hotdesking.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class checkingDateRange extends AppCompatActivity {

    private TextView tv_dateto, tv_datefrom;
    private DatePickerDialog.OnDateSetListener fromDateListener;
    private DatePickerDialog.OnDateSetListener toDateListener;
    private Button generate;
    private Calendar fromCalendar= Calendar.getInstance();
    private Calendar toCalendar= Calendar.getInstance();
    private static String actualFromDate;
    private static String actualToDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checking_date_range);

        tv_datefrom= findViewById(R.id.tv_datefromA);
        tv_dateto= findViewById(R.id.tv_datetoA);
        generate= findViewById(R.id.btn_generate);

        tv_datefrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar= Calendar.getInstance();

                int year= calendar.get(Calendar.YEAR);
                int month= calendar.get(Calendar.MONTH);
                int day= calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog= new DatePickerDialog(
                        checkingDateRange.this,
                        android.R.style.Theme_Material_Dialog_NoActionBar, fromDateListener, year, month, day);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        tv_dateto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar= Calendar.getInstance();
                int year= calendar.get(Calendar.YEAR);
                int month= calendar.get(Calendar.MONTH);
                int day= calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog= new DatePickerDialog(
                        checkingDateRange.this,
                        android.R.style.Theme_Material_Dialog_NoActionBar, toDateListener, year, month, day);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

            }
        });

        fromDateListener= new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker datePicker, int fromyear, int frommonth, int fromday) {
                frommonth= frommonth+1;

                actualFromDate= fromday+"/"+frommonth+"/"+fromyear;
                tv_datefrom.setText(actualFromDate);
                fromCalendar.set(fromyear, frommonth, fromday);

            }
        };

        toDateListener= new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int toyear, int tomonth, int today) {
                tomonth= tomonth+1;
                toCalendar.set(toyear, tomonth, today);
                if( toCalendar.before(fromCalendar)){
                    Log.d("TAG", "Invalid 'To' date range");
                    Toast.makeText(checkingDateRange.this, "Invalid 'To' date range", Toast.LENGTH_SHORT).show();

                } else{
                    actualToDate= today+"/"+tomonth+"/"+toyear;
                    tv_dateto.setText(actualToDate);

                }
            }
        };
    generate.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivity(new Intent(checkingDateRange.this, CheckinReports.class));
        }
    });
    }

    public static String getCheckinFrom(){
            return actualFromDate;
    }
    public static String getCheckinTo(){
        return actualToDate;
    }

}