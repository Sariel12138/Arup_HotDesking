package com.example.arup_hotdesking.controller;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.arup_hotdesking.R;
import com.example.arup_hotdesking.databinding.FragmentProfileBinding;
import com.example.arup_hotdesking.model.UserViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileFragment extends Fragment {
    private FirebaseAuth mAuth;
    private FragmentProfileBinding binding;
    private Button changePwd;
    MainActivity mainActivity;
    private NavController navController;


    public static UserViewModel newInstance() {
        return new UserViewModel();
    }

    private UserViewModel mViewModel;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile,container,false);
        binding.setData(mViewModel);
        binding.setLifecycleOwner(requireActivity());
        return binding.getRoot();


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        FirebaseUser user = mViewModel.getUser();
        if(user!=null){
            binding.emailText.setText(user.getEmail());

            mViewModel.getDisplayName().observe(requireActivity(), new Observer<String>() {
                @Override
                public void onChanged(String s) {
                    binding.displayNameText.setText(s);
                }
            });
            //TODO
            mainActivity = (MainActivity) requireActivity();

            changePwd = mainActivity.findViewById(R.id.buttonchangepwd);

            navController = Navigation.findNavController(mainActivity, R.id.fragment);

            changePwd.setOnClickListener(new changePasswordListener(navController));
        }

    }

    private class changePasswordListener implements View.OnClickListener{
        private NavController navController;

        public changePasswordListener(NavController navController){
            this.navController = navController;
        }

        @Override
        public void onClick(View view) {
            navController.navigate(R.id.changePwdFragment);
        }
    }
}