package com.example.baitaplon;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.baitaplon.Domain.Blog;
import com.example.baitaplon.databinding.ActivityDetailBlogBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DetailBlog extends AppCompatActivity {
    ActivityDetailBlogBinding binding;
    private TextView title, description, address, price, area, numberOfRooms, houseType, contact;
    private ImageView image;
    private Button buttonCall, buttonText,btnBack;
    private String contactNumber;

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

        // Nút gọi điẹn
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
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailBlog.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
