package com.g.laurent.mynews.Controllers.Activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import com.g.laurent.mynews.Models.AlarmReceiver;
import com.g.laurent.mynews.R;
import com.g.laurent.mynews.Views.PageAdapter;
import java.util.Calendar;
import butterknife.ButterKnife;


public class MainActivity extends BaseActivity {

    private static final String EXTRA_NOTIF_SETTINGS = "NOTIFICATION_settings";
    private static final String EXTRA_ENABLE_NOTIF = "enable_notifications";
    private static final String EXTRA_API_KEY = "api_key";
    private String api_key;
    private String fragment_displayed;
    private PageAdapter mPageAdapter;
    private SharedPreferences sharedPreferences_Notif;
    private AlarmManager manager;
    private Calendar calendar;
    private PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // Assign variables
        fragment_displayed="mainfragment";
        String[] list_tabs = recover_list_tabs();
        tab_name=list_tabs[0];
        sharedPreferences_Notif = this.getSharedPreferences(EXTRA_NOTIF_SETTINGS, Context.MODE_PRIVATE);
        Boolean enable_notif = sharedPreferences_Notif.getBoolean(EXTRA_ENABLE_NOTIF, false);
        api_key = getApplicationContext().getResources().getString(R.string.APIkey);

        // configure alarm-manager, toolbar and show PageFragment
        this.configureViewPagerAndTabs();
        configureAlarmManager(enable_notif);
        configureToolbar(SCREEN_LIST_ARTICLES);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    // -------------- CONFIGURATION ALARMMANAGER ----------------------------------

    private void configureAlarmManager(Boolean enable){

        Intent alarmIntent = new Intent(getApplicationContext(), AlarmReceiver.class);
        alarmIntent.putExtra(EXTRA_API_KEY,api_key);
        pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, alarmIntent, 0);

        // Set the alarm to start at 7:00 a.m.
        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 7);
        calendar.set(Calendar.MINUTE,0);

        // Create alarm
        manager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);

        // enable or not alarm
        enable_or_not_alarm_manager(enable);
    }

    public void enable_or_not_alarm_manager(Boolean enable) {

        if(manager!=null && enable) // IF NOTIFICATION ENABLED
            manager.setRepeating(AlarmManager.RTC,calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

        if(manager!=null && !enable) // IF NOTIFICATION DISABLED
            manager.cancel(pendingIntent);
    }

    // -------------- CONFIGURATION TabLayout  -----------------------------

    private void configureViewPagerAndTabs(){

        // Get ViewPager from layout
        ViewPager pager = findViewById(R.id.activity_main_viewpager);
        pager.setOffscreenPageLimit(3);

        mPageAdapter = new PageAdapter(getSupportFragmentManager(), api_key, getApplicationContext());

        // Set Adapter PageAdapter and glue it together
        pager.setAdapter(mPageAdapter);

        // Get TabLayout from layout
        TabLayout tabs= findViewById(R.id.activity_main_tabs);

        // Glue TabLayout and ViewPager together
        tabs.setupWithViewPager(pager);

        // Design purpose. Tabs have the same width
        tabs.setTabMode(TabLayout.MODE_FIXED);
    }

    // ---------------- NOTIFICATIONS -------------------------------------
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // if the notifFragment is displayed when clicking on up button, save settings of notification and show mPageFragment
                if(fragment_displayed.equals("notiffragment"))
                    callback_save_settings.save_data();

                mPageAdapter = new PageAdapter(getSupportFragmentManager(), api_key, getApplicationContext());

                Boolean enable_notif = sharedPreferences_Notif.getBoolean(EXTRA_ENABLE_NOTIF, false);
                this.configureAlarmManager(enable_notif);

                return false;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public PageAdapter getPageAdapter() {
        return mPageAdapter;
    }

}
