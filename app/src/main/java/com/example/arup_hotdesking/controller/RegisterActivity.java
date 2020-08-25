package com.example.arup_hotdesking.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.arup_hotdesking.R;
import com.example.arup_hotdesking.databinding.ActivityRegisterBinding;
import com.example.arup_hotdesking.model.LoginRegisterViewModel;
import com.example.arup_hotdesking.model.RegisterFormState;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "Register";
    private LoginRegisterViewModel registerViewModel;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //MultiDex.install(this);
        //setContentView(R.layout.activity_register);
        registerViewModel = new ViewModelProvider(this).get(LoginRegisterViewModel.class);
        ActivityRegisterBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_register);
        binding.setData(registerViewModel);
        binding.setLifecycleOwner(this);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        final EditText displayNameText = binding.displayName;
        final EditText emailText = binding.email;
        final EditText passwordText = binding.password;
        final EditText confirmPasswordText = binding.confirmPassword;
        final Button registerButton = binding.registerButton;
        progressBar = binding.progressBar;

        registerViewModel.getRegisterFormState().observe(this, new Observer<RegisterFormState>() {
            @Override
            public void onChanged(RegisterFormState registerFormState) {
                if(registerFormState == null){return;}
                registerButton.setEnabled(registerFormState.isDataValid());
                if(registerFormState.getEmailError() != null){
                    emailText.setError(getString(registerFormState.getEmailError()));
                }
                if(registerFormState.getPasswordError() != null){
                    passwordText.setError(getString(registerFormState.getPasswordError()));
                }
                if(registerFormState.getConfirmPasswordError() != null){
                    confirmPasswordText.setError(getString(registerFormState.getConfirmPasswordError()));
                }
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                registerViewModel.registerDataChanged(emailText.getText().toString(),
                        passwordText.getText().toString(),confirmPasswordText.getText().toString());
            }
        };

        emailText.addTextChangedListener(afterTextChangedListener);
        passwordText.addTextChangedListener(afterTextChangedListener);
        confirmPasswordText.addTextChangedListener(afterTextChangedListener);

        //IME option
        /*confirmPasswordText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    progressBar.setVisibility(View.VISIBLE);
                    register(emailText.getText().toString(),
                            confirmPasswordText.getText().toString(),
                            displayNameText.getText().toString());
                }
                return false;
            }
        });*/

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                register(emailText.getText().toString(),
                        confirmPasswordText.getText().toString(),
                        displayNameText.getText().toString());
            }
        });
    }

    private void register(final String email, final String password, final String displayName) {

        DocumentReference employees = db.collection("employees").document(email);
        employees.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot.exists()){
                        authenticate(email,password,displayName);
                        Log.d("whitelist","DocumentSnapshot data: " + documentSnapshot.getData());
                    }else {
                        Toast.makeText(RegisterActivity.this,getString(R.string.registerFailedString), Toast.LENGTH_LONG).show();
                        Log.d("whitelist","No such employee");
                    }
                }else {
                    Toast.makeText(RegisterActivity.this,getString(R.string.connectionFailed), Toast.LENGTH_LONG).show();
                    Log.d("whitelist","get failed with ", task.getException());
                }
                progressBar.setVisibility(View.GONE);
            }
        });

    }

    private void authenticate(String email, String password, final String displayName){
         mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateDisplayName(user,displayName);
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    private void updateDisplayName(FirebaseUser user,String displayName) {
        if(displayName != null && !displayName.trim().isEmpty()){
            UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                    .setDisplayName(displayName).build();
            user.updateProfile(profileChangeRequest)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "User profile updated.");
                            }
                        }
                    });
        }
    }

    private void updateUI(FirebaseUser user) {
        if(user!=null) {
            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}