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

public class UserViewModel extends ViewModel {
    private int workSpace = 0;
    private MutableLiveData<Drawable> workSpaceIcon = new MutableLiveData<>();
    private FirebaseFirestore db;

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

    public void getDeskRecords(String deskNo){
        CollectionReference employees = db.collection("BookingRecords");
        employees
                .whereEqualTo("desk_number",deskNo)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    QuerySnapshot querySnapshot = task.getResult();
                    if(querySnapshot != null){
                        for(QueryDocumentSnapshot snapshot:task.getResult()){
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
    }
}
