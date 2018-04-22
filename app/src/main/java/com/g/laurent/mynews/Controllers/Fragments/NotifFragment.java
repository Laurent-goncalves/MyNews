package com.g.laurent.mynews.Controllers.Fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import com.g.laurent.mynews.Models.Callback_settings;
import com.g.laurent.mynews.Models.ListArticlesSearch;
import com.g.laurent.mynews.Models.Search_request;
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
        query=null;
        type = "notif";


        if(ListSubjects==null)
            System.out.println("eeee      ListSubjects est null");

        configure_edit_text(type);
        configure_checkboxes();
        configure_switch_button(type);
        configure_settings_areas();

        return view;
    }

    private void configure_settings_areas(){
        mCalendarView.setVisibility(View.GONE);
        mLinearLayout.removeView(search_button);
        mLinearLayout.removeView(date_areas);
    }

    private void configure_checkboxes(){

        ListSubjects = string_transform_to_list(sharedPreferences_Notif.getString("list_subjects_notif",null));

        if(ListSubjects!=null)
        grid_checkbox.setAdapter(new GridViewAdapter(getContext(),getResources().getStringArray(R.array.list_checkbox),
                ListSubjects.toArray(new String[ListSubjects.size()])));
        else {
            ListSubjects = new ArrayList<>();
            grid_checkbox.setAdapter(new GridViewAdapter(getContext(),getResources().getStringArray(R.array.list_checkbox),
                    null));
        }
    }

    private void configure_edit_text(String type){

        if(type.equals("notif"))
            query_area.setText(sharedPreferences_Notif.getString("query_notif", null));

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

    // ------------------------------------ DATA ----------------------------------

    @Override
    public void save_data() {

        // Save the settings of notification in sharedpreferrences
        save_settings("notif");
        // Recover data saved
        recover_data();

        if(enable_notif){

            Search_request search_request = new Search_request("notif",query,list_transform_to_String(ListSubjects),null,null);
            // Launch request with criteria and save the list of id of articles
            ListArticlesSearch listArticlesSearch = new ListArticlesSearch(getContext(),search_request,sharedPreferences_Notif,null);
        }
    }

    public void recover_data() {

        if(sharedPreferences_Notif!=null) {
            ListSubjects = string_transform_to_list(sharedPreferences_Notif.getString("list_subjects_notif", null));
            query = sharedPreferences_Notif.getString("query_notif", null);
            enable_notif = sharedPreferences_Notif.getBoolean("enable_notifications",false);
        } else {
            ListSubjects=null;
            query=null;
            enable_notif=false;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        recover_data();
        configure_edit_text(type);
        configure_checkboxes();
        configure_switch_button(type);
    }

}
