package com.example.gallery;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;

public class VideoGroupAdapter extends RecyclerView.Adapter<VideoGroupAdapter.ViewHolder> {
    static int index = 0;
    private Activity activity;
    ArrayList<String> arrayListGroup;

    public VideoGroupAdapter(Activity activity, ArrayList<String> arrayListGroup) {
        this.activity = activity;
        this.arrayListGroup = arrayListGroup;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_group, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvName.setText(arrayListGroup.get(position));

        //init member array

        ArrayList<VideoModel> arrayListMem = new ArrayList<>();

        try {
            arrayListMem = listOfVideos.listOfVideos(activity.getApplicationContext(), arrayListGroup.get(position));
        } catch (IOException e) {
            e.printStackTrace();
        }
        VideoMemberAdapter memAdap = new VideoMemberAdapter(arrayListMem, activity.getApplicationContext());
        holder.recyclerViewMember.setLayoutManager(new GridLayoutManager(activity.getApplicationContext(), 2));
        holder.recyclerViewMember.setAdapter(memAdap);
    }

    @Override
    public int getItemCount() {
        return arrayListGroup.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        RecyclerView recyclerViewMember;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            recyclerViewMember = itemView.findViewById(R.id.rv_group);
        }
    }
}
