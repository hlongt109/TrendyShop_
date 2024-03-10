package com.trendyshopteam.trendyshop.view.fragments.FragmentUser;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.trendyshopteam.trendyshop.R;
import com.trendyshopteam.trendyshop.databinding.FragmentChangePasswordFragmentBinding;


public class ChangePasswordFragment extends Fragment {

    FragmentChangePasswordFragmentBinding binding;
    String oldPass, newPass;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_change_password_fragment, container, false);
        binding = FragmentChangePasswordFragmentBinding.bind(view);


        oldPass = binding.edtOldPass.getText().toString();
        newPass = binding.edtNewPass.getText().toString();
        binding.btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatePassWord(oldPass, newPass);
            }
        });
        return view;
    }

    private void updatePassWord(String oldPass, String newPass) {
        String oldPassword = binding.edtOldPass.getText().toString();
        String newPassword = binding.edtNewPass.getText().toString();
        String confirm = binding.edtConfirmpass.getText().toString();

        if (oldPassword.equals("") || newPassword.equals("") || confirm.equals("")) {
            if (oldPassword.equals("")) {
                binding.iplOldpass.setError("Vui lòng nhập mật khẩu cũ");
            }else{
                newPassword.equals("");
            }

            if (newPassword.isEmpty()) {
                binding.iplNewpass.setError("Vui lòng nhập mật khẩu mới");

            }else{
                binding.iplNewpass.setError("");
            }

            if (confirm.equals("")) {
                binding.iplComfirm.setError("Vui lòng nhập xác nhận mật khẩu");

            }else{
                binding.iplComfirm.setError("");
            }


        } else if (!newPassword.equals(confirm)) {
            binding.iplNewpass.setError("Xác thực mật khẩu và mật khẩu phải giống nhau");
            binding.iplComfirm.setError("Xác thực mật khẩu và mật khẩu phải giống nhau");
        } else {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            AuthCredential authCredential = EmailAuthProvider.getCredential(user.getEmail(), oldPassword);

            user.reauthenticate(authCredential).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("User").child(user.getUid());
                                userRef.child("password").setValue(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            //thay đổi mật khẩu thành công
                                            ResetForm();
                                            Toast.makeText(getContext(), "Thay đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getContext(), "Thay đổi mật khẩu không thành công", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(getContext(), "Không thể cập nhật mật khẩu", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(getContext(), "Mật khẩu cũ không đúng", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void ResetForm() {
        binding.edtOldPass.setText(null);
        binding.edtNewPass.setText(null);
        binding.edtConfirmpass.setText(null);
    }
}

