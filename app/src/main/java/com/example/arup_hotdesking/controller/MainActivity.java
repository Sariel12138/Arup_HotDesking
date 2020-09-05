package com.example.arup_hotdesking.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.dreamlive.hotimglibrary.entity.HotArea;
import com.dreamlive.hotimglibrary.utils.FileUtils;
import com.dreamlive.hotimglibrary.view.HotClickView;
import com.example.arup_hotdesking.R;
import com.example.arup_hotdesking.model.BookingRecord;
import com.example.arup_hotdesking.model.MyAdapter;
import com.example.arup_hotdesking.model.UserViewModel;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarView;

import java.io.InputStream;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private UserViewModel userViewModel;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private NavController navController;
    private AppBarConfiguration appBarConfiguration;
    private Toolbar toolbar;
    private MyAdapter myAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        myAdapter = new MyAdapter();
        toolbar = (Toolbar) this.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawerLayout = findViewById(R.id.drawerlayout);
        mNavigationView = findViewById(R.id.navigationview);
        userViewModel.getIsAdmin().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                mNavigationView.getMenu().getItem(2).setVisible(aBoolean);
            }
        });
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


    private void signOutUI(){
        Intent intent = new Intent(MainActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void seatPopupWindow(View view,HotArea hotArea){
        View contentView = getLayoutInflater().inflate(R.layout.seat_popup_window,null);
        PopupWindow popupWindow = new PopupWindow(contentView,800,1200);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setClippingEnabled(false);
        popupWindow.setFocusable(true);
        popupWindow.showAtLocation(view, Gravity.CENTER,0,0);
        TextView seatIDText = contentView.findViewById(R.id.seatID);
        seatIDText.setText(hotArea.getAreaTitle());

        setUpCustomCalendar(contentView);

        setUpPopupWindowRecyclerView(contentView);





        userViewModel.getDeskRecords(hotArea.getAreaId());

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                userViewModel.resetLiveRecords();
            }
        });
    }

    private void setUpCustomCalendar(View contentView){
        CalendarView calendarView = contentView.findViewById(R.id.customCalendar);
        calendarView.setSelectRange(-1,7);
        calendarView.setRange(calendarView.getCurYear(),calendarView.getCurMonth(),calendarView.getCurDay(),
                calendarView.getCurYear()+1,calendarView.getCurMonth(),calendarView.getCurDay());
        calendarView.setOnCalendarRangeSelectListener(new CalendarView.OnCalendarRangeSelectListener() {
            @Override
            public void onCalendarSelectOutOfRange(Calendar calendar) {

            }

            @Override
            public void onSelectOutOfRange(Calendar calendar, boolean isOutOfMinRange) {
                if(!isOutOfMinRange) Toast.makeText(MainActivity.this,"> 7",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCalendarRangeSelect(Calendar calendar, boolean isEnd) {
                if(!isEnd) Toast.makeText(MainActivity.this,"notEnd"+calendar.isWeekend(),Toast.LENGTH_LONG).show();
                else Toast.makeText(MainActivity.this,"End"+calendar.isWeekend(),Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setUpPopupWindowRecyclerView(View contentView){
        RecyclerView recyclerView = contentView.findViewById(R.id.recyclerView);
        //CalendarView calendarView = contentView.findViewById(R.id.customCalendar);  //TODO
//        myAdapter = new MyAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(contentView.getContext()));
        recyclerView.setAdapter(myAdapter);

        userViewModel.getLiveBookingRecords().observe(this, new Observer<List<BookingRecord>>() {
            @Override
            public void onChanged(List<BookingRecord> bookingRecords) {
                myAdapter.setBookingRecords(bookingRecords);
                myAdapter.notifyDataSetChanged();
            }
        });
    }


    public void initDatas(String filename, HotClickView hotClickView) {
        AssetManager assetManager = getResources().getAssets();
        InputStream imgInputStream = null;
        InputStream fileInputStream = null;
        try {
            imgInputStream = assetManager.open(filename+".png");
            fileInputStream = assetManager.open(filename+".xml");
            hotClickView.setImageBitmap(fileInputStream, imgInputStream, HotClickView.FIT_XY);
            hotClickView.setOnClickListener(new HotClickListener());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            FileUtils.closeInputStream(imgInputStream);
            FileUtils.closeInputStream(fileInputStream);
        }
    }

    class HotClickListener implements HotClickView.OnClickListener{

        @Override
        public void OnClick(View view, HotArea hotArea) {
            seatPopupWindow(view,hotArea);
        }
    }



}