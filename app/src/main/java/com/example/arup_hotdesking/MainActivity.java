package com.example.arup_hotdesking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private final String key = "ADMIN";
    private MainViewModel viewModel;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        binding.setData(viewModel);
        binding.setLifecycleOwner(this);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        initUI(user);

        Button button = findViewById(R.id.signOutButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                signOutUI();
            }
        });*/
    }

    /*@Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        initUI(user);
    }

    private void initUI(final FirebaseUser user){
        final TextView adminText = binding.admintext;
        if(user != null){
            DocumentReference employees = db.collection("employees").document(user.getEmail());
            employees.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        if (documentSnapshot.exists() && documentSnapshot.getBoolean("admin")) {
                            adminText.setVisibility(View.VISIBLE);
                            //adminText.setClickable(true);
                        }
                    }
                }
            });

            final TextView textView = binding.textView;
            TextView textView1 = binding.textView2;
            textView.setText(user.getEmail());
            textView1.setText(user.getDisplayName() == null ? "Arup Employee" : user.getDisplayName());
            adminText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this,AdminActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }


    private void signOutUI(){
        Intent intent = new Intent(MainActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }

    //menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menulist,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.homepage:
                break;
            case R.id.bookseat:
                break;
            case R.id.manageuser:
                break;
            case R.id.logout:
                mAuth.signOut();
                signOutUI();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }*/
}