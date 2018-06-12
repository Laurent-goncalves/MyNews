package com.g.laurent.mynews.Controllers.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.g.laurent.mynews.Models.Callback_search;
import com.g.laurent.mynews.Models.Callback_settings;
import com.g.laurent.mynews.R;
import com.g.laurent.mynews.Views.GridViewAdapter;
import java.util.ArrayList;
import java.util.Calendar;
import butterknife.ButterKnife;

public class SearchFragment extends BaseFragment implements Callback_settings {

    private TextView begin_date_text;
    private TextView end_date_text;
    private Callback_search mCallback_search;
    private static final String EXTRA_SAVING_TYPE = "search";

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        View view = inflater.inflate(R.layout.settings_fragment, container, false);
        ButterKnife.bind(this, view);
        ListSubjects=new ArrayList<>();
        query=null;

        // configures views
        configure_date_selectors(getResources().getString(R.string.begindate),begin_date);
        configure_date_selectors(getResources().getString(R.string.enddate),end_date);
        configure_search_areas();
        configure_checkboxes();
        return view;
    }

    // -------------- SETTINGS AREAS ------------------------------

    private void configure_checkboxes(){

        if(ListSubjects!=null)
            grid_checkbox.setAdapter(new GridViewAdapter(getContext(),getResources().getStringArray(R.array.list_checkbox),
                    ListSubjects.toArray(new String[ListSubjects.size()])));
        else {
            ListSubjects = new ArrayList<>();
            grid_checkbox.setAdapter(new GridViewAdapter(getContext(),getResources().getStringArray(R.array.list_checkbox),
                    null));
        }


        //grid_checkbox.setAdapter(new GridViewAdapter(getContext(),getResources().getStringArray(R.array.list_checkbox),null));
    }

    private void configure_search_areas(){

        mCalendarView.setVisibility(View.GONE);
        mLinearLayout.removeView(toggle_notif);
        mLinearLayout.removeView(line_separator);
        search_button.setEnabled(false);

        configure_search_button();
        configure_checkboxes();
        configure_edit_text();
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
                enable_or_not_search_button();
            }
        });

        query_area.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        });
    }

    // --------------- METHOD FROM CALLBACK --------------------------
    // --------------- to update ListSubjects -------------------------

    @Override
    public void update_list_subjects_in_fragment(String type_modif, String subject) {
        super.update_list_subjects_in_fragment(type_modif, subject);
        enable_or_not_search_button();
    }


    // ------------------------------------------------------------
    // ------------ CONFIGURATION SEARCH BUTTON -------------------
    // ------------------------------------------------------------

    private void enable_or_not_search_button(){
        // if the query is not null, the list of subjects selected has at least one item and the button search is visible, the button button is enabled
        if(query!=null && ListSubjects!=null && search_button!=null) {

            if (!query.equals("") && ListSubjects.size() > 0 && search_button.getVisibility() == View.VISIBLE)
                enable_search_button(true);
            else
                enable_search_button(false);
        }
    }

    private void enable_search_button(boolean enable){

        if(enable){
            search_button.setEnabled(true);
            search_button.setAlpha(1f);
            search_button.setClickable(true);
        } else {
            search_button.setEnabled(false);
            search_button.setAlpha(0.3f);
            search_button.setClickable(false);
        }
    }

    private void configure_search_button(){

        enable_search_button(false);

        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save_settings(EXTRA_SAVING_TYPE);
                mCallback_search.configureAndShowMainFragmentSearchRequest();
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback_search = (Callback_search) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnArticleSelectedListener");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        configure_edit_text();
        configure_checkboxes();
    }

    // -----------------------------------------------------------------------------
    // ------------ CONFIGURATION CALENDAR / DATES (begin & end) -------------------
    // -----------------------------------------------------------------------------

    private void configure_date_selectors(final String type_date, View date_view){

        TextView textView;

        // for each view child of the date_view (begin view or end view)
        for(int index=0; index<((ViewGroup)date_view).getChildCount(); index++) {
            View Child = ((ViewGroup)date_view).getChildAt(index);

            // define the one which is an instance of TextView and add the text to display ("Begin date" or "End date")
            if(Child.getId()==R.id.text_date && Child instanceof TextView) {
                textView = (TextView) Child;
                textView.setText(type_date);
            }

            // if the child is an instance of relativeLayout
            if(Child instanceof RelativeLayout) {

                for(int j=0; j<((ViewGroup) Child).getChildCount(); j++) {
                    final View SubChild = ((ViewGroup) Child).getChildAt(j);

                    // if the subchild is a textview (the date selected)
                    if (SubChild.getId() == R.id.date_selected && SubChild instanceof TextView){
                        if(type_date.equals("Begin date"))
                            begin_date_text = (TextView) SubChild;
                        else
                            end_date_text = (TextView) SubChild;
                    }

                    // if the subchild is the icon to expand the calendarView
                    if (SubChild.getId() == R.id.icon_expand && SubChild instanceof ImageView) {

                        SubChild.setOnClickListener(new View.OnClickListener() { // add an OnClickListener
                            @Override
                            public void onClick(View v) {

                                if(mCalendarView.getVisibility()==View.VISIBLE) {
                                    mCalendarView.setVisibility(View.GONE);
                                    grid_checkbox.setVisibility(View.VISIBLE);
                                    search_button.setVisibility(View.VISIBLE);
                                }
                                else {

                                    grid_checkbox.setVisibility(View.GONE);
                                    search_button.setVisibility(View.GONE);

                                    if(type_date.equals("Begin date"))
                                        configure_and_show_calendarView(begin_date_text, type_date);
                                    else
                                        configure_and_show_calendarView(end_date_text, type_date);
                                }

                            }
                        });
                    }
                }
            }
        }
    }

    private void configure_and_show_calendarView(final TextView date_textview, final String type_date) {

        mCalendarView.setVisibility(View.VISIBLE); // show calendarView

        mCalendarView.setOnDateChangeListener((new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                if(is_the_date_ok(type_date,year,month,dayOfMonth)){
                    date_textview.setText(create_string_date(year, month, dayOfMonth)); // change date selected into string

                    if(type_date.equals("Begin date")) // update views with the date selected
                        update_calendar(type_date, year, month, dayOfMonth);
                    else
                        update_calendar(type_date, year, month, dayOfMonth);

                    mCalendarView.setVisibility(View.GONE); // hide calendar view
                    grid_checkbox.setVisibility(View.VISIBLE); // show checkboxes
                    search_button.setVisibility(View.VISIBLE); // show search button
                }
            }
        }));
    }

    private void update_calendar(String type_date, int year, int month, int dayOfMonth){

        // Define and show the date selected in the calendar
        switch(type_date){

            case "Begin date":
                if(date_begin==null)
                    date_begin = Calendar.getInstance();

                date_begin.set(Calendar.YEAR, year);
                date_begin.set(Calendar.MONTH, month);
                date_begin.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                date_begin_str=create_date_format_yyyymmdd(create_string_date(year, month, dayOfMonth));
                break;
            case "End date":
                if(date_end==null)
                    date_end = Calendar.getInstance();

                date_end.set(Calendar.YEAR, year);
                date_end.set(Calendar.MONTH, month);
                date_end.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                date_end_str=create_date_format_yyyymmdd(create_string_date(year, month, dayOfMonth));
                break;

        }
    }

    @Override
    public void save_data() {

    }
}

