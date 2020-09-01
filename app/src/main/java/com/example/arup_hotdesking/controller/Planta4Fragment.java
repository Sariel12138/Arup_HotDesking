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
import com.example.arup_hotdesking.databinding.Planta4Binding;
import com.example.arup_hotdesking.model.UserViewModel;

public class Planta4Fragment extends Fragment {
    private final String FILENAME = "planta4";
    private UserViewModel userViewModel;
    private Planta4Binding planta4Binding;

    public Planta4Fragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        planta4Binding = DataBindingUtil.inflate(inflater,R.layout.planta_4,container,false);
        planta4Binding.setData(userViewModel);
        planta4Binding.setLifecycleOwner(requireActivity());

        return planta4Binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MainActivity mainActivity = (MainActivity) requireActivity();
        mainActivity.initDatas(FILENAME,planta4Binding.planta4hotview);
    }
}
