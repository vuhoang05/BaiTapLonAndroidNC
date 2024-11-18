package com.example.baitaplon.Apdapter;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.baitaplon.DetailBlog;
import com.example.baitaplon.Domain.Blog;
import com.example.baitaplon.EditBlog;
import com.example.baitaplon.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ManageBlogAdapter extends RecyclerView.Adapter<ManageBlogAdapter.viewHolder> {
    private ArrayList<Blog> blogs;
    private Context context;

    public ManageBlogAdapter(ArrayList<Blog> blogs) {
        this.blogs = blogs;
    }

    @NonNull
    @Override
    public ManageBlogAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_manage_blogs, parent, false);
        return new viewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        Blog currentBlog = blogs.get(position);

        holder.titleBlog.setText(currentBlog.getTitle());
        holder.area.setText(currentBlog.getArea() + "m2");
        holder.price_txt.setText(currentBlog.getPrice() + " vnd");
        holder.address.setText("Địa chỉ:" + currentBlog.getAddress());

        Glide.with(context)
                .load(currentBlog.getImageUrl())
                .transform(new CenterCrop(), new RoundedCorners(20))
                .into(holder.img);

        View.OnClickListener detailClickListener = v -> {
            Intent intent = new Intent(context, DetailBlog.class);
            intent.putExtra("BLOG_ID", blogs.get(position).getPostID()); // Truyền ID của blog sang DetailActivity
            context.startActivity(intent);
        };
        holder.img.setOnClickListener(detailClickListener);
        holder.titleBlog.setOnClickListener(detailClickListener);

        // Edit button click handlers
        holder.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditBlog.class);
            intent.putExtra("BLOG_ID", currentBlog.getPostID());
            context.startActivity(intent);
        });

        // Delete button click handler
        holder.btnDelete.setOnClickListener(v -> {
            // Handle blog deletion
            DatabaseReference blogRef = FirebaseDatabase.getInstance().getReference("Blogs").child(currentBlog.getPostID());
            blogRef.removeValue().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(context, "Blog deleted successfully!", Toast.LENGTH_SHORT).show();
                    blogs.remove(position);
                    notifyItemRemoved(position);
                } else {
                    Toast.makeText(context, "Failed to delete blog", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return blogs.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView titleBlog, price_txt, address, area;
        ImageView img;
        Button btnEdit, btnDelete;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            titleBlog = itemView.findViewById(R.id.titleBlog);
            price_txt = itemView.findViewById(R.id.price_txt);
            address = itemView.findViewById(R.id.address);
            img = itemView.findViewById(R.id.img);
            area = itemView.findViewById(R.id.areaText);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
