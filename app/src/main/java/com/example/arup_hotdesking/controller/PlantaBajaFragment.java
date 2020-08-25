package com.example.arup_hotdesking.controller;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.arup_hotdesking.R;
import com.example.arup_hotdesking.databinding.PlantaBajaBinding;
import com.example.arup_hotdesking.model.UserViewModel;
import com.google.rpc.context.AttributeContext;

import java.util.ArrayList;
import java.util.List;

public class PlantaBajaFragment extends Fragment {
    private List<Button> seats = new ArrayList<>();
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

        View baja = bajaBinding.getRoot();
        for(View view:baja.getTouchables()){
            if(view instanceof Button){
                //Log.d("add",String.valueOf(view.getId()));
                seats.add((Button) view);
            }
        }

        return baja;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        for(Button seat:seats){
            //Log.d("set",String.valueOf(seat.getId()));
            seat.setOnClickListener(new SeatClickListener(seat.getId()));
        }
    }





    class SeatClickListener implements View.OnClickListener{
        private String seatName;

        public SeatClickListener(int id){
            seatName = getResources().getResourceEntryName(id);
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(requireContext(),seatName,Toast.LENGTH_SHORT).show();
            //TODO popup window: set booking detail
        }
    }
}
