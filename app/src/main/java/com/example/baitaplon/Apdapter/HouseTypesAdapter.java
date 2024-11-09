package com.example.baitaplon.Apdapter;

import android.content.Context;
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
import com.example.baitaplon.Domain.Blog;

import java.text.BreakIterator;
import java.util.ArrayList;
import com.example.baitaplon.Domain.houseTypes;
import com.example.baitaplon.R;

public class HouseTypesAdapter extends RecyclerView.Adapter<HouseTypesAdapter.ViewHolder>{
    ArrayList<houseTypes> houseTypes;
    Context context;

    public HouseTypesAdapter(ArrayList<houseTypes> houseTypes) {
        this.houseTypes = houseTypes;
    }

    @NonNull
    @Override
    public HouseTypesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       context = parent.getContext();
       View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_housetypes,parent,false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull HouseTypesAdapter.ViewHolder holder, int position) {
        holder.titleHouseTypes.setText(houseTypes.get(position).getHouseTypeName());
        Glide.with(context)
                .load(houseTypes.get(position).getImageUrl())
                .transform(new CenterCrop(), new RoundedCorners(20))
                .into(holder.imgHouseTypes);
    }

    @Override
    public int getItemCount() {
        return houseTypes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
       TextView titleHouseTypes;
       ImageView imgHouseTypes;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleHouseTypes = itemView.findViewById(R.id.titleHouseTypes);
            imgHouseTypes = itemView.findViewById(R.id.imgHouseTypes);
        }
    }
}
