package com.g.laurent.mynews.Controllers.Fragments;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.g.laurent.mynews.Models.AlarmReceiver;
import com.g.laurent.mynews.Models.Article;
import com.g.laurent.mynews.Models.Callback_list_subjects;
import com.g.laurent.mynews.R;
import com.g.laurent.mynews.Utils.NewsStreams;
import com.g.laurent.mynews.Utils.Search.Doc;
import com.g.laurent.mynews.Utils.Search.ListArticles;
import com.g.laurent.mynews.Utils.Search.Multimedium;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

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
    protected ArrayList<Article> mlistArticles;
    protected Disposable disposable;

    public BaseFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.settings_fragment, container, false);
        sharedPreferences_Notif = getContext().getSharedPreferences("NOTIFICATION_settings", Context.MODE_PRIVATE);
        sharedPreferences_Search = getContext().getSharedPreferences("SEARCH_settings", Context.MODE_PRIVATE);

        return view;
    }

    protected String create_date_format_yyyymmdd(String date){

        if(date.substring(4,5).equals("-")){
            String year = date.substring(0,4);
            String month = date.substring(5,7);
            String day = date.substring(8,10);

            return year.toUpperCase() + month.toUpperCase() + day.toUpperCase();

        } else if (date.substring(2,3).equals("/")){ // 01/02/2018

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

    protected boolean is_the_date_ok(String type_date, int year, int month, int day){

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

    protected String[] getListCheckBoxOK(String liste){
        if(liste!=null)
            return liste.split(",");
        else
            return null;
    }

    protected void enable_or_not_search_button(){
        // if the query is not null, the list of subjects selected has at least one item and the button search is visible, the button button is enabled
        if(query!=null && ListSubjects!=null && search_button!=null) {

            if (!query.equals("") && ListSubjects.size() > 0 && search_button.getVisibility() == View.VISIBLE)
                enable_search_button(true);
            else
                enable_search_button(false);
        }
    }

    protected void enable_search_button(boolean enable){

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

    protected void save_settings(String type) {

        switch(type){

            case "search":

                sharedPreferences_Search.edit().putString("query",query).apply();
                sharedPreferences_Search.edit().putString("list_subjects",list_transform_to_String(ListSubjects)).apply();
                sharedPreferences_Search.edit().putString("begin_date",date_begin_str).apply();
                sharedPreferences_Search.edit().putString("end_date",date_end_str).apply();
                break;

            case "notif":

                sharedPreferences_Notif.edit().putString("query",query).apply();
                sharedPreferences_Notif.edit().putString("list_subjects",list_transform_to_String(ListSubjects)).apply();
                sharedPreferences_Notif.edit().putBoolean("enable_notifications",enable_notif).apply();
                break;
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
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    protected void launch_search_request(String query,String subject, String begin_date,String end_date){

        this.disposable = NewsStreams.streamFetchgetListArticles(query,subject, begin_date,end_date).subscribeWith(new DisposableObserver<ListArticles>() {

            @Override
            public void onNext(ListArticles listArticles) {

                Build_data_SearchArticles(listArticles);
                save_list_ID_articles_notif();
            }

            @Override
            public void onError(Throwable e) {
                Log.e("TAG","On Error"+Log.getStackTraceString(e));
            }

            @Override
            public void onComplete() {
                Log.e("TAG","On Complete !!");
            }
        });
    }

    protected void save_list_ID_articles_notif() {

        ArrayList<String> List_ID = new ArrayList<>();

        if(mlistArticles!=null) {
            for (Article article : mlistArticles)
                List_ID.add(article.getId());
        }

        sharedPreferences_Notif.edit().putString("list_subjects_notif",list_transform_to_String(List_ID)).apply();
    }

    protected void Build_data_SearchArticles(ListArticles listArticles) {

        mlistArticles = new ArrayList<>();

        if(listArticles!=null){
            if(listArticles.getResponse()!=null) {
                if(listArticles.getResponse().getDocs() != null){

                    List<Doc> mDoc = listArticles.getResponse().getDocs();

                    if (mDoc != null) {
                        for(Doc doc : mDoc) {
                            mlistArticles.add(new Article(getImageUrlSearch(doc),
                                    doc.getPubDate(),
                                    doc.getHeadline().getMain(),
                                    doc.getSectionName(),null,doc.getWebUrl(),doc.getId()));
                        }
                    }
                }
            }
        }
    }

    protected String getImageUrlSearch(Doc doc){

        if(doc!=null){
            if (doc.getMultimedia() !=null){
                List<Multimedium> multimediumList = doc.getMultimedia();

                for(Multimedium multimedium : multimediumList){
                    if(multimedium.getUrl()!=null && !multimedium.getUrl().equals("")) {
                        return multimedium.getUrl();
                    }
                }
            }
        }
        return null;
    }

    protected String list_transform_to_String(ArrayList<String> list){

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

        ArrayList<String> new_list_subjects = new ArrayList<>();
        String[] mlist_subjects;

        if(list_string!=null){
            mlist_subjects=list_string.split(",");

            for(int i = 0;i<=mlist_subjects.length-1;i++) {
                new_list_subjects.add(mlist_subjects[i]);
            }

        } else {
            new_list_subjects=null;
        }
        return new_list_subjects;
    }


}
