package com.g.laurent.mynews.Controllers.Activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.g.laurent.mynews.Controllers.Fragments.MainFragment;
import com.g.laurent.mynews.Controllers.Fragments.NotifFragment;
import com.g.laurent.mynews.Controllers.Fragments.SearchFragment;
import com.g.laurent.mynews.Models.AlarmReceiver;
import com.g.laurent.mynews.Models.Callback_search;
import com.g.laurent.mynews.Models.ListArticlesSearch;
import com.g.laurent.mynews.Models.Search_request;
import com.g.laurent.mynews.R;
import com.g.laurent.mynews.Views.ArticleAdapter;

import java.util.Calendar;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements Callback_search, AlarmReceiver.callbackAlarm {

    /** DESCRIPTION : The MainActivity which extends BaseActivity will be used to integrate :
     *       - the MainFragment with the recyclerView (top stories, most popular, search,...)
     *       - the SearchFragment with the different criteria of search
     *       - the NotifFragment with the settings for sending notifications **/

    @BindView(R.id.toolbar_menu_button) ImageButton icon_menu;
    @BindView(R.id.toolbar_menu_search) ImageButton icon_search;
    @BindView(R.id.toolbar_menu_notif) ImageButton icon_notif;
    @BindView(R.id.toolbar_title) TextView title_toolbar;
    @BindView(R.id.relative_layout_toolbar) RelativeLayout mRelativeLayout;
    @BindView(R.id.activity_main_frame_layout) LinearLayout mLinearLayout;
    private MainFragment mainFragment;
    private SearchFragment searchFragment;
    private static final String EXTRA_TAB_NAME = "tab_name";
    private static final String EXTRA_NOTIF_SETTINGS = "NOTIFICATION_settings";
    private static final String EXTRA_ENABLE_NOTIF = "enable_notifications";
    private static final String EXTRA_QUERY_NOTIF = "query_notif";
    private static final String EXTRA_SUBJECTS_NOTIF = "list_subjects_notif";
    private TabLayout tablayout;
    private String fragment_displayed;
    private SharedPreferences sharedPreferences_Notif;
    private String title_tb;
    private int count;

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

        // configure alarm-manager and show MainFragment
        this.configureTabLayout();
        this.configureAndShowMainFragment();
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

        // Create a new bundle to send the tab_name to MainFragment
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_TAB_NAME,tab_name);

        // configure tablayout
        if(tablayout==null)
            this.configureTabLayout();

        // configure toolbar
        this.configureToolbar("MyNews");
        configure_popupmenu_icon_toolbar();
        fragment_displayed="mainfragment";
        updateTabs();

        // configure and show the MainFragment
        mainFragment = new MainFragment();
        mainFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.activity_main_frame_layout, mainFragment)
                .commit();
    }

    @Override
    public void configureAndShowMainFragmentSearchRequest(){

        // Create a new bundle to send the tab_name to MainFragment (for search requests)
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_TAB_NAME,"search request");

        //configure toolbar and tablayout
        fragment_displayed="search mainfragment";
        updateTabs();

        this.configureToolbar("Search Articles");

        // configure and show the MainFragment
        mainFragment = new MainFragment();
        mainFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.activity_main_frame_layout, mainFragment)
                .commit();
    }

    public void configureAndShowNotifFragment(){

        NotifFragment notifFragment = new NotifFragment();
        callback_save_settings = notifFragment; // create callback for savings settings
        callback_list_subjects = notifFragment; // create callback for updating list of subjects

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
                            tab_name = tab.getText().toString();
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

    // -------------- CONFIGURATION Toolbar & icons -----------------------

    @Override
    protected void configureToolbar(String title){
        super.configureToolbar(title);

        this.title_tb = title;

        try {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {

                    if (toolbar != null && title_toolbar != null) {
                        title_toolbar.setText(title_tb);

                        if (getSupportActionBar() != null) {
                            switch (title_tb) {

                                case "MyNews":
                                    icon_search.setVisibility(View.VISIBLE);
                                    icon_menu.setVisibility(View.VISIBLE);
                                    icon_notif.setVisibility(View.VISIBLE);
                                    setIconOnClickListener();
                                    // Disable the Up button
                                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                                    break;
                                case "Search Articles":
                                    icon_search.setVisibility(View.GONE);
                                    icon_menu.setVisibility(View.GONE);
                                    icon_notif.setVisibility(View.GONE);
                                    // Enable the Up button
                                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                                    break;
                                case "Notifications":
                                    icon_search.setVisibility(View.GONE);
                                    icon_menu.setVisibility(View.GONE);
                                    icon_notif.setVisibility(View.GONE);
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

        icon_notif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(MainActivity.this, icon_notif);
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

        icon_search.setOnClickListener(new Button.OnClickListener() {
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
                // if the notifFragment is displayed when clicking on up button, save settings of notification and show mainFragment
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

            if(list_subj!=null) {

                // create a new request for search
                Search_request search_request = new Search_request("notif_checking",new_query, list_subj, null, null);
                // Launch a new search request to check if there are new articles
                new ListArticlesSearch(getApplicationContext(), search_request, sharedPreferences_Notif, null);
            }
        }
    }

    public MainFragment getMainFragment() {
        return mainFragment;
    }

    public SearchFragment getSearchFragment() {
        return searchFragment;
    }
}
