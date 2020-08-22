package com.example.arup_hotdesking;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import cn.qqtheme.framework.picker.OptionPicker;


public class seats extends Fragment {

    private String[] list = new String[]{"Planta 1", "Planta 2", "Planta 3", "Planta 4"};
    Button selectPlanta;

    public seats() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_seats, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final OptionPicker picker = new OptionPicker(this.getActivity(), list);
        picker.setOffset(2);
        picker.setSelectedIndex(1);
        picker.setTextSize(18);
        picker.setCycleDisable(true);
        selectPlanta = getView().findViewById(R.id.button2);
        final TextView textview =  getView().findViewById(R.id.textView4);
        selectPlanta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
                    @Override
                    public void onOptionPicked(int index, String item) {
                        textview.setText(item);
                    }
                });
                picker.show();
            }
        });


    }
}