package com.example.baitaplon;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.baitaplon.Apdapter.BookingAdapter; // Sửa typo từ "Apdapter" thành "Adapter"
import com.example.baitaplon.Domain.Booking;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BookingListActivity extends AppCompatActivity {

    private ListView listViewBookings;
    private BookingAdapter bookingAdapter;
    private ArrayList<Booking> bookingList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_list);

        // Liên kết View với ID
        listViewBookings = findViewById(R.id.listViewBookings);

        // Khởi tạo danh sách và Adapter
        bookingList = new ArrayList<>();
        bookingAdapter = new BookingAdapter(this, bookingList);
        listViewBookings.setAdapter(bookingAdapter);

        // Lấy dữ liệu từ Firebase
        fetchBookingData();
    }

    private void fetchBookingData() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Bookings");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bookingList.clear(); // Xóa dữ liệu cũ trước khi thêm mới
                for (DataSnapshot bookingSnapshot : snapshot.getChildren()) {
                    Booking booking = bookingSnapshot.getValue(Booking.class);
                    if (booking != null) {
                        bookingList.add(booking);
                    }
                }
                bookingAdapter.notifyDataSetChanged(); // Cập nhật danh sách trong Adapter
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(BookingListActivity.this,
                        "Lỗi khi tải dữ liệu: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
