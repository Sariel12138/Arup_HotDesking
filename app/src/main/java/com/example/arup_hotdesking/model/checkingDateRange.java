package com.example.arup_hotdesking.model;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arup_hotdesking.R;
import com.example.arup_hotdesking.controller.CheckinReports;

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
    private SimpleDateFormat dateFormat = new SimpleDateFormat("d/M/yyyy");
    private Spinner spinnerCheckin;
    private String floors[]= {"All Floors", "Planta 1", "Planta 2", "Planta 3", "Planta 4", "Planta Baja"};
    private ArrayAdapter<String> arrayAdapterCheckin;
    private static String floorSelectedCheckin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checking_date_range);

        tv_datefrom= findViewById(R.id.tv_datefromA);
        tv_dateto= findViewById(R.id.tv_datetoA);
        generate= findViewById(R.id.btn_generate);
        spinnerCheckin= findViewById(R.id.spinner_checkin);

        arrayAdapterCheckin= new ArrayAdapter<String>(this, android.R.layout.simple_selectable_list_item, floors);
        spinnerCheckin.setAdapter(arrayAdapterCheckin);

        actualFromDate=null;
        actualToDate=null;

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
                if(actualFromDate==null){
                    Toast.makeText(checkingDateRange.this, "Please select from date", Toast.LENGTH_SHORT).show();
                } else{
                    actualToDate= today+"/"+tomonth+"/"+toyear;
                    tv_dateto.setText(actualToDate);
                }
            }
        };
    generate.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if(actualFromDate==null || actualToDate==null){//toCalendar.equals(fromCalendar)){
                Toast.makeText(checkingDateRange.this, "Date range cannot be empty", Toast.LENGTH_SHORT).show();
            } else if(toCalendar.before(fromCalendar)){
                Toast.makeText(checkingDateRange.this, "Invalid 'To' date.", Toast.LENGTH_SHORT).show();
                tv_dateto.setText("");
            }

            if((actualFromDate!=null || actualToDate!=null)) {
                if (toCalendar.after(fromCalendar)) {
                    startActivity(new Intent(checkingDateRange.this, CheckinReports.class));
                }
            }
        }
    });

    spinnerCheckin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            floorSelectedCheckin= floors[i];
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
            //Toast.makeText(BookingDateRange.this, "Please select a floor.", Toast.LENGTH_SHORT).show();
        }
    });
    }

    public static String getCheckinFrom(){
            return actualFromDate;
    }
    public static String getCheckinTo(){
        return actualToDate;
    }
    public static String getFloor(){

        if(floorSelectedCheckin.equals("All Floors")){
            floorSelectedCheckin="P";
        } else if(floorSelectedCheckin.equals("Planta 1")){
            floorSelectedCheckin= "P1_";
        } else if(floorSelectedCheckin.equals("Planta 2")){
            floorSelectedCheckin= "P2_";
        } else if(floorSelectedCheckin.equals("Planta 3")){
            floorSelectedCheckin= "P3_";
        } else if(floorSelectedCheckin.equals("Planta 4")){
            floorSelectedCheckin= "P4_";
        } else if(floorSelectedCheckin.equals("Planta Baja")){
            floorSelectedCheckin= "PB_";
        }

        return floorSelectedCheckin;
    }
}