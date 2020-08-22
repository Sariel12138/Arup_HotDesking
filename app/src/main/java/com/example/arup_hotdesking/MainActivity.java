package com.example.arup_hotdesking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private NavController navController;
    private AppBarConfiguration appBarConfiguration;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) this.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawerLayout = findViewById(R.id.drawerlayout);
        mNavigationView = findViewById(R.id.navigationview);
        navController = Navigation.findNavController(this, R.id.fragment);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph())
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
                        navController.navigate(R.id.seatsFragment);
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

}