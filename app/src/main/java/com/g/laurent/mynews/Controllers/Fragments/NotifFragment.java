package com.g.laurent.mynews.Controllers.Fragments;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import com.g.laurent.mynews.Models.Callback_settings;
import com.g.laurent.mynews.R;
import com.g.laurent.mynews.Views.GridViewAdapter;

import java.util.ArrayList;
import butterknife.ButterKnife;

public class NotifFragment extends BaseFragment implements Callback_settings {

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

        if(type.equals("notif") && sharedPreferences_Notif!=null)
            list_checkbox_OK = getListCheckBoxOK(sharedPreferences_Notif.getString("list_subjects", null));
        else
            list_checkbox_OK=null;

        grid_checkbox.setAdapter(new GridViewAdapter(getContext(),getResources().getStringArray(R.array.list_checkbox),list_checkbox_OK));
    }


    private void configure_edit_text(String type){

        if(type.equals("notif"))
            query_area.setText(sharedPreferences_Notif.getString("query", null));

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
            toggle_notif.setChecked(sharedPreferences_Notif.getBoolean("enable_notifications",false));

        enable_notif = toggle_notif.isChecked();

        toggle_notif.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton cb, boolean on){
                enable_notif=on;
            }
        });
    }



    @Override
    public void save_data() {
        // Save the settings of notification in sharedpreferrences
        save_settings("notif");
        // create or update the notification builder

        /*NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("New article corresponding to your criteria of interest!")
                .setContentText("article title")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);*/

        // Launch request with criteria and save the list of id of articles

        // Set an alarm. When the alarm rings, a new request is sent.

        // The list of id of the new request is compared to the old one. if there is new id, a notification is sent.


    }

    @Override
    public void onResume() {
        super.onResume();

        configure_edit_text(type);
        configure_checkboxes(type);
        configure_switch_button(type);
    }
}
