package com.example.arup_hotdesking.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.arup_hotdesking.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CheckinReports extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;
    private RecyclerView recyclerView;
    private  FirestoreRecyclerAdapter adapter;
    private Button export;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkin_reports);

        firebaseFirestore= FirebaseFirestore.getInstance();
        recyclerView= findViewById(R.id.checkinlist);
        export= findViewById(R.id.btn_export);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        final String date = simpleDateFormat.format(calendar.getTime());

        Query  query= firebaseFirestore.collection("CheckinRecords").orderBy("DateTime", Query.Direction.DESCENDING ).limit(200);

        FirestoreRecyclerOptions<CheckinRecords> options= new FirestoreRecyclerOptions.Builder<CheckinRecords>()
                .setQuery(query,CheckinRecords.class)
                .build();

         adapter= new FirestoreRecyclerAdapter<CheckinRecords, CheckinRecordsHolder>(options) {

             @NonNull
             @Override
             public CheckinRecordsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                 View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.checkin_item, parent, false );
                 return new CheckinRecordsHolder(view);
             }

             @Override
             protected void onBindViewHolder(@NonNull CheckinRecordsHolder holder, int position, @NonNull CheckinRecords model) {
                 holder.attempt.setText(model.getAttempt());
                 holder.datetime.setText(model.getDateTime());
                 holder.user.setText(model.getUser());
                 holder.seatname.setText(model.getSeatName());
             }
         };

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CheckinReports.this, Export.class));
            }
        });

    }


    private class CheckinRecordsHolder extends RecyclerView.ViewHolder{

        private TextView attempt;
        private TextView datetime;
        private TextView user;
        private TextView seatname;


        public CheckinRecordsHolder(@NonNull View itemView) {
            super(itemView);

            attempt= itemView.findViewById(R.id.list_attempt);
            datetime= itemView.findViewById(R.id.list_datetime);
            user= itemView.findViewById(R.id.list_user);
            seatname= itemView.findViewById(R.id.list_seatname);
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }


}