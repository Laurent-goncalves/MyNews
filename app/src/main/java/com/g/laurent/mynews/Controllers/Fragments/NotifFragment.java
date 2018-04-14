package com.g.laurent.mynews.Controllers.Fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import com.g.laurent.mynews.Models.Callback_notif_acti;
import com.g.laurent.mynews.R;
import com.g.laurent.mynews.Views.GridViewAdapter;

import java.util.ArrayList;
import butterknife.ButterKnife;

public class NotifFragment extends BaseFragment implements Callback_notif_acti {

    public NotifFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        View view = inflater.inflate(R.layout.settings_fragment, container, false);
        ButterKnife.bind(this, view);
        ListSubjects=new ArrayList<>();
        query=null;
        type = "notif";
        configure_edit_text(type);
        configure_checkboxes(type);
        configure_switch_button(type);
        configure_settings_areas();
        return view;
    }

    private void configure_settings_areas(){
        mCalendarView.setVisibility(View.GONE);
        mLinearLayout.removeView(search_button);
        mLinearLayout.removeView(date_areas);
    }

    protected void configure_checkboxes(String type){

        String[] list_checkbox_OK;

        if(type.equals("notif") && mSharedPreferences!=null)
            list_checkbox_OK = getListCheckBoxOK(mSharedPreferences.getString("list_subjects", null));
        else
            list_checkbox_OK=null;

        grid_checkbox.setAdapter(new GridViewAdapter(getContext(),getResources().getStringArray(R.array.list_checkbox),list_checkbox_OK));
    }


    private void configure_edit_text(String type){

        if(type.equals("notif"))
            query_area.setText(mSharedPreferences.getString("query", null));

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

    private void configure_switch_button(String type){
        if(type.equals("notif"))
            toggle_notif.setChecked(mSharedPreferences.getBoolean("enable_notifications",false));

        enable_notif = toggle_notif.isChecked();

        toggle_notif.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton cb, boolean on){
                enable_notif=on;
            }
        });
    }

    private void save_settings_notification() {

        StringBuilder list_subjects = new StringBuilder();

        // Build the list_subjects in a single String (each subject is separated by a ",")
        for(String subject:ListSubjects) {
            list_subjects.append(subject);
            list_subjects.append(",");
        }

        // Remove the last ","
        if(list_subjects.length()>1){
            if(list_subjects.substring(list_subjects.length()-1,list_subjects.length()).equals(",")){
                list_subjects.deleteCharAt(list_subjects.length()-1);
            }
        }
        mSharedPreferences.edit().putString("query",query).apply();
        mSharedPreferences.edit().putString("list_subjects",list_subjects.toString()).apply();
        mSharedPreferences.edit().putBoolean("enable_notifications",enable_notif).apply();
    }

    @Override
    public void update_data_notification() {
        save_settings_notification();
    }

    @Override
    public void onResume() {
        super.onResume();

        configure_edit_text(type);
        configure_checkboxes(type);
        configure_switch_button(type);
    }
}
