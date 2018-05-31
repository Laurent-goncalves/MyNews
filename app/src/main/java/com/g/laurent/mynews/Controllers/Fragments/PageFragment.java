package com.g.laurent.mynews.Controllers.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.g.laurent.mynews.Controllers.Activities.MainActivity;
import com.g.laurent.mynews.Models.Article;
import com.g.laurent.mynews.Models.CallbackPageFragment;
import com.g.laurent.mynews.Models.ListArticlesMostPopular;
import com.g.laurent.mynews.Models.ListArticlesSearch;
import com.g.laurent.mynews.Models.ListArticlesTopStories;
import com.g.laurent.mynews.Models.Search_request;
import com.g.laurent.mynews.R;
import com.g.laurent.mynews.Views.ArticleAdapter;
import com.g.laurent.mynews.Views.PageAdapter;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PageFragment extends Fragment implements CallbackPageFragment {

    @BindView(R.id.fragment_recycler_view)
    RecyclerView recyclerView;

    private ArticleAdapter mArticleAdapter;
    private PageAdapter mPageAdapter;
    private SharedPreferences sharedPreferences_Search;
    public static final String EXTRA_TAB_NAME = "tab_name";
    public static final String EXTRA_TAB_TOP_STORIES = "tab_top_stories";
    public static final String EXTRA_TAB_MOST_POPULAR = "tab_most_popular";
    public static final String EXTRA_TAB_SEARCH = "tab_search";
    public static final String EXTRA_TAB_TRAVEL = "tab_travel";

    public static final String EXTRA_QUERY = "query";
    public static final String EXTRA_SUBJECT = "subject";
    public static final String EXTRA_BEGIN = "begin_date";
    public static final String EXTRA_END = "end_date";

    private static final String EXTRA_BEGIN_DATE = "begin_date_search";
    private static final String EXTRA_END_DATE = "end_date_search";
    private static final String EXTRA_QUERY_SEARCH = "query_search";
    private static final String EXTRA_SUBJECTS_SEARCH = "list_subjects_search";
    private static final String EXTRA_SEARCH_SETTINGS = "SEARCH_settings";
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
    private String type_request;
    private MainActivity mMainActivity;
    private Context context;
    private String api_key;

    public PageFragment() {} // Required empty public constructor

    public static PageFragment newInstance(String tab_name, String api_key) {

        // Create new fragment
        PageFragment frag = new PageFragment();

        // Create bundle and add it some data
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_TAB_NAME, tab_name);
        bundle.putString(EXTRA_API_KEY, api_key);
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

        // Assign variables
        mMainActivity = (MainActivity) getActivity();
        mCallbackPageFragment = this;
        context = getContext();

        // Recover the tab_name and the API key
        if(getArguments()!=null){
            api_key = getArguments().getString(EXTRA_API_KEY,null);
            tab_name = getArguments().getString(EXTRA_TAB_NAME);
            configure_recyclerView();
        }
        return view;
    }

    private void configure_recyclerView(){

        if(tab_name.equals(EXTRA_TAB_SEARCH)){ // if user has clicked on tab "travel"

            // Recover sharedPreferences_Search
            if(context!=null)
                sharedPreferences_Search = context.getSharedPreferences(EXTRA_SEARCH_SETTINGS, Context.MODE_PRIVATE);

            // Assign variables
            query = sharedPreferences_Search.getString(EXTRA_QUERY_SEARCH,null);
            subject = sharedPreferences_Search.getString(EXTRA_SUBJECTS_SEARCH,null);
            begin_date = sharedPreferences_Search.getString(EXTRA_BEGIN_DATE,null);
            end_date = sharedPreferences_Search.getString(EXTRA_END_DATE,null);

        } else { // if user has clicked on Most Popular tab or Top Stories tab

            // Assign variables
            query = Objects.requireNonNull(getArguments()).getString(EXTRA_QUERY);
            subject = define_subject(getArguments().getString(EXTRA_SUBJECT));

            if(subject==null)
                subject = "politics";

            begin_date = getArguments().getString(EXTRA_BEGIN);
            end_date = getArguments().getString(EXTRA_END);
        }

        configure_subject_articles(tab_name);
    }

    public void configure_subject_articles(String tab_name) {

        if(api_key!=null){

            switch(tab_name){
                case EXTRA_TAB_TOP_STORIES:
                    listArticlesTopStories = new ListArticlesTopStories(api_key,subject, mCallbackPageFragment);
                    break;
                case EXTRA_TAB_MOST_POPULAR:
                    listArticlesMostPopular = new ListArticlesMostPopular(api_key, subject, mCallbackPageFragment);
                    break;
                case EXTRA_TAB_TRAVEL:
                    Search_request search_request = new Search_request("search",tab_name);
                    listArticlesSearch = new ListArticlesSearch(context, api_key, search_request, mCallbackPageFragment);
                    break;
                case EXTRA_TAB_SEARCH:
                    search_request = new Search_request("search", query,subject, begin_date, end_date);
                    listArticlesSearch = new ListArticlesSearch(context, api_key, search_request, mCallbackPageFragment);
                    break;
            }
        }
    }

    @Override
    public void launch_configure_recycler_view() {

        try{
            switch(tab_name){
                case EXTRA_TAB_TOP_STORIES:
                    listarticles = listArticlesTopStories.getListArticles();
                    break;
                case EXTRA_TAB_MOST_POPULAR:
                    listarticles = listArticlesMostPopular.getListArticles();
                    break;
                case EXTRA_TAB_TRAVEL:
                    listarticles = listArticlesSearch.getListArticles();
                    break;
                case EXTRA_TAB_SEARCH:
                    listarticles = listArticlesSearch.getListArticles();
                    break;
                case "notif_search":
                    listarticles = listArticlesSearch.getListArticles();
                    break;
            }
            configureRecyclerView(listarticles);

        } catch (Throwable error){
            Toast toast = Toast.makeText(context,"No article found",Toast.LENGTH_LONG);
            toast.show();
            error.printStackTrace();
        }
    }



    public void configureRecyclerView(final ArrayList<Article> listarticles){

        try {
            mMainActivity.runOnUiThread(new Runnable() {

             @Override
             public void run() {
                 if(mArticleAdapter == null) {
                     // Create adapter passing in the sample user data
                     mArticleAdapter = new ArticleAdapter(listarticles, context);
                     // Attach the adapter to the recyclerview to populate items
                     recyclerView.setAdapter(mArticleAdapter);
                     // Set layout manager to position the items
                     recyclerView.setLayoutManager(new LinearLayoutManager(context));
                 } else
                     mArticleAdapter.notifyDataSetChanged();
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
            configureRecyclerView(listarticles);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        mMainActivity = (MainActivity) getActivity();
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

    private String define_type_request(String tab_name){

        switch (tab_name) {
            case "MOST POPULAR":
                return "most popular";
            case "TOP STORIES":
                return "top stories";
            case "search request":
                return "search request";
            default:
                return "search";
        }
    }


}
