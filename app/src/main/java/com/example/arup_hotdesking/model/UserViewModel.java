package com.example.arup_hotdesking.model;

import android.graphics.drawable.Drawable;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class UserViewModel extends ViewModel {
    private int workSpace = 0;
    private MutableLiveData<Drawable> workSpaceIcon = new MutableLiveData<>();

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
}
