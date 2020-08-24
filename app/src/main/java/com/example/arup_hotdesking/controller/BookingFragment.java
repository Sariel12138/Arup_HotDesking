package com.example.arup_hotdesking.controller;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.arup_hotdesking.R;
import com.example.arup_hotdesking.databinding.FragmentBookingBinding;
import com.example.arup_hotdesking.model.UserViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BookingFragment extends Fragment {

    private UserViewModel userViewModel;
    private FragmentBookingBinding bookingBinding;
    NavController navController;
    BottomNavigationView bottomNavigationView;

    public BookingFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        bookingBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_booking,container,false);
        bookingBinding.setData(userViewModel);
        bookingBinding.setLifecycleOwner(requireActivity());


        // Inflate the layout for this fragment
        return bookingBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        bottomNavigationView = bookingBinding.bottomNavView;
        navController = Navigation.findNavController(requireActivity(),R.id.fragment_planta);
        AppBarConfiguration configuration = new AppBarConfiguration.Builder(bottomNavigationView.getMenu()).build();
        NavigationUI.setupActionBarWithNavController((AppCompatActivity) requireActivity(),navController,configuration);
        NavigationUI.setupWithNavController(bottomNavigationView,navController);

        userViewModel.getWorkSpaceIcon().observe(requireActivity(), new Observer<Drawable>() {
            @Override
            public void onChanged(Drawable drawable) {
                bottomNavigationView.getMenu().getItem(1).setIcon(drawable);
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomSelectedListener(R.id.planta2Fragment));
        bottomNavigationView.setOnNavigationItemReselectedListener(new BottomReSelectedListener());
    }

    class BottomSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener{

        private int itemID;

        public BottomSelectedListener(int itemID){
            this.itemID = itemID;
        }

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){
                case R.id.recepcion:
                    navController.navigate(R.id.plantaRecepcionFragment);
                    break;
                case R.id.workspace:
                    navController.navigate(itemID);
                    break;
                case R.id.baja:
                    navController.navigate(R.id.plantaBajaFragment);
                    break;
            }
            return true;
        }
    }

    class BottomReSelectedListener implements BottomNavigationView.OnNavigationItemReselectedListener{

        @Override
        public void onNavigationItemReselected(@NonNull MenuItem item) {
            if(item.getItemId() == R.id.workspace){
                popupMenu(bottomNavigationView);
            }
        }
    }

    private void popupMenu(View v){
        PopupMenu popupMenu = new PopupMenu(requireContext(),v);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu,popupMenu.getMenu());
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.planta2:
                        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomSelectedListener(R.id.planta2Fragment));
                        navController.navigate(R.id.planta2Fragment);
                        break;
                    case R.id.planta3:
                        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomSelectedListener(R.id.planta3Fragment));
                        navController.navigate(R.id.planta3Fragment);
                        break;
                    case R.id.planta4:
                        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomSelectedListener(R.id.planta4Fragment));
                        navController.navigate(R.id.planta4Fragment);
                        break;
                    case R.id.planta5:
                        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomSelectedListener(R.id.planta5Fragment));
                        navController.navigate(R.id.planta5Fragment);
                        break;
                }
                bottomNavigationView.getMenu().getItem(1).setIcon(menuItem.getIcon());
                return true;
            }
        });
    }

}