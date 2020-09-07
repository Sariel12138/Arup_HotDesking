package com.example.arup_hotdesking.model;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.arup_hotdesking.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserViewModel extends ViewModel {
    private int workSpace = R.id.courseFragment;
    private MutableLiveData<Drawable> workSpaceIcon = new MutableLiveData<>();
    private FirebaseFirestore db;
    private List<BookingRecord> bookingRecords;
    private MutableLiveData<List<BookingRecord>> liveRecords = new MutableLiveData<>();
    private FirebaseUser user;
    private MutableLiveData<String> displayName = new MutableLiveData<>();
    private MutableLiveData<Boolean> isAdmin = new MutableLiveData<>();
    private MutableLiveData<Boolean> bookingResult = new MutableLiveData<>();

    public UserViewModel(){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        user = mAuth.getCurrentUser();
        getUserInfo();
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

    public FirebaseUser getUser() { return user; }

    private void getUserInfo() {
        if (user != null) {
            displayName.setValue(user.getDisplayName());
            DocumentReference employees = db.collection("employees").document(user.getEmail());
            employees.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        if (documentSnapshot != null && documentSnapshot.exists()) {
                            isAdmin.setValue(documentSnapshot.getBoolean("admin"));
                        }
                    }
                }
            });
        }
    }

    public MutableLiveData<String> getDisplayName() {
        return displayName;
    }

    public MutableLiveData<Boolean> getIsAdmin() {
        return isAdmin;
    }

    public MutableLiveData<Boolean> getBookingResult() {
        return bookingResult;
    }

    public void getDeskRecords(String deskNo){
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
                        Log.d("getDeskInfo", String.valueOf(bookingRecords.size()));
                        liveRecords.setValue(bookingRecords);
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
    }

    public MutableLiveData<List<BookingRecord>> getLiveBookingRecords(){
        return liveRecords;
    }

    public void resetLiveRecords() {
        liveRecords.setValue(new ArrayList<BookingRecord>());
    }

    public void bookSeat(String seatID, List<Calendar> calendarRange){
        Map<String,Object> bookingRecord = new HashMap<>();
        bookingRecord.put("desk_number",seatID);
        bookingRecord.put("email",user.getEmail());

        StringBuilder fromDateStringBuilder = new StringBuilder();
        Calendar fromDate = calendarRange.get(0);
        fromDateStringBuilder.append(fromDate.getDay()).append("/")
                .append(fromDate.getMonth()).append("/")
                .append(fromDate.getYear());
        bookingRecord.put("from_date",fromDateStringBuilder.toString());

        StringBuilder toDateStringBuilder = new StringBuilder();
        Calendar toDate = calendarRange.get(calendarRange.size()-1);
        toDateStringBuilder.append(toDate.getDay()).append("/")
                .append(toDate.getMonth()).append("/")
                .append(toDate.getYear());
        bookingRecord.put("to_date",toDateStringBuilder.toString());
        bookingRecord.put("year",String.valueOf(fromDate.getYear()));
        db.collection("BookingRecords")
                .add(bookingRecord)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        bookingResult.setValue(true);
                    }
                });

    }
}
