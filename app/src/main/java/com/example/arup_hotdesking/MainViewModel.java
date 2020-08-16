package com.example.arup_hotdesking;

import android.content.Intent;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;

public class MainViewModel extends ViewModel {
    private final String key = "ADMIN";
    private SavedStateHandle handle;


    public MainViewModel(SavedStateHandle handle){
        if(!handle.contains(key)){
            handle.set(key,false);
        }
        this.handle = handle;
    }

    public void setRole(boolean b){
        handle.set(key,b);
    }

    public LiveData<Boolean> getRole(){
        return handle.getLiveData(key);
    }


}
