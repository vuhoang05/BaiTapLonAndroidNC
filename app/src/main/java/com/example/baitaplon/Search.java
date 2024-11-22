package com.example.baitaplon;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.baitaplon.Apdapter.BlogAdapter;
import com.example.baitaplon.Domain.Blog;
import com.example.baitaplon.Domain.houseTypes;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class Search extends AppCompatActivity {
    private Spinner houseTypeSpinner;
    private EditText priceMaxEditText, areaMinEditText, roomMinEditText, addressEditText;
    private Button searchButton;
    private RecyclerView searchResultsRecyclerView;
    private BlogAdapter searchResultsAdapter;
    private ArrayList<Blog> searchResultsList;
    private DatabaseReference databaseReference, houseTypeReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        houseTypeSpinner = findViewById(R.id.houseTypeSpinner);
        priceMaxEditText = findViewById(R.id.priceMaxEditText);
        areaMinEditText = findViewById(R.id.areaMinEditText);
        roomMinEditText = findViewById(R.id.roomMinEditText);
        addressEditText = findViewById(R.id.addressEditText);  // New address input
        searchButton = findViewById(R.id.searchButton);
        searchResultsRecyclerView = findViewById(R.id.searchResultsRecyclerView);
        searchResultsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        searchResultsList = new ArrayList<>();
        searchResultsAdapter = new BlogAdapter(searchResultsList);
        searchResultsRecyclerView.setAdapter(searchResultsAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("Blogs");
        houseTypeReference = FirebaseDatabase.getInstance().getReference("houseType");

        String keyword = getIntent().getStringExtra("searchText");

        if (keyword != null && !keyword.isEmpty()) {
            performSearch(keyword); // Gọi tìm kiếm với từ khóa
            setupHouseTypeSpinner();
        }else{
            setupHouseTypeSpinner();
            loadAllBlogs();
        }

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performSearch();
            }
        });
    }
    private void performSearch(String keyword) {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                searchResultsList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Blog blog = dataSnapshot.getValue(Blog.class);
                    if (blog != null && blog.getTitle().toLowerCase().contains(keyword.toLowerCase())) {
                        searchResultsList.add(blog);
                    }
                }

                // Kiểm tra kết quả
                if (searchResultsList.isEmpty()) {
                    Toast.makeText(Search.this, "Không tìm thấy kết quả phù hợp", Toast.LENGTH_SHORT).show();
                }
                searchResultsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(Search.this, "Lỗi khi tải dữ liệu từ Firebase", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadAllBlogs() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                searchResultsList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Blog blog = dataSnapshot.getValue(Blog.class);
                    if (blog != null) {
                        searchResultsList.add(blog);  // Add all blogs to the list
                    }
                }
                searchResultsAdapter.notifyDataSetChanged();  // Update the RecyclerView
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(Search.this, "Lỗi khi tải dữ liệu từ Firebase", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupHouseTypeSpinner() {
        ArrayList<String> houseTypeList = new ArrayList<>();
        houseTypeList.add("None");  // Add "None" as the default option

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, houseTypeList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        houseTypeSpinner.setAdapter(adapter);

        houseTypeReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                houseTypeList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    houseTypes houseTypeObj = dataSnapshot.getValue(houseTypes.class);
                    if (houseTypeObj != null) {
                        houseTypeList.add(houseTypeObj.getHouseTypeName());
                    }
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(Search.this, "Lỗi khi tải loại nhà từ Firebase", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void performSearch() {
        String selectedHouseType = houseTypeSpinner.getSelectedItem().toString();
        String priceMax = priceMaxEditText.getText().toString();
        String areaMin = areaMinEditText.getText().toString();
        String roomMin = roomMinEditText.getText().toString();
        String address = addressEditText.getText().toString();  // Fetch address input

        if (!priceMax.isEmpty() && !isNumeric(priceMax)) {
            Toast.makeText(Search.this, "Giá tối đa phải là số nguyên hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!areaMin.isEmpty() && !isNumeric(areaMin)) {
            Toast.makeText(Search.this, "Diện tích tối thiểu phải là số nguyên hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!roomMin.isEmpty() && !isNumeric(roomMin)) {
            Toast.makeText(Search.this, "Số phòng tối thiểu phải là số nguyên hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                searchResultsList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Blog blog = dataSnapshot.getValue(Blog.class);
                    if (blog != null) {
                        boolean matches = true;

                        if (!selectedHouseType.isEmpty() && !blog.getHouseType().equals(selectedHouseType)) {
                            matches = false;
                        }
                        if (!priceMax.isEmpty() && Integer.parseInt(blog.getPrice()) > Integer.parseInt(priceMax)) {
                            matches = false;
                        }
                        if (!areaMin.isEmpty() && Integer.parseInt(blog.getArea()) < Integer.parseInt(areaMin)) {
                            matches = false;
                        }
                        if (!roomMin.isEmpty() && Integer.parseInt(blog.getNumberOfRooms()) < Integer.parseInt(roomMin)) {
                            matches = false;
                        }
                        if (!address.isEmpty() && !blog.getAddress().toLowerCase().contains(address.toLowerCase())) {
                            matches = false;
                        }

                        if (matches) {
                            searchResultsList.add(blog);
                        }
                    }
                }
                searchResultsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(Search.this, "Lỗi khi tải dữ liệu từ Firebase", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
