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
import com.example.baitaplon.Domain.Blog;
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

    }
    private void setVariable() {
       /* //set ten nguoi dung
        DatabaseReference dataRef = database.getReference("Users").child(mAuth.getCurrentUser().getUid()).child("email");
        dataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String nameCurrentUser = snapshot.getValue(String.class);
                binding.nameCurrentUser.setText(nameCurrentUser+"");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/

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

      /*  binding.iconTrangchu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Thi_BTL.class);
                startActivity(intent);
            }*/
       /* });*/
    }
    private void initBlogs(){
        DatabaseReference myRef = database.getReference("Blogs");
        binding.progressBarBlogs.setVisibility(View.VISIBLE);
        ArrayList<Blog> list = new ArrayList<>();
//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.exists()){
//                    for(DataSnapshot issue : snapshot.getChildren()){
//                        list.add(issue.getValue(Blog.class));
//                    }
//                    if(list.size()>0){
//                        binding.recyclerBlogs.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false));
//                        RecyclerView.Adapter adapter = new BlogAdapter(list);
//                        binding.recyclerBlogs.setAdapter(adapter);
//                    }
//                    binding.progressBarBlogs.setVisibility(View.GONE);
//                }
//            }
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