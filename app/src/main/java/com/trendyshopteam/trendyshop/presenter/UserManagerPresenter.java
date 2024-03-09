package com.trendyshopteam.trendyshop.presenter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.trendyshopteam.trendyshop.R;
import com.trendyshopteam.trendyshop.adapter.UserAdapter;
import com.trendyshopteam.trendyshop.database.UserDAO;
import com.trendyshopteam.trendyshop.databinding.LayoutUpdateUserBinding;
import com.trendyshopteam.trendyshop.interfaces.UserManageInterface;
import com.trendyshopteam.trendyshop.model.User;
import com.trendyshopteam.trendyshop.view.activities.CreateUserActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class UserManagerPresenter {
    private UserManageInterface userManageInterface;
    private UserDAO userDAO;
    private ArrayList<User> list = new ArrayList<>();
    private UserAdapter userAdapter;
    private DatabaseReference databaseReference;
    private Uri ImageUri = null;
    private LayoutUpdateUserBinding layoutUpdateUserBinding;

    public UserManagerPresenter(UserManageInterface userManageInterface) {
        this.userManageInterface = userManageInterface;
    }

    public void loadDataOnView(Context context, RecyclerView rcv, UserManagerPresenter presenter) {
        userManageInterface.showLoading();
        databaseReference = FirebaseDatabase.getInstance().getReference("User");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user != null && user.getRole().equals("Admin") || user.getRole().equals("NhanVien")) {
                        list.add(user);
                    }
                }
                userAdapter.notifyDataSetChanged();
                userManageInterface.hideLoading();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                userManageInterface.hideLoading();
            }
        });
        userAdapter = new UserAdapter(context, list, presenter);
        rcv.setLayoutManager(new LinearLayoutManager(context));
        rcv.setAdapter(userAdapter);
        userAdapter.showMenu((user, view) -> {
            showMenuUtities(user, view);
        });

    }

    public void addNewUser() {
        userManageInterface.getContext().startActivity(new Intent(userManageInterface.getContext(), CreateUserActivity.class));
    }

    private void showMenuUtities(User user, View view) {
        PopupMenu popupMenu = new PopupMenu(userManageInterface.getContext(), view);
        popupMenu.inflate(R.menu.option_menu);
        popupMenu.setGravity(Gravity.START);
        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.navUpdate) {
                openUpdate(user);
                return true;
            } else if (item.getItemId() == R.id.navDelete) {
                openDelete(user);
                return true;
            } else {
                return false;
            }
        });

        popupMenu.show();

    }

    private void openUpdate(User user) {
        AlertDialog.Builder builder = new AlertDialog.Builder(userManageInterface.getContext());
        LayoutInflater layoutInflater = ((Activity) userManageInterface.getContext()).getLayoutInflater();
        LayoutUpdateUserBinding binding = LayoutUpdateUserBinding.inflate(layoutInflater);
        layoutUpdateUserBinding = binding;
        View view = binding.getRoot();
        builder.setView(view);
        builder.setCancelable(false);
        Dialog dialog = builder.create();
        dialog.show();
        // set data on view
        Glide.with(userManageInterface.getContext()).load(user.getPhoto()).error(R.drawable.image).into(binding.imgAvt);
        binding.tvEmail.setText(String.valueOf(user.getEmail()));
        binding.edPw.setText(String.valueOf(user.getPassword()));
        binding.edFullName.setText(String.valueOf(user.getFullname()));
        String imgUrl = user.getPhoto();
        if (user.getRole().equals("Admin")) {
            binding.rdoAdmin.setChecked(true);
        } else if (user.getRole().equals("NhanVien")) {
            binding.rdoNhanVien.setChecked(true);
        }
        // handle
        binding.btnBack.setOnClickListener(v -> {
            dialog.dismiss();
        });
        binding.imgAvt.setOnClickListener(v -> {
            ImagePicker.with(userManageInterface.getActivity())
                    .crop()
                    .compress(1024)
                    .maxResultSize(1080, 1080)
                    .start();
        });
        binding.btnUpdate.setOnClickListener(v -> {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("User");
            String password = String.valueOf(binding.edPw.getText());
            String name = String.valueOf(binding.edFullName.getText());
            String role;
            if (binding.rdoNhanVien.isChecked()) {
                role = "NhanVien";
            } else if (binding.rdoAdmin.isChecked()) {
                role = "Admin";
            } else {
                role = "";
            }

            if (validate(binding, password, name, role)) {
                Query query = reference.orderByChild("id").equalTo(user.getId());
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                String userId = dataSnapshot.getKey();
                                DatabaseReference userRef = reference.child(userId);
                                Map<String, Object> updates = new HashMap<>();
                                updates.put("fullname", name);
                                updates.put("password", password);
                                updates.put("role", role);
                                if (ImageUri != null) {
                                    updates.put("photo", ImageUri.toString());
                                }
                                updatePasswordInAuth(user, password);
                                userRef.updateChildren(updates)
                                        .addOnSuccessListener(aVoid -> {
                                            // Xử lý khi cập nhật thành công
                                            Toast.makeText(userManageInterface.getContext(), "User information updated successfully", Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                        })
                                        .addOnFailureListener(e -> {
                                            // Xử lý khi cập nhật thất bại
                                            Toast.makeText(userManageInterface.getContext(), "Failed to update user information", Toast.LENGTH_SHORT).show();
                                        });
                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("ErrorUpdate", "Error :" + error);
                        Toast.makeText(userManageInterface.getContext(), "User not found or error occurred", Toast.LENGTH_SHORT).show();
                    }
                });
            }

        });
    }

    private Boolean validate(LayoutUpdateUserBinding binding, String password, String name, String role) {

        if (TextUtils.isEmpty(password)) {
            binding.tilPw.setError("Enter the password");
            return false;
        } else {
            binding.tilPw.setError(null);
        }
        if (TextUtils.isEmpty(name)) {
            binding.tilFullname.setError("Enter the name");
            return false;
        } else {
            binding.tilFullname.setError(null);
        }
        if (TextUtils.isEmpty(role)) {
            Toast.makeText(userManageInterface.getContext(), "Role not selected", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void handleImagePickerResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            ImageUri = data.getData();
            if (layoutUpdateUserBinding.imgAvt != null) {
                layoutUpdateUserBinding.imgAvt.setImageURI(ImageUri);
            }
        }
    }

    private void updatePasswordInAuth(User user, String newPassword) {
        String userId = user.getId();
        FirebaseAuth.getInstance().signInWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnSuccessListener(authResult -> {
                    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    if (firebaseUser != null) {
                        firebaseUser.updatePassword(newPassword)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(userManageInterface.getContext(), "Password updated successfully", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(userManageInterface.getContext(), "Failed to update password in authentication", Toast.LENGTH_SHORT).show();
                                });
                    } else {
                        Toast.makeText(userManageInterface.getContext(), "Failed to update password: User is not logged in", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(userManageInterface.getContext(), "Failed to update password: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void openDelete(User user) {
        AlertDialog.Builder builder = new AlertDialog.Builder(userManageInterface.getContext());
        builder.setTitle("Message !");
        builder.setCancelable(false);
        builder.setMessage("Do you want to delete this user ?");
        builder.setNegativeButton("No", null);
        builder.setPositiveButton("Yes", (dialogInterface, i) -> {
            String idUser = user.getId();
            FirebaseAuth.getInstance().signInWithEmailAndPassword(user.getEmail(), user.getPassword())
                    .addOnSuccessListener(authResult -> {
                        FirebaseUser userCurrent = authResult.getUser();
                        userCurrent.delete().addOnSuccessListener(unused -> {
                            deleteUser(idUser);
                        }).addOnFailureListener(e -> {
                            Toast.makeText(userManageInterface.getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(userManageInterface.getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });
        builder.create().show();
    }

    private void deleteUser(String id) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User");
        databaseReference.child(id).removeValue()
                .addOnSuccessListener(unused -> {
                    Toast.makeText(userManageInterface.getContext(), "User deleted successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(userManageInterface.getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
