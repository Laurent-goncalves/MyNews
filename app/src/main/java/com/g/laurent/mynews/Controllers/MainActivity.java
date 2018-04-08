package com.g.laurent.mynews.Controllers;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.support.v7.view.menu.MenuBuilder;
import android.widget.ImageButton;
import android.widget.Toast;

import com.g.laurent.mynews.R;
import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_menu_button) ImageButton icon_menu;
    @BindView(R.id.toolbar_menu_search) ImageButton icon_search;
    private MainFragment mainFragment;
    public static final String EXTRA_SETTING_TYPE = "setting_type";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setIconOnClickListener();
        this.configureAndShowMainFragment();
        this.configureToolBar();
        this.configureTabLayout();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Toast toast;

        switch (item.getItemId()){
            case R.id.notifications:
                startSettingActivity_notif();
                return true;
            case R.id.help:
                toast = Toast.makeText(this,"Item help selected",Toast.LENGTH_LONG);
                toast.show();
                return true;
            case R.id.about:
                toast = Toast.makeText(this,"Item about selected",Toast.LENGTH_LONG);
                toast.show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // -------------- CONFIGURATION --------------------

    private void configureAndShowMainFragment(){

        mainFragment = (MainFragment) getSupportFragmentManager().findFragmentById(R.id.activity_main_frame_layout);
        if (mainFragment == null) {
            mainFragment = new MainFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.activity_main_frame_layout, mainFragment)
                    .commit();
        }
    }

    private void configureToolBar(){

        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_main_toolbar);
        // Sets the Toolbar
        setSupportActionBar(toolbar);

    }

    private String[] recover_list_tabs() {
        String[] list_tabs = getResources().getStringArray(R.array.list_tab);
        return list_tabs;
    }

    private void configureTabLayout(){

        String[] list_tabs = recover_list_tabs();
        TabLayout tablayout= (TabLayout) findViewById(R.id.activity_main_tabs);

        if(list_tabs!=null) {
            for (String tab : list_tabs)
                tablayout.addTab(tablayout.newTab().setText(tab));
        }
    }

    private void setIconOnClickListener(){

        icon_menu.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

            }
        });

        icon_search.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                startSettingActivity_search();
            }
        });
    }

    private void startSettingActivity_search(){
        Intent intent = new Intent(this,SettingActivity.class);
        intent.putExtra(EXTRA_SETTING_TYPE,"search");
        startActivity(intent);
    }

    private void startSettingActivity_notif(){
        Intent intent = new Intent(this,SettingActivity.class);
        intent.putExtra(EXTRA_SETTING_TYPE,"notif");
        startActivity(intent);
    }

}
