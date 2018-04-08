package com.g.laurent.mynews.Controllers;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.g.laurent.mynews.R;

public class SettingActivity extends AppCompatActivity {

    private static final String EXTRA_SETTING_TYPE = "setting_type";
    private String setting_type;
    private SettingFragment settingFragment;
    public Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Intent intent = getIntent();
        setting_type = intent.getStringExtra(MainActivity.EXTRA_SETTING_TYPE);
        bundle = new Bundle();
        bundle.putString(EXTRA_SETTING_TYPE,setting_type);
        configureAndShowSettingFragment();
        configureToolbar(setting_type);
    }


    private void configureAndShowSettingFragment(){

        settingFragment = (SettingFragment) getSupportFragmentManager().findFragmentById(R.id.setting_activity_layout);

        if (settingFragment == null) {
            settingFragment = new SettingFragment();
            settingFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.setting_activity_layout, settingFragment)
                    .commit();
        }
        else
            settingFragment.setArguments(bundle);

    }

    private void configureToolbar(String setting_type){
        // Get a support ActionBar corresponding to this toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_setting_toolbar);
        // Sets the Toolbar
        setSupportActionBar(toolbar);

        if(toolbar!=null) {

            switch (setting_type) {
                case "search":
                    toolbar.setTitle("Search Articles");
                    break;
                case "notif":
                    toolbar.setTitle("Notifications");
                    break;
            }

            // Enable the Up button
            //toolbar.setDisplayHomeAsUpEnabled(true);
        }
    }


    public void defineDate(View view) {


    }
}
