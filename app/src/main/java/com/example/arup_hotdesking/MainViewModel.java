package com.example.arup_hotdesking;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;

public class MainViewModel extends ViewModel {
    private MutableLiveData<FirebaseUser> userMutableLiveData = new MutableLiveData<>();

    LiveData<FirebaseUser> getCurrentUser(){return  userMutableLiveData;}


}
