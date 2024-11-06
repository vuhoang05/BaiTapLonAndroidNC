package com.example.baitaplon.Apdapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.baitaplon.DetailBlog;
import com.example.baitaplon.Domain.Blog;
import com.example.baitaplon.R;

import java.util.ArrayList;

public class BlogAdapter extends RecyclerView.Adapter<BlogAdapter.viewHolder> {
    ArrayList<Blog> blogs;
    Context context;

    public BlogAdapter(ArrayList<Blog> blogs) {
        this.blogs = blogs;

    }

    @NonNull
    @Override
    public BlogAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_blogs, parent, false);
        return new viewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull BlogAdapter.viewHolder holder, int position) {
        holder.titleBlog.setText(blogs.get(position).getTitle());
        holder.price_txt.setText(blogs.get(position).getPrice()+" vnd");
        holder.address.setText("Địa chỉ:"+blogs.get(position).getAddress());
        Glide.with(context)
                .load(blogs.get(position).getImageUrl())
                .transform(new CenterCrop(), new RoundedCorners(20))
                .into(holder.img);
        // Set OnClickListener for image and title


        /////////////////
        View.OnClickListener detailClickListener = v -> {
            Intent intent = new Intent(context, DetailBlog.class);
            intent.putExtra("BLOG_ID", blogs.get(position).getPostID()); // Truyền ID của blog sang DetailActivity
            context.startActivity(intent);
        };
        holder.img.setOnClickListener(detailClickListener);
        holder.titleBlog.setOnClickListener(detailClickListener);
        /////////////////////


    }

    @Override
    public int getItemCount() {
        return blogs.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView titleBlog, price_txt, address;
        ImageView img;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            titleBlog = itemView.findViewById(R.id.titleBlog);
            price_txt = itemView.findViewById(R.id.price_txt);
            address = itemView.findViewById(R.id.address);
            img = itemView.findViewById(R.id.img);
        }
    }
}
