package com.example.vinay.mycloudmessaging;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;


public class UpdateFragment extends Fragment {

    private RecyclerView recyclerView;
    private String title;
    public static final String TAG = "UpdateFragment";

    public UpdateFragment() { }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        title = getArguments().getString("title");
        Log.d(TAG,"ocd "+title);
        if(title.equals("Home")){
            LocalBroadcastManager.getInstance(this.getActivity()).registerReceiver(mMessageReceiver,
                    new IntentFilter("home"));
        }
        else{
            LocalBroadcastManager.getInstance(this.getActivity()).registerReceiver(mMessageReceiver,
                    new IntentFilter("broadcast"));
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG,"ocdv "+title);
        ProgressBar progressBar = ((AppCompatActivity)getActivity()).findViewById(R.id.loading_panel);
        progressBar.setVisibility(View.VISIBLE);
        View view = inflater.inflate(R.layout.fragment_update, container, false);
                recyclerView = view.findViewById(R.id.recycler_view);
                return view;
    }

    public void onResume() {
        super.onResume();
        Intent intent = new Intent(this.getActivity(),FetchUpdatesService.class);
        intent.putExtra("update",title);
        getActivity().startService(intent);
        Toolbar toolbar = ((AppCompatActivity)getActivity()).findViewById(R.id.toolbar);
        toolbar.setTitle(title);
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final Context c = context;
            ArrayList<Update> updates = (ArrayList<Update>)intent.getSerializableExtra("message");

            UpdateAdapter mAdapter = new UpdateAdapter(updates);
            ProgressBar progressBar = ((AppCompatActivity)getActivity()).findViewById(R.id.loading_panel);
            progressBar.setVisibility(View.GONE);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(c);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                    DividerItemDecoration.VERTICAL);
            recyclerView.addItemDecoration(mDividerItemDecoration);
            recyclerView.setAdapter(mAdapter);
        }
    };

    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this.getActivity()).unregisterReceiver(mMessageReceiver);
    }

}
