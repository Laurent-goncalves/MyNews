package com.g.laurent.mynews.Controllers.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.g.laurent.mynews.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public class WebFragment extends Fragment {

    private String link;
    private static String EXTRA_LINK = "linkaddress";
    @BindView(R.id.webview) WebView mWebView;

    public WebFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_web, container, false);
        ButterKnife.bind(this, view);
        link = getArguments().getString(EXTRA_LINK);
        mWebView.loadUrl(link);
        return view;
    }


}
