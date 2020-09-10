package com.example.arup_hotdesking.controller;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.arup_hotdesking.R;
import com.example.arup_hotdesking.model.BookingRecord;
import com.example.arup_hotdesking.model.MyAdapter;
import com.example.arup_hotdesking.model.MyBookingAdapter;
import com.example.arup_hotdesking.model.UserViewModel;

import java.util.List;

public class MyBookingFragment extends Fragment {
    private UserViewModel userViewModel;
    private RecyclerView recyclerView;
    private MyBookingAdapter myBookingAdapter;
    MainActivity mainActivity;

    public MyBookingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView = getView().findViewById(R.id.mybookingrecyclerView);
        myBookingAdapter = new MyBookingAdapter();
        mainActivity = (MainActivity) requireActivity();

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(myBookingAdapter);

        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        userViewModel.getUserRecords(userViewModel.getUser().getEmail());
        userViewModel.getUserLiveBookingRecords().observe(getViewLifecycleOwner(), new Observer<List<BookingRecord>>() {
            @Override
            public void onChanged(List<BookingRecord> bookingRecords) {
                myBookingAdapter.setBookingRecords(bookingRecords, userViewModel);
                myBookingAdapter.notifyDataSetChanged();
            }
        });


    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_booking, container, false);
    }
}