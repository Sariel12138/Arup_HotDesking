package com.example.arup_hotdesking.model;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.arup_hotdesking.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.MetadataChanges;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.haibin.calendarview.Calendar;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserViewModel extends ViewModel {
    private int workSpace = R.id.courseFragment;
    private MutableLiveData<Drawable> workSpaceIcon = new MutableLiveData<>();
    private FirebaseFirestore db;
    private CollectionReference BookingRecordsCollection;
    private List<BookingRecord> deskBookingRecords;  //desk booking records
    private List<BookingRecord> userBookingRecords; //user booking records
    private MutableLiveData<List<BookingRecord>> liveRecords = new MutableLiveData<>();
    private MutableLiveData<List<BookingRecord>> userLiveRecords = new MutableLiveData<>();
    private ListenerRegistration deskRecordsRegistration;
    private FirebaseUser user;
    private MutableLiveData<String> displayName = new MutableLiveData<>();
    private MutableLiveData<Boolean> isAdmin = new MutableLiveData<>();
    private MutableLiveData<Boolean> bookingResult = new MutableLiveData<>();
    
    public UserViewModel(){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        BookingRecordsCollection = db.collection("BookingRecords");
        user = mAuth.getCurrentUser();
        getUserInfo();
    }

    public List<BookingRecord> getUserBookingRecords() {
        return userBookingRecords;
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

    public void resetBookingResult() {
        bookingResult.setValue(null);
    }

    public void getDeskRecords(String deskNo){
        //If it is necessary to ensure the consistency of the data when writing to the database, register with collectionListener
        deskBookingRecords = new ArrayList<>();
        Query deskRecords = BookingRecordsCollection.whereEqualTo("deskID",deskNo);
        deskRecords
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    QuerySnapshot querySnapshot = task.getResult();
                    if(querySnapshot != null){
                        for(QueryDocumentSnapshot snapshot:querySnapshot){
//                            BookingRecord bookingRecord = new BookingRecord(++id,snapshot.getString("from_date"),
//                                    snapshot.getString("to_date"),snapshot.getString("email"));
                            BookingRecord bookingRecord = snapshot.toObject(BookingRecord.class);
                            bookingRecord.setDocumentID(snapshot.getId());
                            deskBookingRecords.add(bookingRecord);
                            Log.d("getDeskInfo", "email: "+snapshot.getString("email"));
                        }
                        Log.d("getDeskInfo", "number of records: "+ String.valueOf(deskBookingRecords.size()));
                        liveRecords.setValue(deskBookingRecords);
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

        deskRecordsRegistration = deskRecords
                .addSnapshotListener(MetadataChanges.INCLUDE, new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.w("deskSnapshotListener", "Listen failed.", error);
                            return;
                        }

                        for(QueryDocumentSnapshot snapshot:value){
                            BookingRecord bookingRecord = snapshot.toObject(BookingRecord.class);
                            bookingRecord.setDocumentID(snapshot.getId());
                            deskBookingRecords.add(bookingRecord);
                        }
                        liveRecords.setValue(deskBookingRecords);
                    }
                });

    }

    public void removeBookingRecordsListener(){
        deskRecordsRegistration.remove();
    }


    public void getUserRecords(String email){
        userBookingRecords = new ArrayList<>();
        Query userRecords = BookingRecordsCollection.whereEqualTo("email",email);
        userRecords
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    QuerySnapshot querySnapshot = task.getResult();
                    if(querySnapshot != null){
                        for(QueryDocumentSnapshot snapshot:querySnapshot){
//                            BookingRecord bookingRecord = new BookingRecord(++id,snapshot.getString("from_date"),
//                                    snapshot.getString("to_date"),snapshot.getString("email"));
                            BookingRecord bookingRecord = snapshot.toObject(BookingRecord.class);
                            bookingRecord.setDocumentID(snapshot.getId());
                            userBookingRecords.add(bookingRecord);
                            Log.d("getUserInfo", "email: "+snapshot.getString("email"));
                        }
                        Log.d("getUserInfo", "number of records: "+ String.valueOf(userBookingRecords.size()));
                       userLiveRecords.setValue(userBookingRecords);
                    }
                    else {
                        Log.d("getUserInfo",task.getException().toString());
                    }
                }
                else {
                    Log.d("getUserInfo",task.getException().toString());
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

    public MutableLiveData<List<BookingRecord>> getUserLiveBookingRecords(){
        return userLiveRecords;
    }

    public void resetUserLiveRecords() {
        userLiveRecords.setValue(new ArrayList<BookingRecord>());
    }


    public void bookSeat(String deskID, List<Calendar> calendarRange,String deskTitle){
        BookingRecord bookingRecord = new BookingRecord(deskID,calendarRange,user.getEmail(),deskTitle);

        BookingRecordsCollection
                .add(bookingRecord)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        bookingResult.setValue(true);

                    }
                });
    }

    private void deleteBookingRecord(BookingRecord bookingRecord){
        BookingRecordsCollection
                .document(bookingRecord.documentID())
                .delete();
//        userBookingRecords.remove(bookingRecord);
//        userLiveRecords.setValue(userBookingRecords);
    }

    private void updateBookingDates(BookingRecord bookingRecord,List<Calendar> bookDates){
        BookingRecordsCollection
                .document(bookingRecord.documentID())
                .update("bookingRange",bookDates);
    }

    public void deleteBooking(BookingRecord bookingRecord,Calendar bookDate){
       // if(userLiveRecords!=null) return;
        List<BookingRecord> bookingRecords = userLiveRecords.getValue();
        for(int i=0;i<bookingRecords.size();i++){
            if(bookingRecords.get(i) == bookingRecord){
                List<Calendar> bookDates = bookingRecords.get(i).getBookingRange();
                for(int j=0;j<bookDates.size();j++){
                    if(bookDates.get(j) == bookDate) {
                        bookDates.remove(bookDate);
                        if(bookDates.size()==0){
                            bookingRecords.remove(bookingRecord);
                            deleteBookingRecord(bookingRecord);
                        }
                        else{
                            updateBookingDates(bookingRecord,bookDates);
                        }
                    }
                }
            }
        }
        userBookingRecords = bookingRecords;
        userLiveRecords.setValue(userBookingRecords);

    }

}
