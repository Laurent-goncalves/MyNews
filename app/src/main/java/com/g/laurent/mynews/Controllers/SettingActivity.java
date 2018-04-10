package com.g.laurent.mynews.Controllers;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.g.laurent.mynews.Models.Callback_list_subjects;
import com.g.laurent.mynews.R;

public class SettingActivity extends AppCompatActivity {

    private SettingFragment settingFragment;
    private NotifFragment notifFragment;
   // private static final String EXTRA_SETTING_TYPE = "setting_type";
    private String setting_type;
    //public Bundle bundle;
    public Callback_list_subjects callback_list_subjects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Intent intent = getIntent();

        setting_type = intent.getStringExtra(MainActivity.EXTRA_SETTING_TYPE);
        /*bundle = new Bundle();
        bundle.putString(EXTRA_SETTING_TYPE,setting_type);*/
        if(setting_type.equals("search")) {
            configureAndShowSettingFragment();
            callback_list_subjects= (Callback_list_subjects) settingFragment;
        }
        else if(setting_type.equals("notif")){
            configureAndShowNotifFragment();
            callback_list_subjects= (Callback_list_subjects) notifFragment;
        }

        configureToolbar(setting_type);
    }

    private void configureAndShowNotifFragment() {

        notifFragment = (NotifFragment) getSupportFragmentManager().findFragmentById(R.id.setting_activity_layout);

        if (notifFragment == null) {
            notifFragment = new NotifFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.setting_activity_layout, notifFragment)
                    .commit();
        }
    }

    private void configureAndShowSettingFragment(){

        settingFragment = (SettingFragment) getSupportFragmentManager().findFragmentById(R.id.setting_activity_layout);

        if (settingFragment == null) {
            settingFragment = new SettingFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.setting_activity_layout, settingFragment)
                    .commit();
        }
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

    public void update_list_subjects(View view) {

        String subject = null;

        // Find the relativelayout which is the parent of the view
        RelativeLayout relativeLayout = (RelativeLayout) view.getParent();

        // Find the textview related to the relativelayout
        for(int index = 0; index<((ViewGroup)relativeLayout).getChildCount(); index++) {
            View Child = ((ViewGroup) relativeLayout).getChildAt(index);

            if(Child instanceof TextView){
                TextView textview = (TextView) Child;
                subject = textview.getText().toString();
            }
        }

        // Send information to fragment settings
        if(((CheckBox) view).isChecked())
            callback_list_subjects.update_list_subjects_in_fragment("add",subject);
        else
            callback_list_subjects.update_list_subjects_in_fragment("remove",subject);
    }

}
