package com.example.baitaplon.Apdapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.baitaplon.DetailBlog;
import com.example.baitaplon.Domain.Favorite;
import com.example.baitaplon.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {
    ArrayList<Favorite> favoriteList;
    Context context;
    DatabaseReference databaseFavorite;

    public FavoriteAdapter(ArrayList<Favorite> favoriteList) {
        this.favoriteList = favoriteList;
        this.databaseFavorite = FirebaseDatabase.getInstance().getReference("Favorite");
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.viewholder_favorite, parent, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        Favorite favorite = favoriteList.get(position);

        // Gắn dữ liệu lên ViewHolder
        holder.titleBlog.setText(favorite.getTitle());
        holder.area.setText(favorite.getArea() + " m²");
        holder.price_txt.setText(favorite.getPrice());
        holder.address.setText("Địa chỉ: " + favorite.getAddress());

        // Tải ảnh bằng Glide
        Glide.with(context)
                .load(favorite.getImageUrl())
                .transform(new CenterCrop(), new RoundedCorners(20))
                .into(holder.img);

        // Mở chi tiết khi nhấn vào ảnh hoặc tiêu đề
        View.OnClickListener detailClickListener = v -> {
            Intent intent = new Intent(context, DetailBlog.class);
            intent.putExtra("BLOG_ID", favorite.getPostID());
            context.startActivity(intent);
        };
        holder.img.setOnClickListener(detailClickListener);
        holder.titleBlog.setOnClickListener(detailClickListener);

        // Xóa bài yêu thích khỏi Firebase và danh sách
        holder.btnListLoved.setOnClickListener(v -> {
            String favoriteID = favorite.getFavoriteID();
            holder.btnListLoved.setVisibility(View.GONE);
            holder.btnWishlist.setVisibility(View.VISIBLE);

            // Xóa trong Firebase
            databaseFavorite.child(favoriteID).removeValue()
                    .addOnSuccessListener(unused -> {
                        // Cập nhật lại danh sách
                        favoriteList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, favoriteList.size());

                        Toast.makeText(context, "Đã xóa khỏi danh sách yêu thích!", Toast.LENGTH_SHORT).show();

                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(context, "Xóa thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });
    }

    @Override
    public int getItemCount() {
        return favoriteList.size();
    }

    public static class FavoriteViewHolder extends RecyclerView.ViewHolder {
        TextView titleBlog, price_txt, address, area;
        ImageView img, btnListLoved, btnWishlist;

        public FavoriteViewHolder(View itemView) {
            super(itemView);
            titleBlog = itemView.findViewById(R.id.titleBlog);
            price_txt = itemView.findViewById(R.id.price_txt);
            address = itemView.findViewById(R.id.address);
            img = itemView.findViewById(R.id.img);
            area = itemView.findViewById(R.id.areaText);
            btnListLoved = itemView.findViewById(R.id.btnListLoved);
            btnWishlist = itemView.findViewById(R.id.btnWishlist);
        }
    }
}
