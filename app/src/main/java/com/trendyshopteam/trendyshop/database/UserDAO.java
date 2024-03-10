package com.trendyshopteam.trendyshop.database;

import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.trendyshopteam.trendyshop.interfaces.CreateUserInterface;
import com.trendyshopteam.trendyshop.interfaces.UserManageInterface;
import com.trendyshopteam.trendyshop.model.User;

import java.util.ArrayList;

public class UserDAO {
    private CreateUserInterface createUserInterface;

    public UserDAO(CreateUserInterface createUserInterface) {
        this.createUserInterface = createUserInterface;
    }

    public UserDAO() {
    }

    public  interface checkEmailExits{
        void onCheckEmail(boolean isExists);
    }
    public interface OnLoadedUser{
        void onLoadedUser(ArrayList<User> listUser);
        void onError(String errorr);
    }
    public void CreateNewUser(String id, String email, String password, String fullname, String role, Uri photo, DatabaseReference databaseReference) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference("UserPhoto").child(fullname + ".jpg");
        UploadTask uploadTask = storageReference.putFile(photo);
        uploadTask.addOnSuccessListener(taskSnapshot -> {
            storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                String imageUrl = uri.toString();
                User user = new User(id, email,"", password, fullname, "", role, imageUrl, "");
                databaseReference.child(id).setValue(user)
                        .addOnSuccessListener(unused -> {
                            createUserInterface.addSuccess();
                        })
                        .addOnFailureListener(e -> {
                            createUserInterface.addFailure();
                        });
            });
        }).addOnFailureListener(e -> {
            createUserInterface.uploadImageFailure();
        });
    }
    public boolean checkEmailExist(String email, checkEmailExits callBack) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("User");
        Query query = reference.orderByChild("email").equalTo(email);
        ((Query) query).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    callBack.onCheckEmail(true);
                } else {
                    callBack.onCheckEmail(false);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("UserDAO", "Error querying email existence: " + databaseError.getMessage());
                callBack.onCheckEmail(false);
            }
        });
        return false;
    }
    public void getAllUser(OnLoadedUser listener) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<User> list = new ArrayList<>();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    User user = dataSnapshot.getValue(User.class);
                    if (user != null){
                        list.add(user);
                    }
                }
                listener.onLoadedUser(list);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onError(error.getMessage());
            }
        });
    }
}
