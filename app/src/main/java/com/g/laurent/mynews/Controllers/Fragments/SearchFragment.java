package com.g.laurent.mynews.Controllers.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.g.laurent.mynews.Controllers.Activities.WebActivity;
import com.g.laurent.mynews.Models.Article;
import com.g.laurent.mynews.Models.Callback_search;
import com.g.laurent.mynews.R;
import com.g.laurent.mynews.Utils.NewsStreams;
import com.g.laurent.mynews.Utils.Search.Doc;
import com.g.laurent.mynews.Utils.Search.ListArticles;
import com.g.laurent.mynews.Utils.Search.Multimedium;
import com.g.laurent.mynews.Views.ArticleAdapter;
import com.g.laurent.mynews.Views.GridViewAdapter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;


public class SearchFragment extends BaseFragment {


    private TextView begin_date_text;
    private TextView end_date_text;
    public static final String EXTRA_LINK = "linkaddress";
    ArrayList<Article> listArticles = new ArrayList<>();
    private String link_search;
    private Disposable disposable;
    private ArticleAdapter adapter;
    private Callback_search mCallback_search;

    public SearchFragment() {
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
        //mCallback_search=new Callback_search();
        configure_date_selectors(getResources().getString(R.string.begindate),begin_date);
        configure_date_selectors(getResources().getString(R.string.enddate),end_date);
        configure_search_areas();
        configure_checkboxes();
        return view;
    }

    // -------------- SETTINGS AREAS ------------------------------

    protected void configure_checkboxes(){
        grid_checkbox.setAdapter(new GridViewAdapter(getContext(),getResources().getStringArray(R.array.list_checkbox),null));
    }

    private void configure_search_areas(){

        mCalendarView.setVisibility(View.GONE);
        mLinearLayout.removeView(toggle_notif);
        mLinearLayout.removeView(line_separator);
        search_button.setEnabled(false);

        configure_search_button();
        //configure_checkboxes();
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

    private void configure_search_button(){

        enable_search_button(false);

        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save_settings("search");
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


    // -----------------------------------------------------------------------------
    // ------------ CONFIGURATION CALENDAR / DATES (begin & end) -------------------
    // -----------------------------------------------------------------------------

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
                    grid_checkbox.setVisibility(View.VISIBLE);
                    search_button.setVisibility(View.VISIBLE);
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
}
