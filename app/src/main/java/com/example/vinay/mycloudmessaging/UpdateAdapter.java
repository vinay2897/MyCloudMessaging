package com.example.vinay.mycloudmessaging;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class UpdateAdapter extends RecyclerView.Adapter<UpdateAdapter.MyViewHolder> {

    private static final String TAG = "UpdateAdapter";
    private List<Update> updates;
    private RecyclerView recyclerView;
    private ArrayList<String> tags;
    private TagAdapter mAdapter;
    private Context c;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, date;

        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            date = view.findViewById(R.id.date);
        }
    }

    public UpdateAdapter(List<Update> updates) {
        this.updates = updates;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.announcement_list, parent, false);
        recyclerView = (RecyclerView)itemView.findViewById(R.id.recycler_view_tag);
        c = itemView.getContext();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Update update = updates.get(position);

        mAdapter = new TagAdapter(update.getCategories());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(c,LinearLayoutManager.HORIZONTAL, true);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        holder.title.setText(update.getTitle());
        holder.date.setText(update.getStringDate());
        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                browserIntent.setData(Uri.parse(update.getLink()));
                c.startActivity(browserIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return updates.size();
    }
}

