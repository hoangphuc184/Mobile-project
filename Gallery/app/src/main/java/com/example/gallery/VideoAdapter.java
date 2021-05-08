package com.example.gallery;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {

    Context context;
    ArrayList<VideoModel> listOfVideo;
    Activity activity;

    public VideoAdapter(Context context, ArrayList<VideoModel> listOfVideo, Activity activity) {
        this.context = context;
        this.listOfVideo = listOfVideo;
        this.activity = activity;
    }

    @NonNull
    @Override
    public VideoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_item, parent, false);
        return new VideoAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoAdapter.ViewHolder holder, int position) {
        Glide.with(context).load("file://" + listOfVideo.get(position).getVideoThumb()).skipMemoryCache(false).into(holder.imageView);

        holder.imageView.setOnClickListener((v) -> {
            Intent intent = new Intent(context, VideoPlayActivity.class);
            intent.putExtra("Video", listOfVideo.get(position).getVideoPath());
            activity.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return listOfVideo.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.iv_image);
        }
    }
}
