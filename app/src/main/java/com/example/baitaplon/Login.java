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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    EditText editEmail, editPassword;
    TextView registerNow;
    Button btnLogin;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        // Initialize views
        editEmail = findViewById(R.id.emailLogin);
        editPassword = findViewById(R.id.passwordLogin);
        btnLogin = findViewById(R.id.btnLogin);
        registerNow = findViewById(R.id.regiterNow);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();

        // Set up button click listener
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editEmail.getText().toString();
                String password = editPassword.getText().toString();

                // Validate input
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(Login.this, "Vui lòng nhập email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(Login.this, "Vui lòng nhập mật khẩu", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Sign in user with Firebase Auth
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                if (user != null) {
                                    Log.d("Firebase", "User logged in: " + user.getUid());
                                    Toast.makeText(Login.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();

                                    // You can optionally navigate to a different activity after successful login
                                    Intent intent = new Intent(Login.this, MainActivity.class);
                                    startActivity(intent);
                                } else {
                                    Log.e("Firebase", "User is null after login");
                                    Toast.makeText(Login.this, "Đã xảy ra lỗi!", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                // Handle login failure
                                Log.e("Firebase", "Login failed", task.getException());
                                Toast.makeText(Login.this, "Đăng nhập thất bại: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        // Chuyển đến màn hình đăng ký
        registerNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });
    }
}