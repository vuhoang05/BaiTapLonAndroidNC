package com.example.baitaplon;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.baitaplon.Domain.Blog;
import com.example.baitaplon.databinding.ActivityDetailBlogBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class DetailBlog extends AppCompatActivity {
    ActivityDetailBlogBinding binding;

    private TextView title, description, address, price, area, numberOfRooms, houseType, contact;
    private ImageView image,btnBack;
    private Button buttonCall, buttonText, btnLocation;
    private String contactNumber;
    private FusedLocationProviderClient fusedLocationClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBlogBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Ánh xạ các view
        image = findViewById(R.id.detail_image);
        title = findViewById(R.id.detail_title);
        description = findViewById(R.id.detail_description);
        address = findViewById(R.id.detail_address);
        price = findViewById(R.id.detail_price);
        area = findViewById(R.id.detail_area);
        numberOfRooms = findViewById(R.id.detail_number_of_rooms);
        houseType = findViewById(R.id.detail_house_type);
        contact = findViewById(R.id.detail_contact);
        buttonCall = findViewById(R.id.button_call);
        buttonText = findViewById(R.id.button_text);
        btnBack = findViewById(R.id.btnBack);
        btnLocation = findViewById(R.id.btnlocaiton);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Lấy ID bài đăng từ Intent
        String blogId = getIntent().getStringExtra("BLOG_ID");
        if (blogId != null) {
            // Lấy dữ liệu từ Firebase dựa vào blogId và hiển thị chi tiết bài đăng
            DatabaseReference blogRef = FirebaseDatabase.getInstance().getReference("Blogs").child(blogId);
            blogRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Blog blog = snapshot.getValue(Blog.class);
                    if (blog != null) {
                        title.setText(blog.getTitle());
                        description.setText(blog.getDescription());
                        address.setText("Địa chỉ: " + blog.getAddress());
                        price.setText("Giá: " + blog.getPrice() + " VND");
                        area.setText("Diện tích: " + blog.getArea());
                        numberOfRooms.setText("Số phòng: " + blog.getNumberOfRooms());
                        houseType.setText("Loại nhà: " + blog.getHouseType());
                        contact.setText("Liên hệ: " + blog.getSdt());
                        contactNumber = blog.getSdt(); // Lưu số điện thoại để sử dụng cho các nút liên hệ

                        // Hiển thị hình ảnh bằng Glide
                        Glide.with(DetailBlog.this)
                                .load(blog.getImageUrl())
                                .into(image);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Xử lý lỗi nếu cần
                }
            });
        }

        // Nút nhắn tin
        buttonText.setOnClickListener(v -> {
            if (contactNumber != null) {
                Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                smsIntent.setData(Uri.parse("sms:" + contactNumber));
                startActivity(smsIntent);
            }
        });

        // Gọi điện
        buttonCall.setOnClickListener(v -> {
            if (contactNumber != null) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + contactNumber));
                startActivity(callIntent);
            }
        });

        // Nút quay lại
        btnBack.setOnClickListener(view -> {
            Intent intent = new Intent(DetailBlog.this, MainActivity.class);
            startActivity(intent);
        });

        // Nút vị trí
        btnLocation.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(DetailBlog.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(DetailBlog.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            } else {
                getCurrentLocationAndOpenMap();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocationAndOpenMap();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getCurrentLocationAndOpenMap() {
        // Đoạn mã để lấy vị trí hiện tại và vị trí của trọ (giống như bạn đã viết)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                double userLatitude = location.getLatitude();
                double userLongitude = location.getLongitude();

                String addressText = address.getText().toString();
                Geocoder geocoder = new Geocoder(DetailBlog.this);
                try {
                    List<Address> addressList = geocoder.getFromLocationName(addressText, 1);
                    if (addressList != null && !addressList.isEmpty()) {
                        Address trọAddress = addressList.get(0);
                        double trọLatitude = trọAddress.getLatitude();
                        double trọLongitude = trọAddress.getLongitude();

                        Intent mapIntent = new Intent(DetailBlog.this, TestMap.class);
                        mapIntent.putExtra("userLatitude", userLatitude);
                        mapIntent.putExtra("userLongitude", userLongitude);
                        mapIntent.putExtra("houseLatitude", trọLatitude);
                        mapIntent.putExtra("houseLongitude", trọLongitude);
                        startActivity(mapIntent);
                    } else {
                        Toast.makeText(DetailBlog.this, "Không tìm thấy vị trí của trọ từ địa chỉ", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(DetailBlog.this, "Lỗi khi chuyển đổi địa chỉ thành vị trí", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(DetailBlog.this, "Không thể lấy vị trí hiện tại, vui lòng bật GPS", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
