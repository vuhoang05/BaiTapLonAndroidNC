package com.example.baitaplon;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.baitaplon.Apdapter.BlogAdapter;
import com.example.baitaplon.Domain.Blog;
import com.example.baitaplon.Domain.Favorite;
import com.example.baitaplon.databinding.ActivityDetailBlogBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DetailBlog extends AppCompatActivity {
    ActivityDetailBlogBinding binding;
    FirebaseDatabase database;
    private DatabaseReference databaseFavorite;
    private TextView title, description, address, price, area, numberOfRooms, houseType, contact;

    private ImageView image,btnBack,btnSaveBlogg;
    private Button buttonCall, buttonText, btnLocation, btnBooking;
    private String contactNumber;

    private ImageView btnWishlist,btnListLoved;

    private String imageUrl;

    private FusedLocationProviderClient fusedLocationClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBlogBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database = FirebaseDatabase.getInstance();
        databaseFavorite = FirebaseDatabase.getInstance().getReference("Favorite");

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
        btnBooking = findViewById(R.id.btnBooking);
        buttonText = findViewById(R.id.button_text);
        btnBack = findViewById(R.id.btnBack);
        btnLocation = findViewById(R.id.btnlocaiton);
        btnWishlist=findViewById(R.id.btnWishlist);
        btnListLoved=findViewById(R.id.btnListLoved);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        initBlogs();
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
                        price.setText("Giá: " + blog.getPrice() + " đ/Tháng");
                        area.setText("Diện tích: " + blog.getArea());
                        numberOfRooms.setText("Số phòng: " + blog.getNumberOfRooms());
                        houseType.setText("Loại nhà: " + blog.getHouseType());
                        contact.setText("Liên hệ: " + blog.getSdt());
                        contactNumber = blog.getSdt(); // Lưu số điện thoại để sử dụng cho các nút liên hệ
                        imageUrl = blog.getImageUrl();
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
        if (blogId != null) {
            String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            databaseFavorite.orderByChild("userID").equalTo(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    boolean isLoved = false;
                    for (DataSnapshot favoriteSnapshot : snapshot.getChildren()) {
                        Favorite favorite = favoriteSnapshot.getValue(Favorite.class);
                        if (favorite != null && favorite.getTitle().equals(title.getText().toString())) {
                            isLoved = true;
                            break;
                        }
                    }

                    // Cập nhật trạng thái nút
                    if (isLoved) {
                        btnWishlist.setVisibility(View.GONE);
                        btnListLoved.setVisibility(View.VISIBLE);
                    } else {
                        btnWishlist.setVisibility(View.VISIBLE);
                        btnListLoved.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(DetailBlog.this, "Lỗi tải trạng thái yêu thích: " + error.getMessage(), Toast.LENGTH_SHORT).show();
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
        btnBooking.setOnClickListener(view -> {
            Intent intent = new Intent(DetailBlog.this, BookViewingRoom.class);
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
        btnWishlist.setOnClickListener(view -> {
            // Ẩn nút Wishlist và hiển thị nút Loved
            btnWishlist.setVisibility(View.GONE);
            btnListLoved.setVisibility(View.VISIBLE);


            // Lấy ID người dùng từ Firebase
            String userID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

            // Tạo ID duy nhất cho favorite
            String favoriteID = databaseFavorite.push().getKey();
            String postID = getIntent().getStringExtra("BLOG_ID");
            if (favoriteID != null) {
                // Tạo đối tượng Favorite
                Favorite favorite = new Favorite(
                        favoriteID,title.getText().toString(), // Tiêu đề bài viết
                        description.getText().toString(), // Mô tả
                        imageUrl,// Hình ảnh (lấy từ thẻ Tag của ImageView)
                        area.getText().toString(), // Diện tích
                        price.getText().toString(), // Giá
                        numberOfRooms.getText().toString(), // Số phòng
                        postID,
                        address.getText().toString(), // Địa chỉ
                        userID, // ID người dùng
                        contact.getText().toString(), // Số điện thoại liên hệ
                        houseType.getText().toString(), // Loại nhà
                        true, // Trạng thái yêu thích (true khi thêm vào yêu thích)
                        false // Trạng thái Loved (false khi chưa yêu thích)


                );

                // Cập nhật vào Firebase Database
                databaseFavorite.child(favoriteID).setValue(favorite)
                        .addOnSuccessListener(aVoid -> {
                            // Hiển thị thông báo khi thêm thành công
                            Toast.makeText(DetailBlog.this, "Đã thêm vào danh sách yêu thích!", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            // Hiển thị thông báo lỗi khi thêm thất bại
                            Toast.makeText(DetailBlog.this, "Lỗi khi thêm vào yêu thích: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });


            }
        });

        btnListLoved.setOnClickListener(view -> {
            String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

            databaseFavorite.orderByChild("userID").equalTo(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot favoriteSnapshot : snapshot.getChildren()) {
                        Favorite favorite = favoriteSnapshot.getValue(Favorite.class);
                        if (favorite != null && favorite.getTitle().equals(title.getText().toString())) {
                            favoriteSnapshot.getRef().removeValue()
                                    .addOnSuccessListener(unused -> {
                                        Toast.makeText(DetailBlog.this, "Đã xóa khỏi danh sách yêu thích!", Toast.LENGTH_SHORT).show();
                                        btnWishlist.setVisibility(View.VISIBLE);
                                        btnListLoved.setVisibility(View.GONE);
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(DetailBlog.this, "Xóa thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                            return;
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(DetailBlog.this, "Lỗi truy vấn dữ liệu yêu thích: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });







    }
    private void initBlogs(){
        DatabaseReference myRef = database.getReference("Blogs");

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
                        binding.recyclerBlogsRelate.setLayoutManager(new LinearLayoutManager(DetailBlog.this, LinearLayoutManager.HORIZONTAL, false));
                        RecyclerView.Adapter adapter = new BlogAdapter(list);
                        binding.recyclerBlogsRelate.setAdapter(adapter);
                    }

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
                        Address troAddress = addressList.get(0);
                        double troLatitude = troAddress.getLatitude();
                        double troLongitude = troAddress.getLongitude();

                        Intent mapIntent = new Intent(DetailBlog.this, TestMap.class);
                        mapIntent.putExtra("userLatitude", userLatitude);
                        mapIntent.putExtra("userLongitude", userLongitude);
                        mapIntent.putExtra("houseLatitude", troLatitude);
                        mapIntent.putExtra("houseLongitude", troLongitude);
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
