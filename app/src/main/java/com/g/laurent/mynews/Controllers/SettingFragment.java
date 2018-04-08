package com.g.laurent.mynews.Controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import com.g.laurent.mynews.Models.Callback_list_subjects;
import com.g.laurent.mynews.R;
import com.g.laurent.mynews.Views.GridViewAdapter;
import java.util.ArrayList;
import java.util.Calendar;
import butterknife.BindView;
import butterknife.ButterKnife;


public class SettingFragment extends Fragment implements Callback_list_subjects {

    @BindView(R.id.query_area) EditText query_area;
    @BindView(R.id.gridview_check_box) GridView grid_checkbox;
    @BindView(R.id.toggle_enabling_notif) Switch toggle_notif;
    @BindView(R.id.button_search) Button search_button;
    @BindView(R.id.begin_date_selector) View begin_date;
    @BindView(R.id.end_date_selector) View end_date;
    @BindView(R.id.calendar) CalendarView mCalendarView;
    @BindView(R.id.line_separator) View line_separator;
    @BindView(R.id.setting_fragment_layout) LinearLayout mLinearLayout;
    public static final String EXTRA_SETTING_TYPE = "setting_type";
    private TextView begin_date_text;
    private TextView end_date_text;
    private String setting_type;

    private ArrayList<String> ListSubjects;
    private String query;
    private Calendar date_begin;
    private Calendar date_end;

    public SettingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.settings_fragment, container, false);
        ButterKnife.bind(this, view);

        if(this.getArguments()!=null)
            setting_type = this.getArguments().getString(EXTRA_SETTING_TYPE);
        else
            setting_type = "search";

        ListSubjects=new ArrayList<>();
        query=null;

        configure_date_selectors(getResources().getString(R.string.begindate),begin_date);
        configure_date_selectors(getResources().getString(R.string.enddate),end_date);
        configure_settings_areas();
        configure_search_button();
        configure_edit_text();
        configure_checkboxes();
        return view;
    }

    private void configure_settings_areas(){

        mCalendarView.setVisibility(View.GONE);
        switch(setting_type){

            case "search":
                mLinearLayout.removeView(toggle_notif);
                mLinearLayout.removeView(line_separator);
                search_button.setEnabled(false);
                break;

            case "notif":
                mLinearLayout.removeView(search_button);
                mLinearLayout.removeView(begin_date);
                mLinearLayout.removeView(end_date);
                break;
        }
    }

    private void configure_checkboxes(){
        grid_checkbox.setAdapter(new GridViewAdapter(getContext(),getResources().getStringArray(R.array.list_checkbox)));
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
    }

    private void configure_date_selectors(final String type_date, View date_view){

        TextView textView;

        for(int index=0; index<((ViewGroup)date_view).getChildCount(); index++) {
            View Child = ((ViewGroup)date_view).getChildAt(index);

            if(Child.getId()==R.id.text_date && Child instanceof TextView) {
                textView = (TextView) Child;
                textView.setText(type_date);
            }

            if(Child instanceof RelativeLayout) {

                for(int j=0; j<((ViewGroup) Child).getChildCount(); j++) {
                    final View SubChild = ((ViewGroup) Child).getChildAt(j);

                    if (SubChild.getId() == R.id.date_selected && SubChild instanceof TextView){
                        if(type_date.equals("Begin date"))
                            begin_date_text = (TextView) SubChild;
                        else
                            end_date_text = (TextView) SubChild;
                    }

                    if (SubChild.getId() == R.id.icon_expand && SubChild instanceof ImageView) {

                        SubChild.setOnClickListener(new View.OnClickListener() {
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

        mCalendarView.setVisibility(View.VISIBLE);

        mCalendarView.setOnDateChangeListener((new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                if(is_the_date_ok(type_date,year,month,dayOfMonth)){
                    date_textview.setText(create_string_date(year, month, dayOfMonth));

                    if(type_date.equals("Begin date"))
                        update_calendar(type_date, year, month, dayOfMonth);
                    else
                        update_calendar(type_date, year, month, dayOfMonth);

                    mCalendarView.setVisibility(View.GONE);
                }
            }
        }));
    }

    private void configure_search_button(){

        enable_search_button(false);

        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WebActivity.class);
                startActivity(intent);
            }
        });
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

    private void update_calendar(String type_date, int year, int month, int dayOfMonth){

        switch(type_date){

            case "Begin date":
                if(date_begin==null)
                    date_begin = Calendar.getInstance();

                date_begin.set(Calendar.YEAR, year);
                date_begin.set(Calendar.MONTH, month);
                date_begin.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                break;
            case "End date":
                if(date_end==null)
                    date_end = Calendar.getInstance();

                date_end.set(Calendar.YEAR, year);
                date_end.set(Calendar.MONTH, month);
                date_end.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                break;

        }



    }

    private String create_string_date(int year, int month, int dayOfMonth){

        String Day;
        int Month = month + 1;
        String new_month;

        if(dayOfMonth<10)
            Day = "0" + dayOfMonth;
        else
            Day = String.valueOf(dayOfMonth);

        if(Month<10)
            new_month = "0" + Month;
        else
            new_month = String.valueOf(Month);

        return Day + "/" + new_month + "/" + year;
    }

    private boolean is_the_date_ok(String type_date, int year, int month, int day){

        boolean answer = true;

        Calendar dateToValidate = Calendar.getInstance();
        dateToValidate.set(Calendar.YEAR, year);
        dateToValidate.set(Calendar.MONTH, month);
        dateToValidate.set(Calendar.DAY_OF_MONTH, day);

        switch(type_date){

            case "Begin date":

                // Check if begin date is before today's date and begin date before end date
                if(IsStrictlyAfter(dateToValidate,Calendar.getInstance()))
                    answer = false;

                if(date_end!=null) {
                    if (IsStrictlyAfter(dateToValidate, date_end))
                        answer = false;
                }
                break;

            case "End date":

                // Check if end date is before today's date and end date after begin date
                if(IsStrictlyAfter(dateToValidate,Calendar.getInstance()))
                    answer = false;

                if(date_begin!=null) {
                    if (IsStrictlyBefore(dateToValidate,date_begin))
                        answer = false;
                }
                break;
        }
        return answer;
    }

    public boolean IsStrictlyBefore(Calendar dateComp, Calendar dateRef){

        if (dateComp.get(Calendar.YEAR) < dateRef.get(Calendar.YEAR))
            return true;
        else if (dateComp.get(Calendar.YEAR) == dateRef.get(Calendar.YEAR)){
            if (dateComp.get(Calendar.MONTH) < dateRef.get(Calendar.MONTH))
                return true;
            else if (dateComp.get(Calendar.MONTH) == dateRef.get(Calendar.MONTH)) {
                if (dateComp.get(Calendar.DAY_OF_MONTH) < dateRef.get(Calendar.DAY_OF_MONTH))
                    return true;
                else
                    return false;
            }
            else
                return false;
        } else
            return false;
    }

    public boolean IsStrictlyAfter(Calendar dateComp, Calendar dateRef){

        if (dateComp.get(Calendar.YEAR) > dateRef.get(Calendar.YEAR))
            return true;
        else if (dateComp.get(Calendar.YEAR) == dateRef.get(Calendar.YEAR)){
            if (dateComp.get(Calendar.MONTH) > dateRef.get(Calendar.MONTH))
                return true;
            else if (dateComp.get(Calendar.MONTH) == dateRef.get(Calendar.MONTH)) {
                if (dateComp.get(Calendar.DAY_OF_MONTH) > dateRef.get(Calendar.DAY_OF_MONTH))
                    return true;
                else
                    return false;
            }
            else
                return false;
        } else
            return false;

    }

    private void enable_or_not_search_button(){
        // if the query is not null, the list of subjects selected has at least one item and the button search is visible, the button button is enabled
        if(query!=null && ListSubjects!=null && search_button!=null) {

            if (!query.equals("") && ListSubjects.size() > 0 && search_button.getVisibility() == View.VISIBLE)
                enable_search_button(true);
            else
                enable_search_button(false);
        }
    }

    @Override
    public void update_list_subjects_in_fragment(String type_modif, String subject) {

        if(subject!=null){
            switch(type_modif){
                case "add":
                    ListSubjects.add(subject);
                    break;
                case "remove":
                    ListSubjects.remove(subject);
                    break;
            }
        }
        enable_or_not_search_button();
    }

}

