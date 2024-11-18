package com.example.baitaplon;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.baitaplon.Apdapter.ManageBlogAdapter;
import com.example.baitaplon.Domain.Blog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ManageBlogs_Admin extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ManageBlogAdapter blogAdapter;
    private ArrayList<Blog> blogList;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private String userId;
    private ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_manage_blogs_admin);

        recyclerView = findViewById(R.id.recyclerBlogList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btnBack = findViewById(R.id.btnBack);
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Profiles");

        // Check if the user is an admin
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            userId = currentUser.getUid();
            checkIfAdmin();
        } else {
            Toast.makeText(this, "Nguoi dung chua dang nhap!", Toast.LENGTH_SHORT).show();
            finish();  // Exit activity if the user is not logged in
        }
        btnBack.setOnClickListener(view -> {
            Intent intent = new Intent(ManageBlogs_Admin.this, ProfileActivity.class);
            startActivity(intent);
        });
    }

    private void checkIfAdmin() {
        // Check if the user is an admin by fetching their profile from Firebase
        databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Boolean isAdmin = snapshot.child("admin").getValue(Boolean.class);
                    if (isAdmin != null && isAdmin) {
                        // If the user is an admin, load all blog posts
                        loadAllBlogs();
                    } else {
                        Toast.makeText(ManageBlogs_Admin.this, "Ban khong phai la admin", Toast.LENGTH_SHORT).show();
                        finish();  // If the user is not an admin, exit activity
                    }
                } else {
                    Toast.makeText(ManageBlogs_Admin.this, "Failed to fetch user data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(ManageBlogs_Admin.this, "Failed to check admin status", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadAllBlogs() {
        // Fetch all blog posts from Firebase
        DatabaseReference blogReference = FirebaseDatabase.getInstance().getReference("Blogs");
        blogReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                blogList = new ArrayList<>();
                for (DataSnapshot blogSnapshot : snapshot.getChildren()) {
                    Blog blog = blogSnapshot.getValue(Blog.class);
                    if (blog != null) {
                        blogList.add(blog);
                    }
                }
                blogAdapter = new ManageBlogAdapter(blogList);
                recyclerView.setAdapter(blogAdapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(ManageBlogs_Admin.this, "Failed to load blogs", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
