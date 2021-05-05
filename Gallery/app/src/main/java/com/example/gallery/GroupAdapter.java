package com.example.gallery;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ViewHolder> {
    ArrayList<String> arrayListGroup;
    Context context;
    public GroupAdapter(ArrayList<String> arrayListGroup, Context context) {
        this.arrayListGroup = arrayListGroup;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_row_group, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvName.setText(arrayListGroup.get(position));

        //init member array

        ArrayList<String>  arrayListMem = new ArrayList<>();

        try {
            arrayListMem = listOfImage.listOfImages(context, arrayListGroup.get(position));
        } catch (IOException e) {
            e.printStackTrace();
        }
        MemberAdapter memAdap = new MemberAdapter(arrayListMem, context);
        holder.recyclerViewMember.setLayoutManager(new GridLayoutManager(context, 3));
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
