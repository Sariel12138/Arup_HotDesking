package com.example.arup_hotdesking.data;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.arup_hotdesking.data.model.LoggedInUser;
import com.example.arup_hotdesking.ui.login.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.Executor;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    private FirebaseAuth mAuth;
    private LoginActivity loginActivity ;
    FirebaseAuthInvalidUserException faiue;
    private  FirebaseUser user;

    public Result<LoggedInUser> login(final String username, final String password) {




        try {
            // TODO: handle loggedInUser authentication
            /*LoggedInUser fakeUser =
                    new LoggedInUser(
                            java.util.UUID.randomUUID().toString(),
                            "Jane Doe");*/


            mAuth.signInWithEmailAndPassword(username,password).addOnCompleteListener(loginActivity, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        user = mAuth.getCurrentUser();
                    }
                    else if(Objects.equals(task.getException(), faiue)){
                        mAuth.createUserWithEmailAndPassword(username,password).addOnCompleteListener(loginActivity, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    user = mAuth.getCurrentUser();
                                }
                            }
                        });
                    }
                    else {
                        Toast.makeText(loginActivity.getBaseContext(),"AuthenTication failed.",Toast.LENGTH_LONG).show();
                    }
                }
            });
            LoggedInUser loggedInUser = new LoggedInUser(user.getUid(),user.getEmail());
            return new Result.Success<>(loggedInUser);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}