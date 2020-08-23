package com.example.arup_hotdesking.model;

import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.arup_hotdesking.R;

public class LoginRegisterViewModel extends ViewModel {
    private MutableLiveData<RegisterFormState> registerFormState = new MutableLiveData<>();
    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();

    public MutableLiveData<RegisterFormState> getRegisterFormState() {
        return registerFormState;
    }
    public LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    public void registerDataChanged(String email, String password, String confirmPassword) {
        if (!isEmailValid(email)) {
            registerFormState.setValue(new RegisterFormState(R.string.invalid_email, null,null));
        } else if (!isPasswordValid(password)) {
            registerFormState.setValue(new RegisterFormState(null, R.string.invalid_password,null));
        }
        else if(!isPasswordSame(password,confirmPassword)){
            registerFormState.setValue(new RegisterFormState(null, null,R.string.inconsistent_password));
        }else {
            registerFormState.setValue(new RegisterFormState(true));
        }
    }

    public void loginDataChanged(String email, String password) {
        if (!isEmailValid(email)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_email, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    private boolean isEmailValid(String email) {
        if (email == null) {
            return false;
        }
        if(email.trim().isEmpty()){
            return false;
        }
        else {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
    }

    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }

    private boolean isPasswordSame(String password,String confirmPassword){
        return password.equals(confirmPassword);
    }
}
