package com.example.arup_hotdesking.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.arup_hotdesking.R;
import com.example.arup_hotdesking.databinding.CanteenBinding;
import com.example.arup_hotdesking.model.UserViewModel;

public class Planta5Fragment extends Fragment {
    private final String FILENAME = "planta5";
    private UserViewModel userViewModel;
    private CanteenBinding canteenBinding;

    public  Planta5Fragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        canteenBinding = DataBindingUtil.inflate(inflater,R.layout.canteen,container,false);
        canteenBinding.setData(userViewModel);
        canteenBinding.setLifecycleOwner(requireActivity());

        return canteenBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MainActivity mainActivity = (MainActivity) requireActivity();
        mainActivity.initDatas(FILENAME,canteenBinding.canteenhotview);
    }
}
