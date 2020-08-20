package com.example.arup_hotdesking;

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

import com.example.arup_hotdesking.databinding.FragmentAdminBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AdminFragment extends Fragment {
    FragmentAdminBinding binding;

    public static AdminFragment newInstance() {
        return new AdminFragment();
    }

    private AdminViewModel mViewModel;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.activity_main,container,false);
        binding.setData(mViewModel);
        binding.setLifecycleOwner(requireActivity());
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        final EditText editText = binding.newEmail;
        Button button = binding.Add;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DocumentReference employees = db.collection("employees").document(editText.getText().toString());
                Map<String, Object> employee = new HashMap<>();
                employee.put("admin",false);
                employee.put("job","test");
                employees.set(employee).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Log.d("add","document written");
                        }else {
                            Log.d("add","Adding unsuccessful");
                        }
                    }
                });

            }
        });
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(AdminViewModel.class);
        //mViewModel = ViewModelProviders.of(this).get(AdminViewModel.class);
        // TODO: Use the ViewModel
    }

}