package com.example.baitaplon;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baitaplon.Apdapter.ManageBlogAdapter;
import com.example.baitaplon.Domain.Blog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class BlogOfUserList extends AppCompatActivity {

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
        setContentView(R.layout.activity_blog_of_user_list);

        recyclerView = findViewById(R.id.recyclerBlogOfUserList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        btnBack = findViewById(R.id.btnBack);
        // Lấy thông tin người dùng hiện tại
        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("Blogs");

        // Danh sách các bài blog
        blogList = new ArrayList<>();

        // Khởi tạo Adapter
        blogAdapter = new ManageBlogAdapter(blogList);
        recyclerView.setAdapter(blogAdapter);

        btnBack.setOnClickListener(view -> {
            Intent intent = new Intent(BlogOfUserList.this, ProfileActivity.class);
            startActivity(intent);
        });
        // Lấy các bài blog của người dùng từ Firebase

    }
    @Override
    protected void onResume() {
        super.onResume();
        // Xóa danh sách cũ và tải lại dữ liệu khi quay lại màn hình
        blogList.clear();
        loadUserBlogs();
    }
    private void loadUserBlogs() {
        // Lấy danh sách bài blog của người dùng từ Firebase
        databaseReference.orderByChild("userID").equalTo(userId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                // Lặp qua các bài blog từ Firebase
                for (DataSnapshot snapshot : task.getResult().getChildren()) {
                    Blog blog = snapshot.getValue(Blog.class);
                    if (blog != null) {
                        blogList.add(blog); // Thêm bài blog vào danh sách
                    }
                }
                blogAdapter.notifyDataSetChanged(); // Cập nhật adapter
            } else {
                Toast.makeText(BlogOfUserList.this, "Failed to load blogs", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
