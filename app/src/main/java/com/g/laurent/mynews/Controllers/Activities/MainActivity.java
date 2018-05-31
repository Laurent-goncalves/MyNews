package com.g.laurent.mynews.Controllers.Activities;

import android.app.AlarmManager;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.g.laurent.mynews.Controllers.Fragments.PageFragment;
import com.g.laurent.mynews.Controllers.Fragments.NotifFragment;
import com.g.laurent.mynews.Controllers.Fragments.SearchFragment;
import com.g.laurent.mynews.Models.AlarmReceiver;
import com.g.laurent.mynews.Models.Callback_search;
import com.g.laurent.mynews.Models.ListArticlesSearch;
import com.g.laurent.mynews.Models.Search_request;
import com.g.laurent.mynews.R;
import com.g.laurent.mynews.Views.PageAdapter;

import java.util.Calendar;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements Callback_search, AlarmReceiver.callbackAlarm {

    /** DESCRIPTION : The MainActivity which extends BaseActivity will be used to integrate :
     *       - the PageFragment with the recyclerView (top stories, most popular, search,...)
     *       - the SearchFragment with the different criteria of search
     *       - the NotifFragment with the settings for sending notifications **/

    @BindView(R.id.toolbar_menu_button) ImageButton mIcon_menu;
    @BindView(R.id.toolbar_menu_search) ImageButton mIcon_search;
    @BindView(R.id.toolbar_menu_notif) ImageButton mIcon_notif;
    @BindView(R.id.toolbar_title) TextView mTitle_toolbar;
    @BindView(R.id.relative_layout_toolbar) RelativeLayout mRelativeLayout;
    @BindView(R.id.activity_main_frame_layout) LinearLayout mLinearLayout;
    private PageFragment mPageFragment;
    private SearchFragment searchFragment;
    private static final String EXTRA_TAB_NAME = "tab_name";
    private static final String EXTRA_NOTIF_SETTINGS = "NOTIFICATION_settings";
    private static final String EXTRA_ENABLE_NOTIF = "enable_notifications";
    private static final String EXTRA_QUERY_NOTIF = "query_notif";
    private static final String EXTRA_SUBJECTS_NOTIF = "list_subjects_notif";
    private static final String EXTRA_API_KEY = "api_key";
    private String api_key;
    private TabLayout tablayout;
    private String fragment_displayed;
    private SharedPreferences sharedPreferences_Notif;
    private String title_tb;
    private int count;
    private PageAdapter mPageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        // Assign variables
        fragment_displayed="mainfragment";
        String[] list_tabs = recover_list_tabs();
        tab_name=list_tabs[0];
        sharedPreferences_Notif = this.getSharedPreferences(EXTRA_NOTIF_SETTINGS, Context.MODE_PRIVATE);
        Boolean enable_notif = sharedPreferences_Notif.getBoolean(EXTRA_ENABLE_NOTIF, false);
        api_key = getApplicationContext().getResources().getString(R.string.APIkey);

        // configure alarm-manager and show PageFragment
        this.configureViewPagerAndTabs();
        this.configureAlarmManager(enable_notif);
    }


    @Override
    protected void onResume() {
        super.onResume();
        sharedPreferences_Notif = this.getSharedPreferences(EXTRA_NOTIF_SETTINGS, Context.MODE_PRIVATE);
        Boolean enable_notif = sharedPreferences_Notif.getBoolean(EXTRA_ENABLE_NOTIF, false);
        this.configureAlarmManager(enable_notif);
    }

    // -------------- CONFIGURATION Fragment and ALARMMANAGER --------------------

    private void configureAndShowMainFragment(){

        // Create a new bundle to send the tab_name to PageFragment
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_TAB_NAME,tab_name);
        bundle.putString(EXTRA_API_KEY,api_key);

        // configure tablayout
        if(tablayout==null)
            this.configureTabLayout();

        // configure toolbar
        this.configureToolbar("MyNews");
        configure_popupmenu_icon_toolbar();
        fragment_displayed="mainfragment";
        updateTabs();

        // configure and show the PageFragment
        mPageFragment = new PageFragment();
        mPageFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.activity_main_frame_layout, mPageFragment)
                .commit();
    }

    @Override
    public void configureAndShowMainFragmentSearchRequest(){

        // Create a new bundle to send the tab_name to PageFragment (for search requests)
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_TAB_NAME,"search request");
        bundle.putString(EXTRA_API_KEY,api_key);

        //configure toolbar and tablayout
        fragment_displayed="search mainfragment";
        updateTabs();

        this.configureToolbar("Search Articles");

        // configure and show the PageFragment
        mPageFragment = new PageFragment();
        mPageFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.activity_main_frame_layout, mPageFragment)
                .commit();
    }

    public void configureAndShowNotifFragment(){

        NotifFragment notifFragment = new NotifFragment();
        callback_save_settings = notifFragment; // create callback for savings settings
        callback_list_subjects = notifFragment; // create callback for updating list of subjects
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_API_KEY,api_key);
        notifFragment.setArguments(bundle);

        //configure toolbar and tablayout
        this.configureToolbar("Notifications");
        fragment_displayed="notiffragment";
        updateTabs();

        // configure and show the notifFragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.activity_main_frame_layout, notifFragment)
                .commit();
    }

    private void configureAlarmManager(Boolean enable){

        // Configuration of alarm for saving feeling each day
        AlarmReceiver.callbackAlarm mcallbackAlarm=this;
        AlarmReceiver alarmReceiver = new AlarmReceiver();
        alarmReceiver.createCallbackAlarm(mcallbackAlarm);

        Intent alarmIntent = new Intent(getApplicationContext(), alarmReceiver.getClass());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Set the alarm to start at 7:00 a.m.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 7);
        calendar.set(Calendar.MINUTE,0);

        // Create alarm
        AlarmManager manager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);

        if(manager!=null && enable) // IF NOTIFICATION ENABLED
            manager.setRepeating(AlarmManager.RTC,calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

        if(manager!=null && !enable) // IF NOTIFICATION DISABLED
            manager.cancel(pendingIntent);
    }

    public void configureAndShowSearchFragment(){

        searchFragment = new SearchFragment();
        callback_list_subjects = searchFragment;
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_API_KEY,api_key);
        searchFragment.setArguments(bundle);

        //configure toolbar and tablayout
        this.configureToolbar("Search Articles");
        fragment_displayed="searchfragment";
        updateTabs();

        //configure and show SearchFragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.activity_main_frame_layout, searchFragment)
                .commit();
    }

    // -------------- CONFIGURATION TabLayout  -----------------------------

    private void updateTabs(){

        try {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {

                    switch (fragment_displayed) {

                        case "searchfragment":
                            tablayout.setVisibility(View.GONE);
                            break;
                        case "notiffragment":
                            tablayout.setVisibility(View.GONE);
                            break;
                        case "search mainfragment":
                            tablayout.setVisibility(View.GONE);
                            break;
                        case "mainfragment":
                            if (tablayout.getVisibility() == View.GONE)
                                tablayout.setVisibility(View.VISIBLE);
                            break;
                    }
                }});
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
    }

    private void configureTabLayout(){
        // Recover list of tabs to be displayed and find the view to be assigned
        String[] list_tabs = recover_list_tabs();
        tablayout = findViewById(R.id.activity_main_tabs);

        // if the list of tabs is not null, create the tab (title + onTabSelectedListener
        if (list_tabs != null) {
            for (String tab : list_tabs) {

                tablayout.addTab(tablayout.newTab().setText(tab));
                tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        if(count==0) {
                            if (tab.getText() != null) {
                                tab_name = tab.getText().toString();
                            }
                            configureAndShowMainFragment();
                            count++;
                        }
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {
                        count = 0;
                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {
                    }
                });
            }
        }
    }

    private void configureViewPagerAndTabs(){

        // Get ViewPager from layout
        ViewPager pager = (ViewPager)findViewById(R.id.activity_main_viewpager);

        pager.setOffscreenPageLimit(3);

        PageAdapter mPageAdapter = new PageAdapter(getSupportFragmentManager(), api_key);

        // Set Adapter PageAdapter and glue it together
        pager.setAdapter(mPageAdapter);

        // Get TabLayout from layout
        TabLayout tabs= (TabLayout)findViewById(R.id.activity_main_tabs);

        // Glue TabLayout and ViewPager together
        tabs.setupWithViewPager(pager);

        // Design purpose. Tabs have the same width
        tabs.setTabMode(TabLayout.MODE_FIXED);
    }

    // -------------- CONFIGURATION Toolbar & icons -----------------------

    @Override
    protected void configureToolbar(String title){
        super.configureToolbar(title);

        this.title_tb = title;

        try {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {

                    if (toolbar != null && mTitle_toolbar != null) {
                        mTitle_toolbar.setText(title_tb);

                        if (getSupportActionBar() != null) {
                            switch (title_tb) {

                                case "MyNews":
                                    mIcon_search.setVisibility(View.VISIBLE);
                                    mIcon_menu.setVisibility(View.VISIBLE);
                                    mIcon_notif.setVisibility(View.VISIBLE);
                                    setIconOnClickListener();
                                    // Disable the Up button
                                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                                    break;
                                case "Search Articles":
                                    mIcon_search.setVisibility(View.GONE);
                                    mIcon_menu.setVisibility(View.GONE);
                                    mIcon_notif.setVisibility(View.GONE);
                                    // Enable the Up button
                                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                                    break;
                                case "Notifications":
                                    mIcon_search.setVisibility(View.GONE);
                                    mIcon_menu.setVisibility(View.GONE);
                                    mIcon_notif.setVisibility(View.GONE);
                                    // Enable the Up button
                                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                                    break;
                            }
                        }
                    }

                }
            });
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    private void configure_popupmenu_icon_toolbar(){

        mIcon_notif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(MainActivity.this, mIcon_notif);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.menu_toolbar, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        Toast toast;
                        switch (item.getItemId()){
                            case R.id.notifications:
                                configureAndShowNotifFragment();
                                return true;
                            case R.id.help:
                                toast = Toast.makeText(getApplicationContext(),"Item help selected",Toast.LENGTH_LONG);
                                toast.show();
                                return true;
                            case R.id.about:
                                toast = Toast.makeText(getApplicationContext(),"Item about selected",Toast.LENGTH_LONG);
                                toast.show();
                                return true;
                            default:
                                return false;
                        }
                    }
                });

                popup.show();//showing popup menu
            }
        });
    }

    private void setIconOnClickListener(){

        mIcon_search.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                configureToolbar("Search Articles");
                configureAndShowSearchFragment();
            }
        });
    }

    // ---------------- NOTIFICATIONS -------------------------------------
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // if the notifFragment is displayed when clicking on up button, save settings of notification and show mPageFragment
                if(fragment_displayed.equals("notiffragment"))
                    callback_save_settings.save_data();

                configureAndShowMainFragment();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void notification_request_checking() {

        // Check if notifications are enabled or not and then assign the variable enable
        Boolean enable = sharedPreferences_Notif.getBoolean(EXTRA_ENABLE_NOTIF,false);

        if(enable){

            // recover the variable query and list subjects in sharedpreferences
            String new_query = sharedPreferences_Notif.getString(EXTRA_QUERY_NOTIF,null);
            String list_subj = sharedPreferences_Notif.getString(EXTRA_SUBJECTS_NOTIF,null);

            if(list_subj!=null && new_query!=null) {

                // create a new request for search
                Search_request search_request = new Search_request("notif_checking",new_query, list_subj, null, null);
                // Launch a new search request to check if there are new articles
                new ListArticlesSearch(getApplicationContext(),api_key, search_request, sharedPreferences_Notif);
            }
        }
    }

    public PageFragment getPageFragment() {
        return mPageFragment;
    }

    public SearchFragment getSearchFragment() {
        return searchFragment;
    }

}
