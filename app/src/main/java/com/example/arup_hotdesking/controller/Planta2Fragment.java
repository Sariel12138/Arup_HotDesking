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
import com.example.arup_hotdesking.databinding.Planta2Binding;
import com.example.arup_hotdesking.model.UserViewModel;

public class Planta2Fragment extends Fragment {
    private final String FILENAME = "planta2";
    private UserViewModel userViewModel;
    private Planta2Binding planta2Binding;

    public Planta2Fragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        planta2Binding = DataBindingUtil.inflate(inflater,R.layout.planta_2,container,false);
        planta2Binding.setData(userViewModel);
        planta2Binding.setLifecycleOwner(requireActivity());

        return planta2Binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MainActivity mainActivity = (MainActivity) requireActivity();
        mainActivity.initDatas(FILENAME,planta2Binding.planta2hotview);
    }
}
