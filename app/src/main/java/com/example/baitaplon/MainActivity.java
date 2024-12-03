package com.example.baitaplon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baitaplon.Apdapter.BlogAdapter;
import com.example.baitaplon.Apdapter.HouseTypesAdapter;
import com.example.baitaplon.Domain.Blog;
import com.example.baitaplon.Domain.houseTypes;
import com.example.baitaplon.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Queue;

public class MainActivity extends AppCompatActivity {
private ActivityMainBinding binding;
    FirebaseDatabase database;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        initBlogs();
        setVariable();
        initHouseTypes();
    }
    private void setVariable() {

        binding.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, Login.class));
            }
        });

        binding.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = binding.edtSearch.getText().toString();
                if(!text.isEmpty()){
                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                    intent.putExtra("text", text);
                    intent.putExtra("isSearch",true);
                    startActivity(intent);
                }
            }
        });
        binding.upBlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, UploadBlog.class);
                startActivity(intent);
            }
        });
        binding.btSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Search.class);
                startActivity(intent);
            }
        });

        binding.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = binding.edtSearch.getText().toString().trim();
                if (!text.isEmpty()) {
                    Intent intent = new Intent(MainActivity.this, Search.class);
                    intent.putExtra("searchText", text);
                    startActivity(intent);
                } else {
                    binding.edtSearch.setError("Vui lòng nhập từ khóa tìm kiếm!");
                }
            }
        });
        binding.btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
        binding.cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FavoriteList.class);
                startActivity(intent);
            }
        });

      /*  binding.iconTrangchu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Thi_BTL.class);
                startActivity(intent);
            }*/
       /* });*/
    }
    private void initHouseTypes(){
        DatabaseReference myRef = database.getReference("houseType");
        binding.progressBarhouseTypes.setVisibility(View.VISIBLE);
        ArrayList<houseTypes> list = new ArrayList<>();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    list.clear();
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        houseTypes hT = issue.getValue(houseTypes.class);
                        if (hT != null) {
                            hT.setHouseTypeId(issue.getKey()); // Set ID cho blog
                            list.add(hT);
                        }
                    }
                    if (!list.isEmpty()) {
                        binding.recyclerHouseTypes.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
                        RecyclerView.Adapter adapter = new HouseTypesAdapter(list);
                        binding.recyclerHouseTypes.setAdapter(adapter);
                    }
                    binding.progressBarhouseTypes.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void initBlogs(){
        DatabaseReference myRef = database.getReference("Blogs");
        binding.progressBarBlogs.setVisibility(View.VISIBLE);
        ArrayList<Blog> list = new ArrayList<>();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    list.clear();
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        Blog blog = issue.getValue(Blog.class);
                        if (blog != null) {
                            blog.setPostID(issue.getKey()); // Set ID cho blog
                            list.add(blog);
                        }
                    }
                    if (!list.isEmpty()) {
                        binding.recyclerBlogs.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false));
                        RecyclerView.Adapter adapter = new BlogAdapter(list);
                        binding.recyclerBlogs.setAdapter(adapter);
                    }
                    binding.progressBarBlogs.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}