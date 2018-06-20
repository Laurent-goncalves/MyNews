package com.g.laurent.mynews.Views;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import com.g.laurent.mynews.Controllers.Fragments.PageFragment;
import com.g.laurent.mynews.R;

public class PageAdapter extends android.support.v4.app.FragmentStatePagerAdapter {

    private static final String EXTRA_TAB_TOP_STORIES = "tab_top_stories";
    private static final String EXTRA_TAB_MOST_POPULAR = "tab_most_popular";
    private static final String EXTRA_SETTING_ACTIVITY_TYPE = "type_setting_activity";
    private String api_key;
    private Context context;

    public PageAdapter(FragmentManager fm, String api_key, Context context) {
        super(fm);
        this.api_key=api_key;
        this.context=context;
    }

    @Override
    public PageFragment getItem(int position) {
        switch (position){
            case 0: //Page number 1
                return PageFragment.newInstance(EXTRA_TAB_TOP_STORIES,api_key,EXTRA_SETTING_ACTIVITY_TYPE);
            case 1: //Page number 2
                return PageFragment.newInstance(EXTRA_TAB_MOST_POPULAR,api_key,EXTRA_SETTING_ACTIVITY_TYPE);
            case 2: //Page number 3
                return PageFragment.newInstance(context.getResources().getString(R.string.interest_tab),api_key,EXTRA_SETTING_ACTIVITY_TYPE);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0: //Page number 1
                return "TOP STORIES";
            case 1: //Page number 2
                return "MOST POPULAR";
            case 2: //Page number 3
                return context.getResources().getString(R.string.interest_tab).toUpperCase();
            default:
                return null;
        }
    }

}

