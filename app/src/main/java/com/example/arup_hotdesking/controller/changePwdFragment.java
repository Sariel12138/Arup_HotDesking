package com.example.arup_hotdesking.controller;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.arup_hotdesking.R;


public class changePwdFragment extends Fragment {

    private EditText oldPwd;
    private EditText newPwd;
    private EditText newPwdConfirm;
    private Button changebtn;
    MainActivity mainActivity;

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

       // oldPwd.addTextChangedListener(new PwdTextWatcher(oldPwd));
        newPwd.addTextChangedListener(new PwdTextWatcher(newPwd));
        newPwdConfirm.addTextChangedListener(new PwdTextWatcher(newPwdConfirm));

        changebtn.setOnClickListener(new changePwdBtnListener());

    }


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

    private class changePwdBtnListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {

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

}