package com.g.laurent.mynews.Controllers;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.view.menu.ListMenuItemView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.g.laurent.mynews.R;
import com.g.laurent.mynews.Views.GridViewAdapter;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends Fragment {

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
    private Calendar date_begin;
    private Calendar date_end;
    private String setting_type;

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

        configure_date_selectors(getResources().getString(R.string.begindate),begin_date);
        configure_date_selectors(getResources().getString(R.string.enddate),end_date);
        configure_settings_areas();
        configure_checkboxes();
        return view;
    }

    private void configure_settings_areas(){

        mCalendarView.setVisibility(View.GONE);

        switch(setting_type){

            case "search":
                mLinearLayout.removeView(toggle_notif);
                mLinearLayout.removeView(line_separator);
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
}

