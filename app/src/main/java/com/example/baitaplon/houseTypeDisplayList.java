package com.example.baitaplon;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baitaplon.Apdapter.BlogAdapter;
import com.example.baitaplon.Domain.Blog;
import com.example.baitaplon.databinding.ActivityHouseTypeDisplayListBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class houseTypeDisplayList extends AppCompatActivity {

    private ActivityHouseTypeDisplayListBinding binding;
    private FirebaseDatabase database;
    private String houseTypeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate layout
        binding = ActivityHouseTypeDisplayListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Firebase
        database = FirebaseDatabase.getInstance();

        // Get houseTypeName from Intent
        if (getIntent() != null && getIntent().hasExtra("HouseType")) {
            houseTypeName = getIntent().getStringExtra("HouseType");
        } else {
            houseTypeName = ""; // Default value or error handling
            finish(); // Exit activity if no HouseType is provided
        }
        binding.ListBlog.setText("Danh sách bài đăng " + houseTypeName );
        // Initialize blogs
        initBlogs();
    }

    private void initBlogs() {
        // Reference to Firebase Database
        DatabaseReference myRef = database.getReference("Blogs");
        binding.progressBar.setVisibility(View.VISIBLE);

        // Initialize list
        ArrayList<Blog> list = new ArrayList<>();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear(); // Clear the list before adding new items
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        Blog blog = issue.getValue(Blog.class);
                        if (blog != null) {
                            blog.setPostID(issue.getKey()); // Set ID for blog
                            // Add blog to list if houseType matches
                            if (houseTypeName.equals(blog.getHouseType())) {
                                list.add(blog);
                            }
                        }
                    }
                }

                // Update RecyclerView with the filtered list
                setupRecyclerView(list);

                // Hide progress bar
                binding.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void setupRecyclerView(ArrayList<Blog> list) {
        // Setup RecyclerView
        binding.recyclerHouseTypesList.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        );

        // Set adapter
        BlogAdapter adapter = new BlogAdapter(list);
        binding.recyclerHouseTypesList.setAdapter(adapter);
    }
}
