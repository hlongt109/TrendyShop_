package com.trendyshopteam.trendyshop.presenter;

import android.text.TextUtils;
import android.util.Patterns;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.trendyshopteam.trendyshop.interfaces.SignupInterface;
import com.trendyshopteam.trendyshop.model.User;

import java.util.regex.Pattern;

public class SignupPresenter {
    private SignupInterface signupInterface;
    private FirebaseAuth auth;

    public SignupPresenter(SignupInterface signupInterface) {
        this.signupInterface = signupInterface;
        this.auth = FirebaseAuth.getInstance();
    }
    public void signUp(String email, String password, String rePassword,String name) {
        if(validateSignup(email, name, password,rePassword)){
            checkBeforeSigUp(email, password, name);
        }
    }
    private void checkBeforeSigUp(String email, String password, String name){
        loading(true);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("User");
        Query query = reference.orderByChild("email").equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    signupInterface.emailExist();
                    loading(false);
                }else {
                    handleSignUp(email,password,name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                 signupInterface.signUpFailed();
                 loading(false);
            }
        });
    }
    private void handleSignUp(String email, String password,String name){
        loading(true);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User");
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
           if(task.isSuccessful()){
               String id = auth.getCurrentUser().getUid();
               User user = new User(id,email,password,name,"","User","","");
               reference.child(id).setValue(user).addOnCompleteListener(task1 -> {
                  signupInterface.signUpSucess();
                  loading(false);
               }).addOnFailureListener(e -> {
                   signupInterface.signUpFailed();
                   loading(false);
               });
           }
        });
    }
    private Boolean validateSignup(String email, String name, String password, String rePassword) {
        String regexPass = "^(?=.*[A-Z]).{6,}$";
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            signupInterface.emailError();
            return false;
        }else {
            signupInterface.clearErr();
        }
        if(TextUtils.isEmpty(name)){
            signupInterface.nameError();
            return false;
        }else {
            signupInterface.clearErr();
        }
        if(!Pattern.matches(regexPass,password)){
            signupInterface.passError();
            return false;
        }else {
            signupInterface.clearErr();
        }
        if(!password.equals(rePassword)){
            signupInterface.passNotMatch();
            return false;
        }else {
            signupInterface.clearErr();
        }
        return true;
    }
    private void loading(boolean isLoading){
        if(isLoading){
            signupInterface.setLoading();
        }else {
            signupInterface.stopLoading();
        }
    }
}
