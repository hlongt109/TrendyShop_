package com.trendyshopteam.trendyshop.presenter;

import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.trendyshopteam.trendyshop.interfaces.LoginInterface;
import com.trendyshopteam.trendyshop.utilities.SharePreferencesManage;


public class LoginPresenter {
    private LoginInterface loginInterface;
    private SharePreferencesManage sharePreferencesManage;

    public LoginPresenter(LoginInterface loginInterface) {
        this.loginInterface = loginInterface;
    }

    public void login(String email, String password) {
        if (validateLogin(email, password)) {
            handleLogin(email, password);
        }
    }
    public void checkLogged(){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if(user != null){
            new Handler().postDelayed(() -> {
                loginInterface.loginSuccess();
            },2000);
        }
    }
    private void handleLogin(String email, String password) {
        loading(true);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String id = auth.getCurrentUser().getUid();
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User").child(id);
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            String role = snapshot.child("role").getValue(String.class);
                            sharePreferencesManage = new SharePreferencesManage(loginInterface.getContext());
                            sharePreferencesManage.setPosition(id,role);
                            loginInterface.loginSuccess();
                            FirebaseUser user = auth.getCurrentUser();
                            loading(false);
                        }else {
                            loginInterface.loginFailure();
                            loading(false);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        loginInterface.loginFailure();
                        loading(false);
                    }
                });
            }
        }).addOnFailureListener(e -> {
            loginInterface.loginFailure();
            Log.e("Error login ", "Error :"+e);
            loading(false);
        });
    }

    private Boolean validateLogin(String email, String password) {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            loginInterface.emailError();
            return false;
        } else {
            loginInterface.clearError();
        }
        if (TextUtils.isEmpty(password)) {
            loginInterface.passwordError();
            return false;
        } else {
            loginInterface.clearError();
        }
        return true;
    }

    private void loading(boolean isLoading) {
        if (isLoading) {
            loginInterface.setLoading();
        } else {
            loginInterface.stopLoading();
        }
    }
    public void switchScreen(){
        sharePreferencesManage = new SharePreferencesManage(loginInterface.getContext());
        if(sharePreferencesManage.getPosition().equals("Admin") || sharePreferencesManage.getPosition().equals("NhanVien")){
            loginInterface.switchAdminScreen();
        }
        if(sharePreferencesManage.getPosition().equals("User")){
            loginInterface.switchUserScreen();
        }
    }
}
