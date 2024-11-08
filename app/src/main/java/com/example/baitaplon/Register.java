package com.example.baitaplon;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.baitaplon.Domain.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {

    EditText editEmail, editPassword;
    TextView loginNow;
    Button btnRegister;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        // Initialize views
        editEmail = findViewById(R.id.emailRegister);
        editPassword = findViewById(R.id.passwordRegister);
        btnRegister = findViewById(R.id.btnRegister);
        loginNow = findViewById(R.id.loginNow);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        // Set up button click listener
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editEmail.getText().toString();
                String password = editPassword.getText().toString();

                // Validate input
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(Register.this, "Vui lòng nhập email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(Register.this, "Vui lòng nhập mật khẩu", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Create user with Firebase Auth
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                if (user != null) {
                                    String userId = user.getUid();

                                    // Create a User object and save to Realtime Database
                                    User newUser = new User(userId, email, password);
                                    mDatabase.child(userId).setValue(newUser)
                                            .addOnCompleteListener(task1 -> {
                                                if (task1.isSuccessful()) {
                                                    Log.d("Firebase", "User saved: " + userId);
                                                    Toast.makeText(Register.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(Register.this, Login.class);
                                                    startActivity(intent);
                                                    finish();
                                                } else {
                                                    Log.e("Firebase", "Failed to save user data", task1.getException());
                                                    Toast.makeText(Register.this, "Đã xảy ra lỗi khi lưu dữ liệu người dùng!", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                } else {
                                    Log.e("Firebase", "User is null after registration");
                                    Toast.makeText(Register.this, "Đã xảy ra lỗi!", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                // Handle registration failure
                                Log.e("Firebase", "Registration failed", task.getException());
                                Toast.makeText(Register.this, "Đăng ký thất bại: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        // Chuyển đến màn hình đăng nhập
        loginNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
            }
        });
    }
}