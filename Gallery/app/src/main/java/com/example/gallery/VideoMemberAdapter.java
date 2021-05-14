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

public class VideoMemberAdapter extends RecyclerView.Adapter<VideoMemberAdapter.ViewHolder> {
    ArrayList<VideoModel> arrayMemberList;
    Context context;
    public VideoMemberAdapter(ArrayList<VideoModel> arrayMemberList, Context context) {
        this.arrayMemberList  = arrayMemberList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_item, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load("file://" + arrayMemberList.get(position).getVideoThumb()).skipMemoryCache(false).into(holder.iv);

        holder.iv.setOnClickListener((v) -> {
            Intent intent = new Intent(context, VideoPlayActivity.class);
            intent.putExtra("Video", arrayMemberList.get(position).getVideoPath());
            context.startActivity(intent);
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
            iv = itemView.findViewById(R.id.iv_image);
        }
    }
}
