package com.example.baitaplon;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.example.baitaplon.Domain.Profile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ProfileDetailActivity extends AppCompatActivity {
    private EditText etName, etEmail, etPhone, etAddress;
    private Button btnSave, btnEdit;
    private ImageView btnBack;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profiledetail_activityy);
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Profiles");

        // Lấy thông tin người dùng
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            userId = currentUser.getUid();
        } else {
            Toast.makeText(this, "User not logged in!", Toast.LENGTH_SHORT).show();
            finish(); // Quay về màn hình đăng nhập nếu chưa đăng nhập
            return;
        }etName = findViewById(R.id.et_name);
        etEmail = findViewById(R.id.et_email);
        etPhone = findViewById(R.id.et_phone);
        etAddress = findViewById(R.id.et_address);
        btnSave = findViewById(R.id.btn_save);
        btnEdit = findViewById(R.id.btn_edit);
        btnBack = findViewById(R.id.btnBack);

        // Load dữ liệu từ Firebase
        loadProfile();

        // Lưu thông tin
        btnSave.setOnClickListener(v -> saveProfile());

        // Chỉnh sửa thông tin
        btnEdit.setOnClickListener(v -> enableEditing(true));
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileDetailActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loadProfile() {
        databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Profile profile = snapshot.getValue(Profile.class);
                    if (profile != null) {
                        etName.setText(profile.getName());
                        etEmail.setText(profile.getEmail());
                        etPhone.setText(profile.getPhoneNumber());
                        etAddress.setText(profile.getAddress());
                        enableEditing(false);
                    }
                } else {
                    enableEditing(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileDetailActivity.this, "Failed to load profile", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveProfile() {
        String name = etName.getText().toString();
        String email = etEmail.getText().toString();
        String phone = etPhone.getText().toString();
        String address = etAddress.getText().toString();

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || address.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Profile profile = new Profile(userId, address, phone, email, name, false);
        databaseReference.child(userId).setValue(profile).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(ProfileDetailActivity.this, "Profile saved", Toast.LENGTH_SHORT).show();
                enableEditing(false);
            } else {
                Toast.makeText(ProfileDetailActivity.this, "Failed to save profile", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void enableEditing(boolean enable) {
        etName.setEnabled(enable);
        etEmail.setEnabled(enable);
        etPhone.setEnabled(enable);
        etAddress.setEnabled(enable);
        btnSave.setVisibility(enable ? View.VISIBLE : View.GONE);
        btnEdit.setVisibility(enable ? View.GONE : View.VISIBLE);
    }
}