package com.example.vinay.mycloudmessaging;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class FetchUpdatesService extends IntentService {

    private static final String TAG = "FetchUpdatesService";
    private DatabaseReference mDatabase;
    private ArrayList<Update> updates;
    private Context c = this;

    public FetchUpdatesService() {
        super("");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        final String category = intent.getStringExtra("update");
        final String broadcast= category.equals("Home")? "home":"broadcast";
        updates = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance().getReference("updates");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(category.equals("Home")){
                    for (DataSnapshot child : dataSnapshot.getChildren()){
                        updates.add(child.getValue(Update.class));
                    }
                }
                else{
                    for (DataSnapshot child : dataSnapshot.getChildren()){
                        Update temp = child.getValue(Update.class);
                        if(temp.getCategories().contains(category))
                            updates.add(temp);
                    }
                }
                Collections.sort(updates,Collections.reverseOrder());
                Log.d("eee",updates+"");
                Intent intent = new Intent(broadcast);
                intent.putExtra("message", updates);
                LocalBroadcastManager.getInstance(c).sendBroadcast(intent);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

}
