package com.example.arup_hotdesking.model;

import android.graphics.drawable.Drawable;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class UserViewModel extends ViewModel {
    private int workSpace;
    private MutableLiveData<Drawable> workSpaceIcon = new MutableLiveData<>();

    public MutableLiveData<Drawable> getWorkSpaceIcon() {
        return workSpaceIcon;
    }

    public void setWorkSpaceIcon(MutableLiveData<Drawable> workSpaceIcon) {
        this.workSpaceIcon = workSpaceIcon;
    }

    public int getWorkSpace() {
        return workSpace;
    }

    public void setWorkSpace(int workSpace) {
        this.workSpace = workSpace;
    }
}
