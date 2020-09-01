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
import com.example.arup_hotdesking.databinding.Planta1Binding;
import com.example.arup_hotdesking.model.UserViewModel;

public class PlantaRecepcionFragment extends Fragment {
    private final String FILENAME = "planta1";
    private UserViewModel userViewModel;
    private Planta1Binding planta1Binding;

    public PlantaRecepcionFragment(){
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        planta1Binding = DataBindingUtil.inflate(inflater,R.layout.planta_1,container,false);
        planta1Binding.setData(userViewModel);
        planta1Binding.setLifecycleOwner(requireActivity());

        return planta1Binding.getRoot();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MainActivity mainActivity = (MainActivity) requireActivity();
        mainActivity.initDatas(FILENAME,planta1Binding.planta1hotview);
    }
}
