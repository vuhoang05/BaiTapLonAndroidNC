package com.example.baitaplon;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.baitaplon.Domain.Blog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class UploadBlog extends AppCompatActivity {
    private EditText titleEditText, descriptionEditText, priceEditText, areaEditText, numberOfRoomEditText,phoneNumberEditText,imageUrlEditText,addressEditText;
    private Spinner houseTypeSpinner;
    private Button postBlogButton;
    private FirebaseDatabase database;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_blog);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        titleEditText = findViewById(R.id.titleBlog);
        descriptionEditText = findViewById(R.id.description);
        imageUrlEditText = findViewById(R.id.urlImg);
        houseTypeSpinner = findViewById(R.id.houseTypeSpinner);
        priceEditText = findViewById(R.id.price);
        areaEditText = findViewById(R.id.area);
        numberOfRoomEditText = findViewById(R.id.numberOfRoom);
        postBlogButton = findViewById(R.id.postBlog);
        phoneNumberEditText = findViewById(R.id.phoneNumber);
        addressEditText = findViewById(R.id.address);

        ArrayList<String> houseTypes = new ArrayList<>();
        houseTypes.add("Căn hộ");
        houseTypes.add("Nhà riêng");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, houseTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        houseTypeSpinner.setAdapter(adapter);
        // Khởi tạo Firebase Realtime Database
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Blogs");
        mAuth = FirebaseAuth.getInstance();

        postBlogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String idBlog = databaseReference.push().getKey();
                String title = titleEditText.getText().toString();
                String description = descriptionEditText.getText().toString();
                String houseType = houseTypeSpinner.getSelectedItem().toString();
                String price = priceEditText.getText().toString();
                String area = areaEditText.getText().toString();
                String numberOfRoom = numberOfRoomEditText.getText().toString();
                String idUser = mAuth.getCurrentUser().getUid();
                String phoneNumber = phoneNumberEditText.getText().toString();
                String imageUrl= imageUrlEditText.getText().toString();

                // Kiểm tra xem các trường có được nhập đầy đủ hay không
                if (title.isEmpty() || description.isEmpty() || price.isEmpty() || area.isEmpty() || numberOfRoom.isEmpty()) {
                    Toast.makeText(UploadBlog.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }

                Blog blog = new Blog(idBlog, houseType, phoneNumber, idUser, addressEditText.getText().toString(), numberOfRoom, price, area, imageUrl, description, title);
                databaseReference.child(idBlog).setValue(blog)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(UploadBlog.this, "Bài đăng đã được lưu thành công!", Toast.LENGTH_SHORT).show();
                            titleEditText.setText("");
                            descriptionEditText.setText("");
                            imageUrlEditText.setText("");
                            priceEditText.setText("");
                            areaEditText.setText("");
                            numberOfRoomEditText.setText("");
                            phoneNumberEditText.setText("");
                            addressEditText.setText("");
                            houseTypeSpinner.setSelection(0);
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(UploadBlog.this, "Lỗi khi lưu bài đăng: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });


            }
        });
    }
}
