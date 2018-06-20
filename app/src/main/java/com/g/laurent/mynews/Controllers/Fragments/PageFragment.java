package com.g.laurent.mynews.Controllers.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.g.laurent.mynews.Controllers.Activities.MainActivity;
import com.g.laurent.mynews.Controllers.Activities.SettingActivity;
import com.g.laurent.mynews.Models.Article;
import com.g.laurent.mynews.Models.CallbackPageFragment;
import com.g.laurent.mynews.Models.ListArticlesMostPopular;
import com.g.laurent.mynews.Models.ListArticlesSearch;
import com.g.laurent.mynews.Models.ListArticlesTopStories;
import com.g.laurent.mynews.Models.Search_request;
import com.g.laurent.mynews.R;
import com.g.laurent.mynews.Views.ArticleAdapter;
import java.util.ArrayList;
import java.util.Objects;
import butterknife.BindView;
import butterknife.ButterKnife;

public class PageFragment extends Fragment implements CallbackPageFragment {

    @BindView(R.id.fragment_recycler_view) RecyclerView recyclerView;
    @BindView(R.id.progressBar) ProgressBar mProgressBar;
    private ArticleAdapter mArticleAdapter;
    private SharedPreferences sharedPreferences_Search;

    // TYPE OF ACTIVITY
    private static final String EXTRA_ACTIVITY_TYPE = "type_activity";
    public static final String EXTRA_TAB_NAME = "tab_name";
    public static final String EXTRA_TAB_TOP_STORIES = "tab_top_stories";
    public static final String EXTRA_TAB_MOST_POPULAR = "tab_most_popular";
    public static final String EXTRA_QUERY = "query";
    public static final String EXTRA_SUBJECT = "subject";
    public static final String EXTRA_BEGIN = "begin_date";
    public static final String EXTRA_END = "end_date";

    // SETTINGS FROM SEARCH FRAGMENT
    private static final String EXTRA_BEGIN_DATE = "begin_date_search";
    private static final String EXTRA_END_DATE = "end_date_search";
    private static final String EXTRA_QUERY_SEARCH = "query_search";
    private static final String EXTRA_SUBJECTS_SEARCH = "list_subjects_search";
    private static final String EXTRA_SEARCH_SETTINGS = "SEARCH_settings";

    // API key
    private static final String EXTRA_API_KEY = "api_key";


    private ListArticlesTopStories listArticlesTopStories;
    private ListArticlesMostPopular listArticlesMostPopular;
    private ListArticlesSearch listArticlesSearch;
    private CallbackPageFragment mCallbackPageFragment;
    private ArrayList<Article> listarticles;

    private String tab_name;
    private String query;
    private String subject;
    private String begin_date;
    private String end_date;
    private MainActivity mMainActivity;
    private SettingActivity mSettingActivity;
    private Context context;
    private String api_key;
    private String tab_interest ;

    public PageFragment() {} // Required empty public constructor

    public static PageFragment newInstance(String tab_name, String api_key, String activity_type) {

        // Create new fragment
        PageFragment frag = new PageFragment();

        // Create bundle and add it some data
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_TAB_NAME, tab_name);
        bundle.putString(EXTRA_API_KEY, api_key);
        bundle.putString(EXTRA_ACTIVITY_TYPE, activity_type);
        frag.setArguments(bundle);

        return(frag);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);

        // Inflate the layout for this fragment and assign views
        View view = inflater.inflate(R.layout.fragment_recycler, container, false);
        ButterKnife.bind(this, view);

        // Assign and initialize variables
        mCallbackPageFragment = this;
        context = getContext();
        if(mProgressBar!=null)
        mProgressBar.setVisibility(View.VISIBLE);
        tab_interest = getResources().getString(R.string.interest_tab);

        // Recover the tab_name and the API key
        if(getArguments()!=null){

            api_key = getArguments().getString(EXTRA_API_KEY,null);
            tab_name = getArguments().getString(EXTRA_TAB_NAME);

            // Launch configuration of recyclerView
            if(context instanceof MainActivity) {
                start_configure_recyclerView_mainActivity();
            } else {
                start_configure_recyclerView_settingActivity();
            }
        }
        return view;
    }

    // ---------------------------------------------------------------------------------------------
    // ----------------------------------------- MainActivity --------------------------------------
    // ---------------------------------------------------------------------------------------------

    private void start_configure_recyclerView_mainActivity(){

        if(tab_name.equals(tab_interest)){ // if user has clicked on tab interest (travel here)

            // Assign variables
            query = getResources().getString(R.string.query_interest);
            subject = null;
            begin_date = null;
            end_date = null;

        } else { // if user has clicked on Most Popular tab or Top Stories tab

            // Assign variables
            query = Objects.requireNonNull(getArguments()).getString(EXTRA_QUERY); // recover the "section"
            subject = define_subject(getArguments().getString(EXTRA_SUBJECT)); // recover subjects

            if(subject==null)
                subject = "politics";

            begin_date = getArguments().getString(EXTRA_BEGIN);
            end_date = getArguments().getString(EXTRA_END);
        }

        configure_articles_mainActivity(tab_name);
    }

    public void configure_articles_mainActivity(String tab_name) {

        if(api_key!=null){

            switch(tab_name){
                case EXTRA_TAB_TOP_STORIES:
                    listArticlesTopStories = new ListArticlesTopStories(context,api_key,subject, mCallbackPageFragment);
                    break;
                case EXTRA_TAB_MOST_POPULAR:
                    listArticlesMostPopular = new ListArticlesMostPopular(context, api_key, subject, mCallbackPageFragment);
                    break;
                default: // if tab interest
                    Search_request search_request = new Search_request("search",query);
                    listArticlesSearch = new ListArticlesSearch(context, api_key, search_request, mCallbackPageFragment);
                    break;
            }
        }
    }

    @Override
    public void finish_configure_recyclerView_mainActivity() {

        mMainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try{
                    switch(tab_name){
                        case EXTRA_TAB_TOP_STORIES:
                            listarticles = listArticlesTopStories.getListArticles();
                            break;
                        case EXTRA_TAB_MOST_POPULAR:
                            listarticles = listArticlesMostPopular.getListArticles();
                            break;
                        default: // if interest tab
                            listarticles = listArticlesSearch.getListArticles();
                            break;
                    }
                    configureRecyclerView();

                } catch (final Throwable error){
                    Toast toast = Toast.makeText(context,"No article found \r\n" + error ,Toast.LENGTH_LONG);
                    toast.show();
                    error.printStackTrace();
                }
            }
        });
    }

    @Override
    public void display_error_message_mainActivity(final String error) {

        mMainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                mProgressBar.setVisibility(View.GONE);
                String text_to_display = "Error network request: \r\n" + error;
                Toast toast = Toast.makeText(getContext(),text_to_display,Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }


    // ---------------------------------------------------------------------------------------------
    // ----------------------------------------- SettingActivity --------------------------------------
    // ---------------------------------------------------------------------------------------------

    private void start_configure_recyclerView_settingActivity(){

        // Recover sharedPreferences_Search
        if(context!=null)
            sharedPreferences_Search = context.getSharedPreferences(EXTRA_SEARCH_SETTINGS, Context.MODE_PRIVATE);

        // Assign variables
        query = sharedPreferences_Search.getString(EXTRA_QUERY_SEARCH,null);
        subject = sharedPreferences_Search.getString(EXTRA_SUBJECTS_SEARCH,null);
        begin_date = sharedPreferences_Search.getString(EXTRA_BEGIN_DATE,null);
        end_date = sharedPreferences_Search.getString(EXTRA_END_DATE,null);

        configure_articles_settingActivity();
    }

    public void configure_articles_settingActivity() {
        if(api_key!=null && context!=null && mCallbackPageFragment!=null){
            Search_request search_request = new Search_request("search",query,subject,begin_date,end_date);
            listArticlesSearch = new ListArticlesSearch(context, api_key, search_request, mCallbackPageFragment);
        }
    }

    @Override
    public void finish_configure_recyclerView_settingActivity() {
        mSettingActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try{
                    listarticles = listArticlesSearch.getListArticles();
                    configureRecyclerView();
                } catch (final Throwable error){
                    Toast toast = Toast.makeText(context,"No article found \r\n" + error ,Toast.LENGTH_LONG);
                    toast.show();
                    error.printStackTrace();
                }
            }
        });
    }

    @Override
    public void display_error_message_settingActivity(final String error) {
        mSettingActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                mProgressBar.setVisibility(View.GONE);
                String text_to_display = "Error network request: \r\n" + error;
                Toast toast = Toast.makeText(getContext(),text_to_display,Toast.LENGTH_LONG);
                toast.show();
            }
        });
    }

    public void configureRecyclerView(){

        try {
            mSettingActivity.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    // Create adapter passing in the sample user data
                    mArticleAdapter = new ArticleAdapter(listarticles, context);
                    // Attach the adapter to the recyclerview to populate items
                    recyclerView.setAdapter(mArticleAdapter);
                    // Set layout manager to position the items
                    recyclerView.setLayoutManager(new LinearLayoutManager(context));

                    if(mProgressBar!=null)
                        mProgressBar.setVisibility(View.GONE);
                }
            });
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        try {
            mMainActivity.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    // Create adapter passing in the sample user data
                    mArticleAdapter = new ArticleAdapter(listarticles, context);
                    // Attach the adapter to the recyclerview to populate items
                    recyclerView.setAdapter(mArticleAdapter);
                    // Set layout manager to position the items
                    recyclerView.setLayoutManager(new LinearLayoutManager(context));

                    if(mProgressBar!=null)
                        mProgressBar.setVisibility(View.GONE);
                }
            });
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(listarticles!=null)
            configureRecyclerView();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        try{
            mMainActivity = (MainActivity) getActivity();
        } catch(Throwable ignored){}
        try{
            mSettingActivity = (SettingActivity) getActivity();
        } catch(Throwable ignored){}
    }

    // ---------------------- TOOLS FOR DEFINING VARIABLES --------------------------------

    private String define_subject(String tab_name) {

        String[] list_subjects = getResources().getStringArray(R.array.list_checkbox);

        for(String subject : list_subjects){
            if(subject.toLowerCase().equals(tab_name))
                return subject.toLowerCase();
        }
        return null;
    }

    public void setListarticles(ArrayList<Article> listarticles) {
        this.listarticles = listarticles;
    }
}