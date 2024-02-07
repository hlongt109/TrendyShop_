package com.trendyshopteam.trendyshop.database;

import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

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

    public  interface checkUsernameExits{
        void onCheckUsername(boolean isExists);
    }
    public interface OnLoadedUser{
        void onLoadedUser(ArrayList<User> listUser);
        void onError(String errorr);
    }
    public void CreateNewUser(String id, String username, String password, String email, String fullname, String role, Uri photo, DatabaseReference databaseReference) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference("UserPhoto").child(id + ".jpg");
        UploadTask uploadTask = storageReference.putFile(photo);
        uploadTask.addOnSuccessListener(taskSnapshot -> {
            storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                String imageUrl = uri.toString();
                User user = new User(id, username, password, email, fullname, "", role, imageUrl, "");
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
    public boolean checkUserNameExist(String username, checkUsernameExits callBack) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("User");
        Query query = reference.orderByChild("username").equalTo(username);
        ((Query) query).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    callBack.onCheckUsername(true);
                } else {
                    callBack.onCheckUsername(false);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("UserDAO", "Error querying username existence: " + databaseError.getMessage());
                callBack.onCheckUsername(false);
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
