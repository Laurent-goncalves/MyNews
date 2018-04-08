package com.g.laurent.mynews.Controllers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.g.laurent.mynews.R;

public class WebActivity extends AppCompatActivity {

    private WebFragment webFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        configureAndShowWebFragment();
    }

    private void configureAndShowWebFragment(){

        webFragment = (WebFragment) getSupportFragmentManager().findFragmentById(R.id.activity_web_frame_layout);
        if (webFragment == null) {
            webFragment = new WebFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.activity_web_frame_layout, webFragment)
                    .commit();
        }
    }
}
