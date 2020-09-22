package com.example.arup_hotdesking.controller;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.arup_hotdesking.R;
import com.example.arup_hotdesking.databinding.FragmentAdminBinding;
import com.example.arup_hotdesking.model.UserViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AdminFragment extends Fragment {
    FragmentAdminBinding binding;

    public static AdminFragment newInstance() {
        return new AdminFragment();
    }

    private UserViewModel mViewModel;
    private FirebaseFirestore db;
    MainActivity mainActivity;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_admin,container,false);
        binding.setData(mViewModel);
        binding.setLifecycleOwner(requireActivity());


        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        //mViewModel = ViewModelProviders.of(this).get(AdminViewModel.class);
        // TODO: Use the ViewModel
        db = FirebaseFirestore.getInstance();
        mainActivity = (MainActivity) requireActivity();


        //add a new user
        final EditText editText = binding.newEmail;
        Button button = binding.Add;

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editText.getText().toString().isEmpty()){
                    editText.setError("Please enter the email");
                }else {
                    final DocumentReference employees = db.collection("employees").document(editText.getText().toString());

                    employees.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot documentSnapshot = task.getResult();
                                if (documentSnapshot != null && documentSnapshot.exists()) {
                                    editText.setError("User already exists!");
                                } else {
                                    Map<String, Object> employee = new HashMap<>();
                                    employee.put("admin", false);
                                    employee.put("job", "employee");
                                    employees.set(employee).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d("add", "document written");
                                                Toast.makeText(mainActivity, getString(R.string.AddtoWhiteList), Toast.LENGTH_LONG).show();
                                            } else {
                                                Log.d("add", "Adding unsuccessful");
                                                Toast.makeText(mainActivity, getString(R.string.failMessage), Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                                }
                            }
                        }
                    });
                }
            }
        });

        //make a normal user to be an admin
        final EditText editText2 = binding.newEmail2;
        Button button2 = binding.add2;
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(editText2.getText().toString().isEmpty()){
                    editText2.setError("Please enter the email");
                }else {
                    final DocumentReference employees = db.collection("employees").document(editText2.getText().toString());

                    employees.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot documentSnapshot = task.getResult();
                                if (documentSnapshot != null && documentSnapshot.exists()) {
                                    if(documentSnapshot.getBoolean("admin") == true){
                                        editText2.setError("The user is already an admin!");
                                    }else{
                                        //not an admin
                                        employees.update("admin", true).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    Toast.makeText(mainActivity, getString(R.string.upgradeSuccess), Toast.LENGTH_LONG).show();
                                                }else {
                                                    Toast.makeText(mainActivity, getString(R.string.failMessage), Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });
                                    }
                                    //not exist
                                } else {
                                    editText2.setError("User not exists!");
                                }
                            }
                        }
                    });
                }
            }
        });

        //delete a user
        final EditText editText3 = binding.newEmail3;
        Button button3 = binding.add3;

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editText3.getText().toString().isEmpty()){
                    editText3.setError("Please enter the email");
                }else {
                    final DocumentReference employees = db.collection("employees").document(editText3.getText().toString());
                    employees.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot documentSnapshot = task.getResult();
                                if (documentSnapshot != null && documentSnapshot.exists()) {
                                    employees.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(mainActivity, getString(R.string.deleteUserSuccess), Toast.LENGTH_LONG).show();
                                            }else {
                                                Toast.makeText(mainActivity, getString(R.string.failMessage), Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                                }else{
                                    editText3.setError("User not exists!");
                                }
                            }
                        }
                    });
                }
            }
        });


        //make a admin to be a normal user
        final EditText editText4 = binding.newEmail4;
        Button button4 = binding.add4;

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editText4.getText().toString().isEmpty()){
                    editText4.setError("Please enter the email");
                }else {
                    final DocumentReference employees = db.collection("employees").document(editText4.getText().toString());
                    employees.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot documentSnapshot = task.getResult();
                                if (documentSnapshot != null && documentSnapshot.exists()) {
                                    if(documentSnapshot.getBoolean("admin") == true){
                                       //the user is an admin
                                        employees.update("admin", false).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    Toast.makeText(mainActivity, getString(R.string.degradeSuccess), Toast.LENGTH_LONG).show();
                                                }else {
                                                    Toast.makeText(mainActivity, getString(R.string.failMessage), Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });
                                    }else{
                                        editText4.setError("The user is already a normal user!");
                                    }
                                }else{
                                    editText4.setError("User not exists!");
                                }
                            }
                        }
                    });

                }
            }
        });




    }

}