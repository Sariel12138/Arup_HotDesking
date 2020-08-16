package com.example.arup_hotdesking;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.arup_hotdesking.databinding.FragmentProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileFragment extends Fragment {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FragmentProfileBinding binding;

    public static ProfileViewModel newInstance() {
        return new ProfileViewModel();
    }

    private ProfileViewModel mViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_profile,container,false);
        binding.setData(mViewModel);
        binding.setLifecycleOwner(requireActivity());
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        initUI(user);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        //mViewModel = ViewModelProviders.of(this).get(AdminViewModel.class);
        // TODO: Use the ViewModel
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
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

            TextView textView = binding.textView;
            TextView textView1 = binding.textView2;
            textView.setText(user.getEmail());
            textView1.setText(user.getDisplayName() == null ? "Arup Employee" : user.getDisplayName());
            adminText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NavController controller = Navigation.findNavController(v);
                    controller.navigate(R.id.action_profileFragment_to_adminFragment);
                }
            });
            Button signOutButton = binding.signOutButton;
            signOutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mAuth.signOut();
                    signOutUI();
                }
            });
        }
    }

    private void signOutUI(){
        Intent intent = new Intent(getActivity(),LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}