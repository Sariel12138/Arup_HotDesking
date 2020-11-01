package com.example.arup_hotdesking.controller;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.arup_hotdesking.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.Executor;


public class changePwdFragment extends Fragment {

    private EditText oldPwd;
    private EditText newPwd;
    private EditText newPwdConfirm;
    private Button changebtn;
    MainActivity mainActivity;
    FirebaseAuth mAuth;

    public changePwdFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        oldPwd = getView().findViewById(R.id.password2);
        newPwd = getView().findViewById(R.id.password3);
        newPwdConfirm = getView().findViewById(R.id.password4);
        changebtn = getView().findViewById(R.id.changeButton);
        mainActivity = (MainActivity) requireActivity();
        mAuth = FirebaseAuth.getInstance();

       // oldPwd.addTextChangedListener(new PwdTextWatcher(oldPwd));
        newPwd.addTextChangedListener(new PwdTextWatcher(newPwd));
        newPwdConfirm.addTextChangedListener(new PwdTextWatcher(newPwdConfirm));

        changebtn.setOnClickListener(new changePwdBtnListener());

    }

    /**
     * a listener for new password text area, chaeck if the new password is valid
     */
    private class PwdTextWatcher implements TextWatcher {

        EditText password;

        public PwdTextWatcher (EditText password){
            this.password = password;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
           if(isPasswordValid(password.getText().toString()) == false){
               password.setError("Password must be > 5 characters");
           }

           if(password.getId() == R.id.password4){
               if(isPasswordSame(newPwd.getText().toString(), password.getText().toString()) == false){
                   password.setError("Inconsistent password");
               }
           }

           if(isPasswordValid(newPwd.getText().toString()) && isPasswordValid(newPwdConfirm.getText().toString())
                   && isPasswordSame(newPwd.getText().toString(), newPwdConfirm.getText().toString())){
               changebtn.setEnabled(true);
           }else{
               changebtn.setEnabled(false);
           }
        }
    }

    /**
     * a listener for the confirm button
     */
    private class changePwdBtnListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            String email = mAuth.getCurrentUser().getEmail();
            String password = oldPwd.getText().toString();
            final String newPassword = newPwd.getText().toString();

            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(mainActivity, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        mAuth.getCurrentUser().updatePassword(newPassword);
                        Toast.makeText(mainActivity, getString(R.string.changePasswordSuccess), Toast.LENGTH_LONG).show();
                        FirebaseAuth.getInstance().signOut();
                        signOutUI();
                    }else{
                        Toast.makeText(mainActivity, getString(R.string.wrongOldPassword), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_change_pwd, container, false);
    }

    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }

    private boolean isPasswordSame(String password,String confirmPassword){
        return password.equals(confirmPassword);
    }

    private void signOutUI(){
        Intent intent = new Intent(mainActivity,LoginActivity.class);
        startActivity(intent);
        mainActivity.finish();
    }

}