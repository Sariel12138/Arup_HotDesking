package com.example.arup_hotdesking.model;

import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class UserViewModel extends ViewModel {
    private int workSpace = 0;
    private MutableLiveData<Drawable> workSpaceIcon = new MutableLiveData<>();
    private FirebaseFirestore db;
    private List<BookingRecord> bookingRecords;

    public UserViewModel(){
        db = FirebaseFirestore.getInstance();
    }

    public MutableLiveData<Drawable> getWorkSpaceIcon() {
        return workSpaceIcon;
    }

    public void setWorkSpaceIcon(Drawable icon) {
        this.workSpaceIcon.setValue(icon);
    }

    public int getWorkSpace() {
        return workSpace;
    }

    public void setWorkSpace(int workSpace) {
        this.workSpace = workSpace;
    }

    public List<BookingRecord> getDeskRecords(String deskNo){
        //If it is necessary to ensure the consistency of the data when writing to the database, register with collectionListener

        CollectionReference records = db.collection("BookingRecords");
        bookingRecords = new ArrayList<>();
        records
                .whereEqualTo("desk_number",deskNo)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    QuerySnapshot querySnapshot = task.getResult();
                    if(querySnapshot != null){
                        int id = 0;
                        for(QueryDocumentSnapshot snapshot:task.getResult()){
                            BookingRecord bookingRecord = new BookingRecord(++id,snapshot.getString("from_date"),
                                    snapshot.getString("to_date"),snapshot.getString("email"));
                            bookingRecords.add(bookingRecord);
                            Log.d("getDeskInfo", snapshot.getString("email"));
                        }
                    }
                    else {
                        Log.d("getDeskInfo",task.getException().toString());
                    }
                }
                else {
                    Log.d("getDeskInfo",task.getException().toString());
                }
            }
        });
        Log.d("getDeskInfo","recordssize:"+bookingRecords.size());
        return bookingRecords;
    }
}
