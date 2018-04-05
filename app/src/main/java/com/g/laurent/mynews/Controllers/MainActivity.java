package com.g.laurent.mynews.Controllers;

import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import com.g.laurent.mynews.R;


public class MainActivity extends AppCompatActivity {

    private MainFragment mainFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.configureAndShowMainFragment();
        this.configureToolBar();
        this.configureTabLayout();
    }



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
}
