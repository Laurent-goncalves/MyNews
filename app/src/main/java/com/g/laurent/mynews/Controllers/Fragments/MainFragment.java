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
import com.g.laurent.mynews.Models.Article;
import com.g.laurent.mynews.Models.CallbackMainActivity;
import com.g.laurent.mynews.Models.ListArticlesMostPopular;
import com.g.laurent.mynews.Models.ListArticlesSearch;
import com.g.laurent.mynews.Models.ListArticlesTopStories;
import com.g.laurent.mynews.Models.Search_request;
import com.g.laurent.mynews.R;
import com.g.laurent.mynews.Views.ArticleAdapter;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainFragment extends Fragment implements CallbackMainActivity {

    @BindView(R.id.fragment_recycler_view)
    RecyclerView recyclerView;

    private ArticleAdapter adapter;
    private SharedPreferences sharedPreferences_Search;
    public static final String EXTRA_TAB_NAME = "tab_name";
    public static final String EXTRA_QUERY = "query";
    public static final String EXTRA_SUBJECT = "subject";
    public static final String EXTRA_BEGIN = "begin_date";
    public static final String EXTRA_END = "end_date";

    private static final String EXTRA_BEGIN_DATE = "begin_date_search";
    private static final String EXTRA_END_DATE = "end_date_search";
    private static final String EXTRA_QUERY_SEARCH = "query_search";
    private static final String EXTRA_SUBJECTS_SEARCH = "list_subjects_search";
    private static final String EXTRA_SEARCH_SETTINGS = "SEARCH_settings";

    private ListArticlesTopStories listArticlesTopStories;
    private ListArticlesMostPopular listArticlesMostPopular;
    private ListArticlesSearch listArticlesNotif;
    private ListArticlesSearch listArticlesSearch;
    private CallbackMainActivity mCallbackMainActivity;
    private ArrayList<Article> listarticles;

    private String tab_name;
    private String query;
    private String subject;
    private String begin_date;
    private String end_date;
    private String type_request;

    public MainFragment() {} // Required empty public constructor

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view;

        if(inflater!=null) {
            view = inflater.inflate(R.layout.fragment_recycler, container, false);
            ButterKnife.bind(this, view);
            sharedPreferences_Search = getContext().getSharedPreferences(EXTRA_SEARCH_SETTINGS, Context.MODE_PRIVATE);
            mCallbackMainActivity = this;
        } else
            view = null;

        define_variables();
        configure_subject_articles(tab_name);
        return view;
    }

    private void define_variables(){

        // Recover the tab_name
        if(getArguments()!=null)
            tab_name = getArguments().getString(EXTRA_TAB_NAME);

        if(tab_name!=null){

            if(tab_name.equals("search request")){ // in case of search request

                query = sharedPreferences_Search.getString(EXTRA_QUERY_SEARCH,null);
                subject = sharedPreferences_Search.getString(EXTRA_SUBJECTS_SEARCH,null);
                begin_date = sharedPreferences_Search.getString(EXTRA_BEGIN_DATE,null);
                end_date = sharedPreferences_Search.getString(EXTRA_END_DATE,null);

            } else if(getArguments()!=null){ // if not
                query = getArguments().getString(EXTRA_QUERY);
                subject = define_subject(getArguments().getString(EXTRA_SUBJECT));

                if(subject==null)
                    subject = "politics";

                begin_date = getArguments().getString(EXTRA_BEGIN);
                end_date = getArguments().getString(EXTRA_END);
            }
        }
    }

    public void configure_subject_articles(String tab_name) {

        type_request = define_type_request(tab_name);

        switch(type_request){
            case "top stories":
                listArticlesTopStories = new ListArticlesTopStories(subject,mCallbackMainActivity);
                break;
            case "most popular":
                listArticlesMostPopular = new ListArticlesMostPopular(subject,mCallbackMainActivity);
                break;
            case "search":
                if(query==null)
                    query = tab_name;

                Search_request search_request = new Search_request("search", query, null, begin_date, end_date);
                listArticlesSearch = new ListArticlesSearch(getContext(), search_request,null,mCallbackMainActivity);
                break;
            default:
                search_request = new Search_request("search",null,tab_name,null,null);
                listArticlesNotif = new ListArticlesSearch(getContext(), search_request,null,mCallbackMainActivity);
                break;
        }
    }

    @Override
    public void launch_configure_recycler_view() {

        switch(type_request){
            case "top stories":
                listarticles = listArticlesTopStories.getListArticles();
                break;
            case "most popular":
                listarticles = listArticlesMostPopular.getListArticles();
                break;
            case "search":
                listarticles=listArticlesSearch.getListArticles();
                break;
            case "notif_search":
                listarticles=listArticlesNotif.getListArticles();
                break;
        }

        configureRecyclerView(listarticles);
    }

     public void configureRecyclerView(final ArrayList<Article> listarticles){

         try {
             getActivity().runOnUiThread(new Runnable() {

                 @Override
                 public void run() {
                     if(adapter == null) {
                         // Create adapter passing in the sample user data
                         adapter = new ArticleAdapter(listarticles, getContext());
                         // Attach the adapter to the recyclerview to populate items
                         recyclerView.setAdapter(adapter);
                         // Set layout manager to position the items
                         recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                     } else {
                         adapter.notifyDataSetChanged();
                     }


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
            default:
                return "search";
        }
    }

}
