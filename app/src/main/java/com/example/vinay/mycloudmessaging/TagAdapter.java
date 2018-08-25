package com.example.vinay.mycloudmessaging;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.MyViewHolder> {

    private static final String TAG = "TagAdapter";
    private List<String> tags;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tag;

        public MyViewHolder(View view) {
            super(view);
            tag = view.findViewById(R.id.tag);
        }
    }
    public TagAdapter(List<String> tags) {
        this.tags = tags;

    }

    public TagAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tag_list, parent, false);
        return new TagAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TagAdapter.MyViewHolder holder, int position) {

        String tag = tags.get(position);
        holder.tag.setText(tag);
    }

    @Override
    public int getItemCount() {
        return tags.size();
    }

}
