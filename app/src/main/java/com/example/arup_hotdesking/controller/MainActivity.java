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
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.dreamlive.hotimglibrary.entity.HotArea;
import com.dreamlive.hotimglibrary.utils.FileUtils;
import com.dreamlive.hotimglibrary.view.HotClickView;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
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
    private View popupView;
    TextView reservedTag;
    TextView reservedText;
    TextView statusTag;
    TextView statusText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        toolbar = (Toolbar) this.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawerLayout = findViewById(R.id.drawerlayout);
        mNavigationView = findViewById(R.id.navigationview);
        userViewModel.getIsAdmin().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                mNavigationView.getMenu().getItem(4).setVisible(aBoolean);
                mNavigationView.getMenu().getItem(5).setVisible(aBoolean);
                mNavigationView.getMenu().getItem(6).setVisible(aBoolean);

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
                    case R.id.myBookings://book a seat
                        navController.navigate(R.id.myBookingFragment);
                        break;
                    case R.id.adminFragment://manage users
                        navController.navigate(R.id.adminFragment);
                        break;
                    case R.id.signinseat:
                    signIn();
                        break;
                    case R.id.bookRecord:
                        bookingReports();
                        break;
                    case R.id.checkinRecord:
                        checkinReports();
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

    public void bookingReports(){
        Intent intent= new Intent(MainActivity.this, BookingDateRange.class);
        startActivity(intent);
    }

    public void checkinReports(){
        Intent intent= new Intent(MainActivity.this, checkingDateRange.class);
        startActivity(intent);
    }

    public void signIn(){
        Intent intent= new Intent(MainActivity.this, Signin.class);
        startActivity(intent);
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


        popupView = getLayoutInflater().inflate(R.layout.seat_popup_window,null);

        final Switch lock =  popupView.findViewById(R.id.switch1);
        reservedTag = popupView.findViewById(R.id.reservedTag);
        reservedText = popupView.findViewById(R.id.reservedText);
        statusTag = popupView.findViewById(R.id.statusTag);
        statusText = popupView.findViewById(R.id.statusText);

        userViewModel.getIsAdmin().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean) {
                    reservedTag.setVisibility(View.VISIBLE);
                    reservedText.setVisibility(View.VISIBLE);
                    statusTag.setVisibility(View.VISIBLE);
                    statusText.setVisibility(View.VISIBLE);
                    lock.setVisibility(View.VISIBLE);
                }else{
                    reservedTag.setVisibility(View.INVISIBLE);
                    reservedText.setVisibility(View.INVISIBLE);
                    statusTag.setVisibility(View.INVISIBLE);
                    statusText.setVisibility(View.INVISIBLE);
                    lock.setVisibility(View.INVISIBLE);
                }
            }
        });

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final DocumentReference deskLock = db.collection("Desks").document(hotArea.getAreaTitle());

        deskLock.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    String isActive = task.getResult().getString("Active");
                    if(isActive.equals("True")) {
                        lock.setChecked(true);
                    }else if(isActive.equals("False")){
                       lock.setChecked(false);
                    }
                }
            }
        });

        lock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    deskLock.update("Active", "True");
                }else {
                    deskLock.update("Active", "False");
                }
            }
        });



        PopupWindow popupWindow = new PopupWindow(popupView,800,1200);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setClippingEnabled(false);
        popupWindow.setFocusable(true);
        popupWindow.showAtLocation(view, Gravity.CENTER,0,0);
        TextView seatIDText = popupView.findViewById(R.id.seatID);
        CalendarView calendarView = popupView.findViewById(R.id.customCalendar);
        Button book = popupView.findViewById(R.id.bookButton);
        MyAdapter myAdapter = new MyAdapter();

        int curDay = calendarView.getCurDay();
        int curMonth = calendarView.getCurMonth();
        int curYear = calendarView.getCurYear();
        String reservedEmail = userViewModel.getReservedEmail(curDay,curMonth,curYear);
        reservedText.setText(reservedEmail);
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(curDay).append("/").append(curMonth).append("/").append(curYear);
        db.collection("CheckinRecords").whereEqualTo("User",reservedEmail).whereEqualTo("SeatName",hotArea.getAreaTitle())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot documentSnapshot:task.getResult()){
                                if(documentSnapshot.getString("DateTime").split(" ")[0].equals(stringBuilder.toString()))
                                    statusText.setText("In Use");
                            }
                        }
                    }
                });

        userViewModel.getLiveBookingRecords().observe(this, new BookingRecordsObserver(calendarView,
                 myAdapter));

        seatIDText.setText(hotArea.getAreaTitle());

        setUpCustomCalendar(calendarView,book);

        userViewModel.getDeskRecords(hotArea.getAreaId());
        book.setOnClickListener(new BookingButtonClickListener(calendarView,hotArea));

        userViewModel.getBookingResult().observe(this,new BookingResultObserver(popupWindow));



        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                userViewModel.getBookingResult().removeObservers(MainActivity.this);
                userViewModel.resetLiveRecords();
                userViewModel.removeBookingRecordsListener();
            }
        });
    }

    static class BookingRecordsObserver implements Observer<List<BookingRecord>>{
        CalendarView calendarView;
        RecyclerView recyclerView;
        MyAdapter myAdapter;

        public BookingRecordsObserver(CalendarView calendarView,MyAdapter myAdapter){
            this.calendarView = calendarView;
            this.myAdapter = myAdapter;
        }

        @Override
        public void onChanged(List<BookingRecord> bookingRecords) {
            myAdapter.setBookingRecords(bookingRecords);
            myAdapter.notifyDataSetChanged();
            calendarView.setOnCalendarInterceptListener(new CalendarIntercepter(bookingRecords));
            calendarView.update();

//            for(int i=0;i<bookingRecords.size();i++){
//                List<Calendar> bookingRange = bookingRecords.get(i).getBookingRange();
//                for(int j=0;j<bookingRange.size();j++){
//                    Calendar calendar = bookingRange.get(j);
//                    if(calendar.getDay() == calendarView.getCurDay() &&
//                    calendar.getMonth() == calendarView.getCurMonth() &&
//                    calendar.getYear() == calendarView.getCurYear())
//                        bookingRecords.get(i).getEmail()
//
//                }
//            }
        }
    }

    class BookingResultObserver implements Observer<Boolean>{
        private PopupWindow popupWindow;

        public BookingResultObserver(PopupWindow popupWindow){
            this.popupWindow = popupWindow;
        }

        @Override
        public void onChanged(Boolean aBoolean) {
            if(aBoolean == null){
                return;
            }
            if(aBoolean){
                Toast.makeText(MainActivity.this,"Booking succeeded",Toast.LENGTH_LONG).show();
                userViewModel.resetBookingResult();
                popupWindow.dismiss();
            }
            else{
                Toast.makeText(MainActivity.this,"Booking failed",Toast.LENGTH_LONG).show();
            }
        }
    }

    class BookingButtonClickListener implements View.OnClickListener{
        private CalendarView calendarView;
        private String deskID;
        private String deskTitle;

        public BookingButtonClickListener(CalendarView calendarView,HotArea hotArea){
            this.calendarView = calendarView;
            this.deskID = hotArea.getAreaId();
            this.deskTitle = hotArea.getAreaTitle();
        }

        @Override
        public void onClick(View view) {
            userViewModel.bookSeat(deskID,calendarView.getMultiSelectCalendars(),deskTitle);

        }
    }

    private void setUpCustomCalendar(CalendarView calendarView, Button bookButton){
       // calendarView.setSelectRange(-1,7);


        calendarView.setRange(calendarView.getCurYear(),calendarView.getCurMonth(),calendarView.getCurDay(),
                calendarView.getCurYear()+1,calendarView.getCurMonth(),calendarView.getCurDay());
        calendarView.setOnCalendarMultiSelectListener(new MyCalendarMultiSelectListener(bookButton));
    }

    class MyCalendarMultiSelectListener implements CalendarView.OnCalendarMultiSelectListener{
        private Button button;

        public MyCalendarMultiSelectListener(Button button){
            this.button = button;
        }

        @Override
        public void onCalendarMultiSelectOutOfRange(Calendar calendar) {

        }

        @Override
        public void onMultiSelectOutOfSize(Calendar calendar, int maxSize) {
            Toast.makeText(MainActivity.this,R.string.calendarOutOfRange,Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCalendarMultiSelect(Calendar calendar, int curSize, int maxSize) {
            if(curSize==0) {
                 button.setEnabled(false);
              }
              else button.setEnabled(true);
        }
    }

    static class CalendarIntercepter implements CalendarView.OnCalendarInterceptListener{
        List<BookingRecord> bookingRecords;
        CalendarView calendarView;

        public  CalendarIntercepter(List<BookingRecord> bookingRecords){
            this.bookingRecords = bookingRecords;
        }

        @Override
        public boolean onCalendarIntercept(Calendar calendar) {
            if(bookingRecords == null) return false;
            for(int i=0;i<bookingRecords.size();i++){
                if(bookingRecords.get(i).getBookingRange().contains(calendar)) return true;
            }
            return false;
        }

        @Override
        public void onCalendarInterceptClick(Calendar calendar, boolean isClick) {

        }
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
        public void OnClick(final View view, final HotArea hotArea) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            final DocumentReference deskLock = db.collection("Desks").document(hotArea.getAreaTitle());

            userViewModel.getIsAdmin().observe(MainActivity.this, new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean aBoolean) {
                    if(aBoolean) {
                        seatPopupWindow(view, hotArea);
                    }else{
                        deskLock.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.isSuccessful()){
                                    String isActive = task.getResult().getString("Active");
                                    if(isActive.equals("True")) {
                                        seatPopupWindow(view, hotArea);
                                    }else if(isActive.equals("False")){
                                        Toast.makeText(MainActivity.this,hotArea.getAreaTitle()+" is locked down!",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
                    }
                }
            });
        }
    }



}