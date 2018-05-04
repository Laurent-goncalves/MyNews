package com.g.laurent.mynews.Controllers.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Switch;
import com.g.laurent.mynews.Models.Callback_list_subjects;
import com.g.laurent.mynews.R;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import butterknife.BindView;

public class BaseFragment extends Fragment implements Callback_list_subjects {

    @BindView(R.id.query_area) EditText query_area;
    @BindView(R.id.gridview_check_box) GridView grid_checkbox;
    @BindView(R.id.toggle_enabling_notif) Switch toggle_notif;
    @BindView(R.id.button_search) Button search_button;
    @BindView(R.id.begin_date_selector) View begin_date;
    @BindView(R.id.end_date_selector) View end_date;
    @BindView(R.id.listview_dates) LinearLayout date_areas;
    @BindView(R.id.calendar) CalendarView mCalendarView;
    @BindView(R.id.line_separator) View line_separator;
    @BindView(R.id.setting_fragment_layout) LinearLayout mLinearLayout;
    protected Calendar date_begin;
    protected Calendar date_end;
    protected String date_begin_str;
    protected String date_end_str;
    protected ArrayList<String> ListSubjects;
    protected String query;
    protected Boolean enable_notif;
    protected String type;
    protected SharedPreferences sharedPreferences_Search;
    protected SharedPreferences sharedPreferences_Notif;
    protected static final String EXTRA_NOTIF_SETTINGS = "NOTIFICATION_settings";
    protected static final String EXTRA_SEARCH_SETTINGS = "SEARCH_settings";
    protected static final String EXTRA_ENABLE_NOTIF = "enable_notifications";
    protected static final String EXTRA_QUERY_NOTIF = "query_notif";
    protected static final String EXTRA_SUBJECTS_NOTIF = "list_subjects_notif";
    protected static final String EXTRA_BEGIN_DATE = "begin_date_search";
    protected static final String EXTRA_END_DATE = "end_date_search";
    protected static final String EXTRA_QUERY_SEARCH = "query_search";
    protected static final String EXTRA_SUBJECTS_SEARCH = "list_subjects_search";

    public BaseFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.settings_fragment, container, false);

        // Assign variables
        sharedPreferences_Notif = getContext().getSharedPreferences(EXTRA_NOTIF_SETTINGS, Context.MODE_PRIVATE);
        sharedPreferences_Search = getContext().getSharedPreferences(EXTRA_SEARCH_SETTINGS, Context.MODE_PRIVATE);
        ListSubjects=new ArrayList<>();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    // ------------------------ DATE CHECKING FOR CALENDAR VIEW ------------------------------

    public String create_date_format_yyyymmdd(String date){

        // if the date is in format yyyy-mm-dd
        if(date.substring(4,5).equals("-")){
            String year = date.substring(0,4);
            String month = date.substring(5,7);
            String day = date.substring(8,10);

            return year.toUpperCase() + month.toUpperCase() + day.toUpperCase();

            // if the date is in format dd/mm/yyyy
        } else if (date.substring(2,3).equals("/")){

            String year = date.substring(6,10);
            String month = date.substring(3,5);
            String day = date.substring(0,2);

            return year.toUpperCase() + month.toUpperCase() + day.toUpperCase();

        } else
            return "";
    }

    protected String create_string_date(int year, int month, int dayOfMonth){

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

    public boolean is_the_date_ok(String type_date, int year, int month, int day){

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

    protected boolean IsStrictlyBefore(Calendar dateComp, Calendar dateRef) {
        return dateComp.get(Calendar.YEAR) < dateRef.get(Calendar.YEAR) || dateComp.get(Calendar.YEAR) == dateRef.get(Calendar.YEAR) && (dateComp.get(Calendar.MONTH) < dateRef.get(Calendar.MONTH) || dateComp.get(Calendar.MONTH) == dateRef.get(Calendar.MONTH) && dateComp.get(Calendar.DAY_OF_MONTH) < dateRef.get(Calendar.DAY_OF_MONTH));
    }

    protected boolean IsStrictlyAfter(Calendar dateComp, Calendar dateRef) {
        return dateComp.get(Calendar.YEAR) > dateRef.get(Calendar.YEAR) || dateComp.get(Calendar.YEAR) == dateRef.get(Calendar.YEAR) && (dateComp.get(Calendar.MONTH) > dateRef.get(Calendar.MONTH) || dateComp.get(Calendar.MONTH) == dateRef.get(Calendar.MONTH) && dateComp.get(Calendar.DAY_OF_MONTH) > dateRef.get(Calendar.DAY_OF_MONTH));
    }

    // ------------------------ SAVE SETTINGS (callback method)------------------------------

    protected void save_settings(String type) {

        switch(type){
            case "search":
                sharedPreferences_Search.edit().putString(EXTRA_QUERY_SEARCH,query).apply();
                sharedPreferences_Search.edit().putString(EXTRA_SUBJECTS_SEARCH,list_transform_to_String(ListSubjects)).apply();
                sharedPreferences_Search.edit().putString(EXTRA_BEGIN_DATE,date_begin_str).apply();
                sharedPreferences_Search.edit().putString(EXTRA_END_DATE,date_end_str).apply();
                break;

            case "notif":
                sharedPreferences_Notif.edit().putString(EXTRA_QUERY_NOTIF,query).apply();
                sharedPreferences_Notif.edit().putString(EXTRA_SUBJECTS_NOTIF,list_transform_to_String(ListSubjects)).apply();
                sharedPreferences_Notif.edit().putBoolean(EXTRA_ENABLE_NOTIF,enable_notif).apply();
                break;
        }
    }

    // ------------------------ UPDATE ListSubjects (callback method) ------------------------------

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
    }

    protected String list_transform_to_String(ArrayList<String> list){

        /* /////////////////  Transform an ArrayList into string      */
        StringBuilder list_subjects = new StringBuilder();

        // Build the list_subjects in a single String (each subject is separated by a ",")
        for(String subject:list) {
            list_subjects.append(subject);
            list_subjects.append(",");
        }

        // Remove the last ","
        if(list_subjects.length()>1){
            if(list_subjects.substring(list_subjects.length()-1,list_subjects.length()).equals(",")){
                list_subjects.deleteCharAt(list_subjects.length()-1);
            }
        }

        return list_subjects.toString();

    }

    protected ArrayList<String> string_transform_to_list(String list_string){
        /* /////////////////  Transform a list in string into ArrayList      */
        ArrayList<String> new_list_subjects = new ArrayList<>();
        String[] mlist_subjects;

        if(list_string!=null){
            mlist_subjects=list_string.split(",");

            Collections.addAll(new_list_subjects, mlist_subjects);

        } else {
            new_list_subjects=null;
        }
        return new_list_subjects;
    }

    // ------------------- GETTER & SETTER ----------------------------------------------

    public void setDate_begin(Calendar date_begin) {
        this.date_begin = date_begin;
    }

    public void setDate_end(Calendar date_end) {
        this.date_end = date_end;
    }

    public ArrayList<String> getListSubjects() {
        return ListSubjects;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

}