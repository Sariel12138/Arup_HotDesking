package com.example.arup_hotdesking.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arup_hotdesking.R;
import com.example.arup_hotdesking.model.BookingRecord;
import com.example.arup_hotdesking.model.MyAdapter;
import com.example.arup_hotdesking.model.UserViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private UserViewModel userViewModel;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private NavController navController;
    private AppBarConfiguration appBarConfiguration;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        toolbar = (Toolbar) this.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawerLayout = findViewById(R.id.drawerlayout);
        mNavigationView = findViewById(R.id.navigationview);
        navController = Navigation.findNavController(this, R.id.fragment);
        appBarConfiguration = new AppBarConfiguration.Builder(R.id.profile_nav_graph,R.id.adminFragment,R.id.bookingFragment)
                .setDrawerLayout(mDrawerLayout)
                .build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        LoadNavItemSelListener();

    }

    private void LoadNavItemSelListener() {
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("ResourceType")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.profileFragment://home page
                        navController.navigate(R.id.profileFragment);
                        break;
                    case R.id.bookseat://book a seat
                        navController.navigate(R.id.bookingFragment);
                        break;
                    case R.id.adminFragment://manage users
                        navController.navigate(R.id.adminFragment);
                        break;
                    case R.id.logout://log out
                        FirebaseAuth.getInstance().signOut();
                        signOutUI();
                        break;
                }

                mDrawerLayout.closeDrawer(GravityCompat.START);

                return true;
            }
        });

    }


    @Override
    public boolean onSupportNavigateUp() {
        NavigationUI.navigateUp(navController, appBarConfiguration);
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }

    public MenuItem getAdminMenuItem(){
        return mNavigationView.getMenu().getItem(2);
    }

    private void signOutUI(){
        Intent intent = new Intent(MainActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void seatPopupWindow(View view){
        View contentView = getLayoutInflater().inflate(R.layout.seat_popup_window,null);
        String deskID = getResources().getResourceEntryName(view.getId());
        PopupWindow popupWindow = new PopupWindow(contentView,800,1200);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setClippingEnabled(false);
        popupWindow.setFocusable(true);
        popupWindow.showAsDropDown(view);
        //popupWindow.showAtLocation(binding.getRoot(), Gravity.NO_GRAVITY,150,50);
        TextView seatIDText = contentView.findViewById(R.id.seatID);
        seatIDText.setText(deskID);
        RecyclerView recyclerView = contentView.findViewById(R.id.recyclerView);
        MyAdapter myAdapter = new MyAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(contentView.getContext()));
        recyclerView.setAdapter(myAdapter);

        userViewModel.getDeskRecords(deskID);   //TODO log test

        myAdapter.setBookingRecords();  //TODO inside clicklistener(myAdapter.notifyDataSetChanged)

    }

}