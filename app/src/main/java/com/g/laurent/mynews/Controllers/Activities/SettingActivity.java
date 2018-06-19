package com.g.laurent.mynews.Controllers.Activities;

import android.os.Bundle;
import android.view.MenuItem;
import com.g.laurent.mynews.Controllers.Fragments.NotifFragment;
import com.g.laurent.mynews.Controllers.Fragments.PageFragment;
import com.g.laurent.mynews.Controllers.Fragments.SearchFragment;
import com.g.laurent.mynews.Models.Callback_search;
import com.g.laurent.mynews.R;
import butterknife.ButterKnife;

public class SettingActivity extends BaseActivity implements Callback_search {

    /** DESCRIPTION : The SettingActivity which extends BaseActivity will be used to integrate :
     *       - the SearchFragment with the different criteria of search
     *       - The PageFragment with the result of the search request
     *       - the NotifFragment with the settings for sending notifications **/

    private static final String EXTRA_TYPE_SETTINGS = "type_of_settings";
    private static final String EXTRA_SETTINGS_SEARCH = "search_settings";
    private static final String EXTRA_SETTINGS_NOTIF = "notif_settings";
    private static final String EXTRA_API_KEY = "api_key";
    private static final String EXTRA_TAB_SEARCH = "tab_search";
    private static final String EXTRA_SETTING_ACTIVITY_TYPE = "type_setting_activity";
    private String api_key;
    private String type_settings;
    private SearchFragment searchFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);

        type_settings = getIntent().getStringExtra(EXTRA_TYPE_SETTINGS);
        api_key = getApplicationContext().getResources().getString(R.string.APIkey);

        if(type_settings!=null){

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
    }

    public void configureAndShowNotifFragment(){

        NotifFragment notifFragment = new NotifFragment();
        callback_save_settings = notifFragment; // create callback for savings settings
        callback_list_subjects = notifFragment; // create callback for updating list of subjects
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_API_KEY,api_key);
        notifFragment.setArguments(bundle);

        // configure and show the notifFragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.setting_activity_layout, notifFragment)
                .commit();
    }

    public void configureAndShowSearchFragment(){

        searchFragment = new SearchFragment();
        callback_list_subjects = searchFragment;
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_API_KEY,api_key);
        searchFragment.setArguments(bundle);

        //configure and show SearchFragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.setting_activity_layout, searchFragment)
                .commit();
    }

    @Override
    public void configureAndShowMainFragmentSearchRequest(){
        // configure and show the PageFragment
        PageFragment mPageFragment = PageFragment.newInstance(EXTRA_TAB_SEARCH,api_key,EXTRA_SETTING_ACTIVITY_TYPE);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.setting_activity_layout, mPageFragment)
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home) {
            if(type_settings.equals(EXTRA_SETTINGS_NOTIF))
                callback_save_settings.save_data();

            finish();
        }
        return true;
    }

    public SearchFragment getSearchFragment() {
        return searchFragment;
    }
}
