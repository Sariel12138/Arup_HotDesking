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
import com.example.arup_hotdesking.databinding.Planta3Binding;
import com.example.arup_hotdesking.model.UserViewModel;

public class Planta3Fragment extends Fragment {
    private final String FILENAME = "planta3";
    private UserViewModel userViewModel;
    private Planta3Binding planta3Binding;

    public Planta3Fragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        planta3Binding = DataBindingUtil.inflate(inflater,R.layout.planta_3,container,false);
        planta3Binding.setData(userViewModel);
        planta3Binding.setLifecycleOwner(requireActivity());

        return planta3Binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MainActivity mainActivity = (MainActivity) requireActivity();
        mainActivity.initDatas(FILENAME,planta3Binding.planta3hotview);
    }
}
