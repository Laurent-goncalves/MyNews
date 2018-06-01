package com.g.laurent.mynews.Controllers.Activities;

import android.os.Bundle;
import com.g.laurent.mynews.Controllers.Fragments.NotifFragment;
import com.g.laurent.mynews.Controllers.Fragments.SearchFragment;
import com.g.laurent.mynews.R;
import butterknife.ButterKnife;

public class SettingActivity extends BaseActivity {

    private static final String EXTRA_TYPE_SETTINGS = "type_of_settings";
    private static final String EXTRA_SETTINGS_SEARCH = "search_settings";
    private static final String EXTRA_SETTINGS_NOTIF = "notif_settings";
    private static final String EXTRA_API_KEY = "api_key";
    private String api_key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);

        String type_settings = getIntent().getStringExtra(EXTRA_TYPE_SETTINGS);
        api_key = getApplicationContext().getResources().getString(R.string.APIkey);

        switch(type_settings){
            case EXTRA_SETTINGS_SEARCH:
                configureAndShowSearchFragment();
                configureToolbar(SCREEN_SETTINGS_SEARCH);
                break;
            case EXTRA_SETTINGS_NOTIF:
                configureAndShowNotifFragment();
                configureToolbar(SCREEN_SETTINGS_NOTIF);
                break;
            default:
                finish();
                break;
        }
    }

    public void configureAndShowNotifFragment(){

        NotifFragment notifFragment = new NotifFragment();
        callback_save_settings = notifFragment; // create callback for savings settings
        callback_list_subjects = notifFragment; // create callback for updating list of subjects
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_API_KEY,api_key);
        notifFragment.setArguments(bundle);

        //configure toolbar
        this.configureToolbar("Notifications");

        // configure and show the notifFragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.setting_activity_layout, notifFragment)
                .commit();
    }

    public void configureAndShowSearchFragment(){

        SearchFragment searchFragment = new SearchFragment();
        callback_list_subjects = searchFragment;
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_API_KEY,api_key);
        searchFragment.setArguments(bundle);

        //configure toolbar
        this.configureToolbar("Search Articles");

        //configure and show SearchFragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.setting_activity_layout, searchFragment)
                .commit();
    }

}
