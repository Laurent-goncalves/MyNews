package com.g.laurent.mynews.Controllers.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;

import com.g.laurent.mynews.Controllers.Fragments.WebFragment;
import com.g.laurent.mynews.R;

public class WebActivity extends AppCompatActivity {

    private WebFragment webFragment;
    private String link;
    public static final String EXTRA_LINK = "linkaddress";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        link=getIntent().getStringExtra(EXTRA_LINK);
        configureAndShowWebFragment();
    }

    @Override
    public void onBackPressed() {
        System.out.println("eeee onBackPressed");
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if ( id == android.R.id.home ) {
            System.out.println("eeee onOptionsItemSelected");
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void configureAndShowWebFragment(){

        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_LINK,link);
        webFragment = (WebFragment) getSupportFragmentManager().findFragmentById(R.id.activity_web_frame_layout);
        if (webFragment == null) {
            webFragment = new WebFragment();
            webFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.activity_web_frame_layout, webFragment)
                    .commit();
        }
    }
}
