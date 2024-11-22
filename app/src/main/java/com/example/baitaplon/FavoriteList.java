package com.example.baitaplon;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.baitaplon.Apdapter.FavoriteAdapter;
import com.example.baitaplon.Domain.Favorite;
import com.example.baitaplon.databinding.ActivityFavoriteListBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FavoriteList extends AppCompatActivity {

    private ActivityFavoriteListBinding binding;
    private FirebaseDatabase database;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate layout
        binding = ActivityFavoriteListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Firebase
        database = FirebaseDatabase.getInstance();

        // Get userId from Firebase Authentication
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        initFavorites();
    }

    private void initFavorites() {
        DatabaseReference ref = database.getReference("Favorite");

        ref.orderByChild("userID").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Favorite> favoriteList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Favorite favorite = dataSnapshot.getValue(Favorite.class);
                    if (favorite != null) {
                        favoriteList.add(favorite);
                    }
                }
                // Set up RecyclerView with Adapter
                FavoriteAdapter adapter = new FavoriteAdapter(favoriteList);
                binding.recyclerFavoriteList.setLayoutManager(new LinearLayoutManager(FavoriteList.this));
                binding.recyclerFavoriteList.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }
}
