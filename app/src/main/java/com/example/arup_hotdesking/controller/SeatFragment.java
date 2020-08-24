package com.example.arup_hotdesking.controller;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.arup_hotdesking.R;

import cn.qqtheme.framework.picker.OptionPicker;


public class SeatFragment extends Fragment {

//    private String[] list = new String[]{"Planta 1", "Planta 2", "Planta 3", "Planta 4", "Planta 5", "Planta Baja"};
//    Button selectPlanta;
//    View map;
//    TextView choosedPlanta;
//    ViewGroup parent;
//    int Cindex;

    public SeatFragment() {
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

    }


    /*    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final OptionPicker picker = new OptionPicker(this.getActivity(), list);
        picker.setOffset(2);
        picker.setSelectedIndex(1);
        picker.setTextSize(18);
        picker.setCycleDisable(true);
        selectPlanta = getView().findViewById(R.id.button2);
        choosedPlanta =  getView().findViewById(R.id.textView4);
        map = getView().findViewById(R.id.plantamap);
        parent = (ViewGroup) map.getParent();
        Cindex = parent.indexOfChild(map);

        selectPlanta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
                    @Override
                    public void onOptionPicked(int index, String item) {
                        choosedPlanta.setText(item);

                        switch (index){
                            case 0://planta 1
                                parent.removeView(map);
                                map = getLayoutInflater().inflate(R.layout.planta_recepcion, parent, false);
                                parent.addView(map, Cindex);
                                break;
                            case 1://planta 2
                                parent.removeView(map);
                                map = getLayoutInflater().inflate(R.layout.planta_2, parent, false);
                                parent.addView(map, Cindex);
                                break;
                            case 2://planta 3
                                parent.removeView(map);
                                map = getLayoutInflater().inflate(R.layout.planta_3, parent, false);
                                parent.addView(map, Cindex);
                                break;
                            case 3://planta 4
                                parent.removeView(map);
                                map = getLayoutInflater().inflate(R.layout.planta_4, parent, false);
                                parent.addView(map, Cindex);
                                break;
                            case 4://planta 5
                                parent.removeView(map);
                                map = getLayoutInflater().inflate(R.layout.planta_5, parent, false);
                                parent.addView(map, Cindex);
                                break;
                            case 5://planta baja
                                parent.removeView(map);
                                map = getLayoutInflater().inflate(R.layout.planta_baja, parent, false);
                                parent.addView(map, Cindex);
                                break;
                        }

                    }
                });
                picker.show();
            }
        });


    }*/
}