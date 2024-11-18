package com.example.baitaplon;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EditBlog extends AppCompatActivity {

    private EditText etTitle, etDescription, etPrice, etArea, etAddress, etNumberOfRooms, etPhoneNumber, etImageUrl;
    private Spinner houseTypeSpinner;
    private Button btnSave, btnCancel;
    private DatabaseReference blogReference, houseTypeReference;
    private String blogID; // Lấy `postId` từ intent hoặc nguồn khác
    private ArrayList<String> houseTypeList;
    private ArrayAdapter<String> adapter;
    private ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_blog);

        // Ánh xạ các view
        etTitle = findViewById(R.id.titleBlog);
        etDescription = findViewById(R.id.description);
        etPrice = findViewById(R.id.price);
        etArea = findViewById(R.id.area);
        etAddress = findViewById(R.id.address);
        etNumberOfRooms = findViewById(R.id.numberOfRoom);
        etPhoneNumber = findViewById(R.id.phoneNumber);
        etImageUrl = findViewById(R.id.urlImg);
        houseTypeSpinner = findViewById(R.id.houseTypeSpinner);
        btnSave = findViewById(R.id.postBlog);
        btnBack = findViewById(R.id.btnBack);
         // Thêm nút hủy (nếu cần)

        // Firebase reference
        blogReference = FirebaseDatabase.getInstance().getReference("Blogs");
        houseTypeReference = FirebaseDatabase.getInstance().getReference("houseType");

        // Lấy postId từ Intent
        blogID = getIntent().getStringExtra("BLOG_ID");

        // Khởi tạo ArrayList và Adapter cho Spinner
        houseTypeList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, houseTypeList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        houseTypeSpinner.setAdapter(adapter);

        // Load dữ liệu houseType
        loadHouseTypes();

        // Load dữ liệu bài đăng
        loadBlogDetails();

        // Xử lý sự kiện lưu
        btnSave.setOnClickListener(v -> saveBlog());
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditBlog.this, BlogOfUserList.class);
                startActivity(intent);
            }
        });



    }

    private void loadHouseTypes() {
        houseTypeReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                houseTypeList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String houseTypeName = dataSnapshot.child("houseTypeName").getValue(String.class);
                    if (!TextUtils.isEmpty(houseTypeName)) {
                        houseTypeList.add(houseTypeName);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EditBlog.this, "Lỗi khi tải loại nhà", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadBlogDetails() {
        blogReference.child(blogID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    etTitle.setText(snapshot.child("title").getValue(String.class));
                    etDescription.setText(snapshot.child("description").getValue(String.class));
                    etPrice.setText(snapshot.child("price").getValue(String.class));
                    etArea.setText(snapshot.child("area").getValue(String.class));
                    etAddress.setText(snapshot.child("address").getValue(String.class));
                    etNumberOfRooms.setText(snapshot.child("numberOfRooms").getValue(String.class));
                    etPhoneNumber.setText(snapshot.child("phoneNumber").getValue(String.class));
                    etImageUrl.setText(snapshot.child("imageUrl").getValue(String.class));

                    String houseType = snapshot.child("houseType").getValue(String.class);
                    if (houseType != null) {
                        int spinnerPosition = adapter.getPosition(houseType);
                        houseTypeSpinner.setSelection(spinnerPosition);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EditBlog.this, "Lỗi khi tải bài đăng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveBlog() {
        String title = etTitle.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String price = etPrice.getText().toString().trim();
        String area = etArea.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String numberOfRooms = etNumberOfRooms.getText().toString().trim();
        String phoneNumber = etPhoneNumber.getText().toString().trim();
        String imageUrl = etImageUrl.getText().toString().trim();
        String houseType = houseTypeSpinner.getSelectedItem().toString();

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(description) || TextUtils.isEmpty(price)) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        // Cập nhật dữ liệu lên Firebase
        blogReference.child(blogID).child("title").setValue(title);
        blogReference.child(blogID).child("description").setValue(description);
        blogReference.child(blogID).child("price").setValue(price);
        blogReference.child(blogID).child("area").setValue(area);
        blogReference.child(blogID).child("address").setValue(address);
        blogReference.child(blogID).child("numberOfRooms").setValue(numberOfRooms);
        blogReference.child(blogID).child("phoneNumber").setValue(phoneNumber);
        blogReference.child(blogID).child("imageUrl").setValue(imageUrl);
        blogReference.child(blogID).child("houseType").setValue(houseType);

        Toast.makeText(this, "Bài đăng đã được cập nhật", Toast.LENGTH_SHORT).show();
        finish();
    }
}
