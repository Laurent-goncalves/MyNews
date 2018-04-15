package com.g.laurent.mynews.Controllers.Activities;

import android.support.design.widget.TabLayout;
import android.os.Bundle;
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
import com.g.laurent.mynews.Models.Callback_list_subjects;
import com.g.laurent.mynews.Models.Callback_search;
import com.g.laurent.mynews.Models.Callback_settings;
import com.g.laurent.mynews.R;
import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends BaseActivity implements Callback_search {

    @BindView(R.id.toolbar_menu_button) ImageButton icon_menu;
    @BindView(R.id.toolbar_menu_search) ImageButton icon_search;
    @BindView(R.id.toolbar_menu_notif) ImageButton icon_notif;
    @BindView(R.id.toolbar_title) TextView title_toolbar;
    @BindView(R.id.relative_layout_toolbar) RelativeLayout mRelativeLayout;
    @BindView(R.id.activity_main_frame_layout) LinearLayout mLinearLayout;
    private MainFragment mainFragment;
    private SearchFragment searchFragment;
    private NotifFragment notifFragment;
    public static final String EXTRA_TAB_NAME = "tab_name";
    private TabLayout tablayout;
    private String fragment_displayed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        tab_name=null;
        fragment_displayed="mainfragment";
        String[] list_tabs = recover_list_tabs();

        if(tab_name==null){
            tab_name=list_tabs[0];
        }

        this.configureAndShowMainFragment();
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("eeee OnResume MainActivity");
        this.configureAndShowMainFragment();
    }

    // -------------- CONFIGURATION --------------------

    private void configureAndShowMainFragment(){

        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_TAB_NAME,tab_name);

        mainFragment = new MainFragment();
        mainFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.activity_main_frame_layout, mainFragment)
                .commit();

        if(tablayout==null)
            this.configureTabLayout();

        if(tablayout.getVisibility()==View.GONE)
            tablayout.setVisibility(View.VISIBLE);

        this.configureToolbar("MyNews");
        configure_popupmenu_icon_toolbar();
        fragment_displayed="mainfragment";
    }

    @Override
    public void configureAndShowMainFragmentSearchRequest(){

        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_TAB_NAME,"search request");

        mainFragment = new MainFragment();
        mainFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.activity_main_frame_layout, mainFragment)
                .commit();

        tablayout.setVisibility(View.GONE);

        this.configureToolbar("Search Articles");
        fragment_displayed="mainfragment";

    }

    private void configureAndShowNotifFragment(){

        notifFragment = new NotifFragment();
        callback_save_settings = (Callback_settings) notifFragment;
        callback_list_subjects = (Callback_list_subjects) notifFragment;
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.activity_main_frame_layout, notifFragment)
                .commit();

        this.configureToolbar("Notifications");
        fragment_displayed="notiffragment";
        tablayout.setVisibility(View.GONE);
    }

    private void configureAndShowSearchFragment(){

        searchFragment = new SearchFragment();
        callback_list_subjects = (Callback_list_subjects) searchFragment;
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.activity_main_frame_layout, searchFragment)
                .commit();

        this.configureToolbar("Search Articles");
        fragment_displayed="searchfragment";
        tablayout.setVisibility(View.GONE);
    }

    private void configureTabLayout(){

        String[] list_tabs = recover_list_tabs();
        tablayout = (TabLayout) findViewById(R.id.activity_main_tabs);

        if (list_tabs != null) {
            for (String tab : list_tabs) {

                tablayout.addTab(tablayout.newTab().setText(tab));
                tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        tab_name = tab.getText().toString();
                        configureAndShowMainFragment();
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {
                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {
                    }
                });
            }
        }
    }

    @Override
    protected void configureToolbar(String title){
        super.configureToolbar(title);

        if(toolbar!=null) {
            title_toolbar.setText(title);

            if(getSupportActionBar()!=null) {
                switch (title) {

                    case "MyNews":

                        icon_search.setVisibility(View.VISIBLE);
                        icon_menu.setVisibility(View.VISIBLE);
                        icon_notif.setVisibility(View.VISIBLE);
                        setIconOnClickListener();
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

    private void setIconOnClickListener(){

        icon_menu.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
            }
        });

        icon_search.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                configureToolbar("Search Articles");
                configureAndShowSearchFragment();
            }
        });
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if(fragment_displayed.equals("notiffragment"))
                    callback_save_settings.save_data();
                configureAndShowMainFragment();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
