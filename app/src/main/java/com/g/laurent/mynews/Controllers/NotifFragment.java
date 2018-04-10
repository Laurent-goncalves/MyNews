package com.g.laurent.mynews.Controllers;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.g.laurent.mynews.R;
import com.g.laurent.mynews.Views.GridViewAdapter;

import java.util.ArrayList;

import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotifFragment extends BaseSettingFragment {


    public NotifFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings_fragment, container, false);
        ButterKnife.bind(this, view);
        ListSubjects=new ArrayList<>();
        query=null;

        configure_edit_text();
        configure_checkboxes();
        configure_settings_areas();
        return view;
    }

    private void configure_settings_areas(){
        mCalendarView.setVisibility(View.GONE);
        mLinearLayout.removeView(search_button);
        mLinearLayout.removeView(begin_date);
        mLinearLayout.removeView(end_date);
    }

    private void configure_edit_text(){

        query_area.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                query = s.toString();
            }
        });
    }

    protected void configure_checkboxes(){
        grid_checkbox.setAdapter(new GridViewAdapter(getContext(),getResources().getStringArray(R.array.list_checkbox)));
    }
}
