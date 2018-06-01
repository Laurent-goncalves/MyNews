package com.g.laurent.mynews.Controllers.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.g.laurent.mynews.Models.Callback_list_subjects;
import com.g.laurent.mynews.Models.Callback_settings;
import com.g.laurent.mynews.R;
import butterknife.BindView;


public class BaseActivity extends AppCompatActivity {

    /** DESCRIPTION : In the BaseActivity, we define and configure the toolbar and
     *  the tablayout.
     *  **/

    @BindView(R.id.toolbar_menu_button) ImageButton mIcon_menu;
    @BindView(R.id.toolbar_menu_search) ImageButton mIcon_search;
    @BindView(R.id.toolbar_menu_notif) ImageButton mIcon_notif;
    @BindView(R.id.toolbar_title) TextView mToolbar_view;
    @BindView(R.id.relative_layout_toolbar) RelativeLayout mRelativeLayout;
    private static final String EXTRA_TYPE_SETTINGS = "type_of_settings";
    protected static final String SCREEN_SETTINGS_SEARCH = "search_settings";
    protected static final String SCREEN_SETTINGS_NOTIF = "notif_settings";
    protected static final String SCREEN_LIST_ARTICLES = "list_articles_viewpager";
    protected Callback_list_subjects callback_list_subjects;
    protected Callback_settings callback_save_settings;
    protected String tab_name;
    protected Toolbar toolbar;
    protected String title_toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected String[] recover_list_tabs() {
        return getResources().getStringArray(R.array.list_tab);
    }

    protected void configureToolbar(final String type_fragment){
        // Assign toolbar
        toolbar = findViewById(R.id.activity_main_toolbar);
        // Sets the Toolbar
        setSupportActionBar(toolbar);

        // Configure
        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    if (toolbar != null && mToolbar_view != null) {

                        if (getSupportActionBar() != null) {
                            switch (type_fragment) {

                                case SCREEN_LIST_ARTICLES:

                                    // configure icons
                                    mIcon_search.setVisibility(View.VISIBLE);
                                    mIcon_menu.setVisibility(View.VISIBLE);
                                    mIcon_notif.setVisibility(View.VISIBLE);
                                    title_toolbar = getResources().getString(R.string.app_name);
                                    mToolbar_view.setText(title_toolbar);
                                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                                    configure_popupmenu_icon_toolbar();
                                    configure_icon_search();
                                    break;

                                case SCREEN_SETTINGS_SEARCH:
                                    mIcon_search.setVisibility(View.GONE);
                                    mIcon_menu.setVisibility(View.GONE);
                                    mIcon_notif.setVisibility(View.GONE);
                                    title_toolbar = getResources().getString(R.string.Search_articles);
                                    mToolbar_view.setText(title_toolbar);
                                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

                                    break;

                                case SCREEN_SETTINGS_NOTIF:
                                    mIcon_search.setVisibility(View.GONE);
                                    mIcon_menu.setVisibility(View.GONE);
                                    mIcon_notif.setVisibility(View.GONE);
                                    title_toolbar = getResources().getString(R.string.Notifications);
                                    mToolbar_view.setText(title_toolbar);
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

    private void configure_icon_search(){

        mIcon_search.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),SettingActivity.class);
                intent.putExtra(EXTRA_TYPE_SETTINGS, SCREEN_SETTINGS_SEARCH);
                getApplicationContext().startActivity(intent);
            }
        });
    }

    private void configure_popupmenu_icon_toolbar(){

        mIcon_notif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(BaseActivity.this, mIcon_notif);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.menu_toolbar, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        Toast toast;
                        switch (item.getItemId()){
                            case R.id.notifications:
                                Intent intent = new Intent(getApplicationContext(),SettingActivity.class);
                                intent.putExtra(EXTRA_TYPE_SETTINGS, SCREEN_SETTINGS_NOTIF);
                                getApplicationContext().startActivity(intent);
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

    public void update_list_subjects(View view) {

        String subject = null;

        // Find the relativelayout which is the parent of the view
        RelativeLayout relativeLayout = (RelativeLayout) view.getParent();

        // Find the textview related to the relativelayout
        for(int index = 0; index<relativeLayout.getChildCount(); index++) {
            View Child = relativeLayout.getChildAt(index);

            // for each child of the relativelayout, check if it's an instance of textview
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

    public void setToolbar(Toolbar toolbar) {
        this.toolbar = toolbar;
    }
}
