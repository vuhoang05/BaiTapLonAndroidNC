package com.example.baitaplon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {
    private TextView helloUser, btnAdmin;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private String userId;
    private ImageView iconAdminn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        helloUser = findViewById(R.id.HelloUser);
        btnAdmin = findViewById(R.id.btnAdmin);
        iconAdminn = findViewById(R.id.icAdmin);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Profiles");

        // Lấy thông tin người dùng hiện tại
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            userId = currentUser.getUid();
            // Kiểm tra xem người dùng đã có thông tin cá nhân hay chưa
            checkUserProfile();
        } else {
            Toast.makeText(this, "User not logged in!", Toast.LENGTH_SHORT).show();
            finish(); // Nếu chưa đăng nhập, quay lại màn hình login
        }

        // Khi người dùng bấm vào "Chi tiết thông tin cá nhân"
        findViewById(R.id.btnProfileDetail).setOnClickListener(view -> {
            Intent intent = new Intent(ProfileActivity.this, ProfileDetailActivity.class);
            startActivity(intent);
        });

        btnAdmin.setOnClickListener(view -> {
            Intent intent = new Intent(ProfileActivity.this, ManageBlogs_Admin.class);
            startActivity(intent);
        });

        findViewById(R.id.btnLogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(ProfileActivity.this, Login.class));
            }
        });
        findViewById(R.id.btnBack).setOnClickListener(view -> {
            Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.btnBlogsOfUser).setOnClickListener(view -> {
            Intent intent = new Intent(ProfileActivity.this, BlogOfUserList.class);
            startActivity(intent);
        });

        findViewById(R.id.btnBookViewingRoom).setOnClickListener(view -> {
            Intent intent = new Intent(ProfileActivity.this, BookingListActivity.class);
            startActivity(intent);
        });



    }

    private void checkUserProfile() {
        // Lấy thông tin profile từ Firebase
        databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Nếu có thông tin cá nhân, hiển thị tên người dùng
                    String name = snapshot.child("name").getValue(String.class);
                    if (name != null) {
                        helloUser.setText("Xin chào, " + name);
                    }

                    // Kiểm tra nếu người dùng là admin
                    Boolean isAdmin = snapshot.child("admin").getValue(Boolean.class);
                    if (isAdmin != null && isAdmin) {
                        btnAdmin.setVisibility(View.VISIBLE);
                        iconAdminn.setVisibility(View.VISIBLE);// Hiển thị phần Admin quản lý
                    } else {
                        btnAdmin.setVisibility(View.GONE);
                        iconAdminn.setVisibility(View.GONE);// Ẩn phần Admin quản lý
                    }
                } else {
                    // Nếu chưa có thông tin cá nhân, yêu cầu thêm thông tin
                    helloUser.setText("Xin chào, vui lòng thêm thông tin cá nhân");
                    btnAdmin.setVisibility(View.GONE);
                    iconAdminn.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(ProfileActivity.this, "Failed to load profile", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
