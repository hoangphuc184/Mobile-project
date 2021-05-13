package com.example.gallery;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ImageByLocMemberAdapter extends RecyclerView.Adapter<ImageByLocMemberAdapter.ViewHolder> {
    ArrayList<String> arrayMemberList;
    Context context;
    public ImageByLocMemberAdapter(ArrayList<String> arrayMemberList, Context context) {
        this.arrayMemberList  = arrayMemberList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.gallery_item, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final String image =arrayMemberList.get(position);
        Glide.with(context).load(image).into(holder.iv);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FullScreenActivity.class);
                intent.putExtra("path", image);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayMemberList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv = itemView.findViewById(R.id.image);
        }
    }
}
