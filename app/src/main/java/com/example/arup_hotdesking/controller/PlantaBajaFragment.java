package com.example.arup_hotdesking.controller;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.arup_hotdesking.R;
import com.example.arup_hotdesking.databinding.PlantaBajaBinding;
import com.example.arup_hotdesking.model.UserViewModel;

public class PlantaBajaFragment extends Fragment {
    private final String FILENAME = "plantabaja";
    private UserViewModel userViewModel;
    private PlantaBajaBinding bajaBinding;

    public PlantaBajaFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        bajaBinding = DataBindingUtil.inflate(inflater,R.layout.planta_baja,container,false);
        bajaBinding.setData(userViewModel);
        bajaBinding.setLifecycleOwner(requireActivity());

        return bajaBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MainActivity mainActivity = (MainActivity) requireActivity();
        mainActivity.initDatas(FILENAME,bajaBinding.bajahotview);
    }

}
