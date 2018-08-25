package com.example.vinay.mycloudmessaging;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private String clickedItem;
    private RecyclerView recyclerView;
    private DrawerLayout mDrawerLayout;
    private Toolbar toolbar;
    private Bundle args;
    private FragmentManager fragmentManager;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        designToolbar();
        mDrawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        clickedItem = menuItem.toString();
                        createFragment(clickedItem);
                        return true;
                    }
                });

        createNotification();

        if(savedInstanceState==null){
            Intent intent1 = new Intent(MainActivity.this,RegisterDeviceService.class);
            startService(intent1);

            args = new Bundle();
            args.putString("title", "Home");
            Fragment fragment = new UpdateFragment();
            fragment.setArguments(args);
            fragmentManager = getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.add(R.id.content, fragment);
            ft.commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void createNotification(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId  = getString(R.string.default_notification_channel_id);
            String channelName = getString(R.string.default_notification_channel_name);
            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_LOW));
        }
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final Context c = context;
            ArrayList<Update> updates = (ArrayList<Update>)intent.getSerializableExtra("message");
            UpdateAdapter mAdapter = new UpdateAdapter(updates);
            View view = findViewById(R.id.loading_panel);
            view.setVisibility(View.GONE);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(c);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                    DividerItemDecoration.VERTICAL);
            recyclerView.addItemDecoration(mDividerItemDecoration);
            recyclerView.setAdapter(mAdapter);
        }
    };

    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceSate) {
        super.onSaveInstanceState(savedInstanceSate);
        String g = toolbar.getTitle().toString();
        savedInstanceSate.putString("title",g+"");
    }

    public void designToolbar(){
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
    }

    public void createFragment(String title){
        Fragment fragment = null;
        if(toolbar.getTitle().equals(title))
            return;
        switch(title){
            case "Admission":
                fragment = new AdmissionFragment();
                break;
            case "Announcements":
                fragment = new AnnouncementFragment();
                break;
            case "News":
                fragment = new NewsFragment();
                break;
            case "Notice":
                fragment = new NoticeFragment();
                break;
            case "Events":
                fragment = new EventFragment();
                break;
            case "Exam":
                fragment = new ExamFragment();
                break;
            default:
                fragment = new HomeFragment();
                break;
        }

        if(fragment!=null){
            args = new Bundle();
            args.putString("title", title);
            fragment.setArguments(args);
            FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
            ft1.replace(R.id.content, fragment);
            ft1.addToBackStack(title);
            ft1.commit();
        }
    }

}
