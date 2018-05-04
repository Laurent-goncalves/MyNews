package com.g.laurent.mynews.Controllers.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
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

    private static final String EXTRA_SAVING_TYPE = "notif";

    public NotifFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        View view = inflater.inflate(R.layout.settings_fragment, container, false);
        ButterKnife.bind(this, view);
        type = EXTRA_SAVING_TYPE;
        recover_data();
        configure_edit_text();
        configure_checkboxes();
        configure_switch_button();
        configure_settings_areas();
        return view;
    }

    // ------------------------------ CONFIGURATIONS VIEWS -------------------------------------------

    private void configure_settings_areas(){
        mCalendarView.setVisibility(View.GONE);
        mLinearLayout.removeView(search_button);
        mLinearLayout.removeView(date_areas);
    }

    private void configure_checkboxes(){

        if(ListSubjects!=null)
        grid_checkbox.setAdapter(new GridViewAdapter(getContext(),getResources().getStringArray(R.array.list_checkbox),
                ListSubjects.toArray(new String[ListSubjects.size()])));
        else {
            ListSubjects = new ArrayList<>();
            grid_checkbox.setAdapter(new GridViewAdapter(getContext(),getResources().getStringArray(R.array.list_checkbox),
                    null));
        }
    }

    private void configure_edit_text(){
        query_area.setText(query);
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
                enable_or_not_notification_switch(false,false);
            }
        });
    }

    private void configure_switch_button(){
        toggle_notif.setChecked(enable_notif);
        toggle_notif.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton cb, boolean on){
                enable_or_not_notification_switch(true,on);
            }
        });
    }

    private Boolean allow_switch_button_on(){

        Boolean allow = false;

        // if at least one subject is selected and a word is indicated in edittext
        if(ListSubjects!=null && query!=null){
            if(ListSubjects.size() >= 1 && !query.equals(""))
                allow = true;
        } else
            allow = false;

        return allow;
    }

    private void enable_or_not_notification_switch(Boolean click_switch, Boolean state){

        if(click_switch){ // if the user clicks on switch button
            enable_notif = state && allow_switch_button_on(); // if the user wants to turn on the switch and it is allowed to do it,...
            toggle_notif.setChecked(enable_notif); // set the switch to the good state
        } else { // if the user didn't click on switch button
            if (!allow_switch_button_on()) {
                enable_notif = false;
                toggle_notif.setChecked(false);
            }
        }
    }

    // --------------- METHOD FROM CALLBACK --------------------------
    // --------------- to update ListSubjects -------------------------

    @Override
    public void update_list_subjects_in_fragment(String type_modif, String subject) {
        super.update_list_subjects_in_fragment(type_modif, subject);
        enable_or_not_notification_switch(false,false);

    }

    // ------------------------------------ DATA saving & recovering ----------------------------------

    @Override
    public void save_data() {

        // Save the settings of notification in sharedpreferrences
        save_settings(EXTRA_SAVING_TYPE);

        // Recover data saved
        recover_data();

        if(enable_notif){
            Search_request search_request = new Search_request(EXTRA_SAVING_TYPE,query,list_transform_to_String(ListSubjects),null,null);
            // Launch request with criteria and save the list of id of articles
            new ListArticlesSearch(getContext(),search_request,sharedPreferences_Notif,null);
        }
    }

    public void recover_data() {

        if(sharedPreferences_Notif!=null) {
            ListSubjects = string_transform_to_list(sharedPreferences_Notif.getString(EXTRA_SUBJECTS_NOTIF, null));
            query = sharedPreferences_Notif.getString(EXTRA_QUERY_NOTIF, null);
            enable_notif = sharedPreferences_Notif.getBoolean(EXTRA_ENABLE_NOTIF,false);
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
        configure_edit_text();
        configure_checkboxes();
        configure_switch_button();
    }

}
