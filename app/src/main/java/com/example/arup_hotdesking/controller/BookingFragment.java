package com.example.arup_hotdesking.controller;

import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.dreamlive.hotimglibrary.entity.HotArea;
import com.dreamlive.hotimglibrary.utils.FileUtils;
import com.dreamlive.hotimglibrary.view.HotClickView;
import com.example.arup_hotdesking.R;
import com.example.arup_hotdesking.databinding.FragmentBookingBinding;
import com.example.arup_hotdesking.model.MyAdapter;
import com.example.arup_hotdesking.model.UserViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.InputStream;

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

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomSelectedListener(userViewModel.getWorkSpace()));
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
                case R.id.baja:
                    navController.navigate(R.id.plantaBajaFragment);
                    break;
                case R.id.workspace:
                    navController.navigate(itemID);
                    break;
                case R.id.canteen:
                    navController.navigate(R.id.plantaCanteenFragment);
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
            private int planta;
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.planta1:
                        planta = R.id.planta1Fragment;
                        break;
                    case R.id.planta2:
                        planta = R.id.planta2Fragment;
                        break;
                    case R.id.planta3:
                        planta = R.id.planta3Fragment;
                        break;
                    case R.id.planta4:
                        planta = R.id.planta4Fragment;
                        break;
                }
                bottomNavigationView.setOnNavigationItemSelectedListener(new BottomSelectedListener(planta));
                userViewModel.setWorkSpace(planta);
                navController.navigate(planta);
                userViewModel.setWorkSpaceIcon(menuItem.getIcon());
                return true;
            }
        });
    }
}