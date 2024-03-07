package com.trendyshopteam.trendyshop.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.trendyshopteam.trendyshop.R;
import com.trendyshopteam.trendyshop.database.UserDAO;
import com.trendyshopteam.trendyshop.databinding.ActivityCreateUserBinding;
import com.trendyshopteam.trendyshop.interfaces.CreateUserInterface;

public class CreateUserPresenter {
    private CreateUserInterface createUserInterface;
    private ActivityCreateUserBinding binding;
    private FirebaseAuth auth;
    private UserDAO userDAO;
    private Uri ImageUri = null;

    public CreateUserPresenter(CreateUserInterface createUserInterface, ActivityCreateUserBinding binding) {
        this.createUserInterface = createUserInterface;
        this.binding = binding;
        this.auth = FirebaseAuth.getInstance();

    }

    public void listener() {
        binding.btnBack.setOnClickListener(v -> createUserInterface.getActivity().onBackPressed());
        binding.imgAvt.setOnClickListener(v -> {
            ImagePicker.with(createUserInterface.getActivity())
                    .crop()
                    .compress(1024)
                    .maxResultSize(1080, 1080)
                    .start();
        });
        binding.btnAdd.setOnClickListener(v -> {
            if (isValidDetails()) {
                userDAO = new UserDAO(createUserInterface);
                userDAO.checkEmailExist(String.valueOf(binding.edEmail.getText()).trim(), isExists -> {
                    if (isExists) {
                        createUserInterface.isEmailExist();
                    } else {
                        createUserInterface.clearEmailErr();
                        addData();
                    }
                });
            }
        });
    }

    private void addData() {
        loading(true);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User");
        String pas = binding.edPw.getText().toString().trim();
        String email = binding.edEmail.getText().toString().trim();
        String name = binding.edFullName.getText().toString().trim();
        String  role;
        if (binding.rdoNhanVien.isChecked()) {
            role = "NhanVien";
        } else if (binding.rdoAdmin.isChecked()) {
            role = "Admin";
        } else {
            role = "";
        }
        auth.createUserWithEmailAndPassword(email,pas).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                String id = auth.getCurrentUser().getUid();
                    userDAO = new UserDAO(createUserInterface);
                    userDAO.CreateNewUser(id, email, pas, name, role, ImageUri, databaseReference);
                    resetFields();
            }
        }).addOnFailureListener(e -> {
            Log.e("Create user error ", "Error :"+e);
            createUserInterface.addFailure();
        });

    }

    public void handleImagePickerResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            ImageUri = data.getData();
            if (binding.imgAvt != null) {
                binding.imgAvt.setImageURI(ImageUri);
            }
        }
    }

    private void loading(Boolean isLoading) {
        if (isLoading) {
            binding.btnAdd.setVisibility(View.INVISIBLE);
            binding.progressBar.setVisibility(View.VISIBLE);
        } else {
            binding.btnAdd.setVisibility(View.VISIBLE);
            binding.progressBar.setVisibility(View.INVISIBLE);
        }
    }

    private Boolean isValidDetails() {
        String pw = binding.edPw.getText().toString().trim();
        String name = binding.edFullName.getText().toString().trim();
        String email = binding.edEmail.getText().toString().trim();
        if ( pw.isEmpty() || name.isEmpty() || email.isEmpty() || binding.rdoGroup.getCheckedRadioButtonId() == -1 || ImageUri == null) {
            if (pw.isEmpty()) {
                createUserInterface.isPassEmpty();
            } else {
                createUserInterface.clearPasswordErr();
            }
            if (name.isEmpty()) {
                createUserInterface.isNameEmpty();
            } else {
                createUserInterface.clearNameErr();
            }
            if (email.isEmpty()) {
                createUserInterface.isEmailEmpty();
            } else {
                createUserInterface.clearEmailErr();
            }
            if (binding.rdoGroup.getCheckedRadioButtonId() == -1) {
                createUserInterface.isRdoNull();
            }
            if (ImageUri == null) {
                createUserInterface.isImgNull();
            }
            return false;
        } else {
            return true;
        }
    }


    private void resetFields() {
        loading(false);
        ImageUri = null;
        binding.edEmail.setText("");
        binding.edPw.setText("");
        binding.edFullName.setText("");
        binding.imgAvt.setImageResource(R.drawable.ic_camera);
    }

}
