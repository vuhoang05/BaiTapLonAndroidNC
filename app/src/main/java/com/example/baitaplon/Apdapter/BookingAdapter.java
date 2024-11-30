package com.example.baitaplon.Apdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.widget.ArrayAdapter;

import com.example.baitaplon.Domain.Booking;
import com.example.baitaplon.R;

import java.util.ArrayList;

public class BookingAdapter extends ArrayAdapter<Booking> {

    // Constructor của Adapter
    public BookingAdapter(Context context, ArrayList<Booking> bookings) {
        super(context, 0, bookings);
    }

    // ViewHolder Pattern để tối ưu hóa hiệu suất
    private static class ViewHolder {
        TextView tvTenantName;
        TextView tvOwnerName;
        TextView tvTime;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;

        // Kiểm tra nếu convertView đã được tái sử dụng
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_booking, parent, false);

            // Khởi tạo ViewHolder và ánh xạ các thành phần giao diện
            viewHolder = new ViewHolder();
            viewHolder.tvTenantName = convertView.findViewById(R.id.tvTenantName);
            viewHolder.tvOwnerName = convertView.findViewById(R.id.tvOwnerName);
            viewHolder.tvTime = convertView.findViewById(R.id.tvTime);

            // Gắn ViewHolder vào convertView
            convertView.setTag(viewHolder);
        } else {
            // Lấy ViewHolder từ convertView đã tái sử dụng
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Lấy đối tượng Booking hiện tại
        Booking booking = getItem(position);
        if (booking != null) {
            // Gán dữ liệu từ đối tượng Booking vào các TextView
            viewHolder.tvTenantName.setText("Khách thuê: " + booking.getTenantName());
            viewHolder.tvOwnerName.setText("Chủ nhà: " + booking.getOwnerName());
            viewHolder.tvTime.setText("Thời gian: " + booking.getSelectedTime());
        }

        // Trả về convertView đã được cập nhật
        return convertView;
    }
}
