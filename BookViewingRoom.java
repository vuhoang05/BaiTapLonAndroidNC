package com.example.baitaplon;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.baitaplon.Domain.Booking;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class BookViewingRoom extends AppCompatActivity {

    private EditText edtSelectTime;
    private Button btnConfirmBooking;
    private TextView tvTenantInfo, tvOwnerInfo;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_viewing_room);

        // Liên kết các View với ID
        edtSelectTime = findViewById(R.id.edtSelectTime);
        btnConfirmBooking = findViewById(R.id.btnConfirmBooking);
        tvTenantInfo = findViewById(R.id.tvTenantInfo);
        tvOwnerInfo = findViewById(R.id.tvOwnerInfo);

        // Khởi tạo Firebase Auth và Database Reference
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Bookings");

        // Lấy thông tin người dùng hiện tại
        loadUserInfo();

        // Sự kiện chọn thời gian xem phòng
        edtSelectTime.setOnClickListener(v -> showDateTimePicker());

        // Sự kiện nhấn nút "Đặt lịch xem phòng"
        btnConfirmBooking.setOnClickListener(v -> confirmBooking());

        // Thiết lập padding cho hệ thống window (nếu cần)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Phương thức tải thông tin người dùng hiện tại
    private void loadUserInfo() {
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Profiles").child(userId);

            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        // Lấy tên người dùng
                        String tenantName = snapshot.child("name").getValue(String.class);
                        if (tenantName != null) {
                            tvTenantInfo.setText(tenantName);
                        } else {
                            tvTenantInfo.setText("Khách thuê ẩn danh");
                        }


                    } else {
                        Toast.makeText(BookViewingRoom.this, "Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    Toast.makeText(BookViewingRoom.this, "Lỗi khi tải thông tin người dùng", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "Người dùng chưa đăng nhập!", Toast.LENGTH_SHORT).show();
        }
    }

    // Xử lý logic đặt lịch
    private void confirmBooking() {
        String selectedTime = edtSelectTime.getText().toString();
        String tenantName = tvTenantInfo.getText().toString(); // Lấy tên khách thuê
        String ownerName = tvOwnerInfo.getText().toString();   // Lấy tên chủ nhà

        if (selectedTime.isEmpty()) {
            Toast.makeText(this, "Vui lòng chọn thời gian xem phòng", Toast.LENGTH_SHORT).show();
        } else {
            // Tạo đối tượng Booking
            Booking booking = new Booking(tenantName, ownerName, selectedTime);

            // Lưu vào Firebase
            String bookingId = databaseReference.push().getKey();
            if (bookingId != null) {
                databaseReference.child(bookingId).setValue(booking)
                        .addOnSuccessListener(aVoid ->
                                Toast.makeText(this, "Đặt lịch xem phòng thành công!", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e ->
                                Toast.makeText(this, "Lỗi khi lưu lịch: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        }
    }

    // Hiển thị DatePicker và TimePicker
    private void showDateTimePicker() {
        // Lấy thời gian hiện tại
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Hiển thị DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // Khi người dùng chọn ngày, tiếp tục hiển thị TimePickerDialog
                    showTimePicker(selectedYear, selectedMonth, selectedDay);
                }, year, month, day);
        datePickerDialog.show();
    }

    private void showTimePicker(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // Hiển thị TimePickerDialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, selectedHour, selectedMinute) -> {
                    // Định dạng thời gian đã chọn và hiển thị lên EditText
                    String selectedDateTime = String.format("%02d/%02d/%d %02d:%02d",
                            day, month + 1, year, selectedHour, selectedMinute);
                    edtSelectTime.setText(selectedDateTime);
                }, hour, minute, true);
        timePickerDialog.show();
    }
}
