package com.g.laurent.mynews.Controllers.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.g.laurent.mynews.Models.Article;
import com.g.laurent.mynews.R;
import com.g.laurent.mynews.Utils.MostPopular.MediaMetadatum;
import com.g.laurent.mynews.Utils.MostPopular.Medium;
import com.g.laurent.mynews.Utils.MostPopular.MostPopular;
import com.g.laurent.mynews.Utils.MostPopular.Result;
import com.g.laurent.mynews.Utils.Search.Doc;
import com.g.laurent.mynews.Utils.Search.ListArticles;
import com.g.laurent.mynews.Utils.Search.Multimedium;
import com.g.laurent.mynews.Utils.NewsStreams;
import com.g.laurent.mynews.Utils.TopStories.MultimediumTopS;
import com.g.laurent.mynews.Utils.TopStories.ResultTopS;
import com.g.laurent.mynews.Utils.TopStories.TopStories;
import com.g.laurent.mynews.Views.ArticleAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment{

    @BindView(R.id.fragment_recycler_view)
    RecyclerView recyclerView;
    private ArticleAdapter adapter;
    private List<Doc> mDoc;
    private List<Result> mResultMostPopular;
    private List<ResultTopS> mResultTopStories;
    private Disposable disposable;
    ArrayList<Article> listArticles = new ArrayList<>();
    private SharedPreferences sharedPreferences_Search;

    public static final String EXTRA_TAB_NAME = "tab_name";
    public static final String EXTRA_QUERY = "query";
    public static final String EXTRA_SUBJECT = "subject";
    public static final String EXTRA_BEGIN = "begin_date";
    public static final String EXTRA_END = "end_date";

    private String tab_name;
    private String query;
    private String filter_q;
    private String subject;
    private String begin_date;
    private String end_date;


    public MainFragment() {} // Required empty public constructor

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recycler, container, false);
        ButterKnife.bind(this, view);
        sharedPreferences_Search = getContext().getSharedPreferences("SEARCH_settings", Context.MODE_PRIVATE);

        tab_name = getArguments().getString(EXTRA_TAB_NAME);

        if(tab_name.equals("search request")){

            query = sharedPreferences_Search.getString("query",null);
            subject = sharedPreferences_Search.getString("list_subjects",null);
            begin_date = sharedPreferences_Search.getString("begin_date",null);
            end_date = sharedPreferences_Search.getString("end_date",null);

        } else {
            query = getArguments().getString(EXTRA_QUERY);
            subject = define_subject(getArguments().getString(EXTRA_SUBJECT));

            if(subject==null)
                subject = "food";

            begin_date = getArguments().getString(EXTRA_BEGIN);
            end_date = getArguments().getString(EXTRA_END);
        }

        configure_subject_articles(tab_name);

        return view;
    }

    private void configure_subject_articles(String tab_name) {

        String type_request = define_type_request(tab_name);

        switch(type_request){

            case "search request":

                this.disposable = NewsStreams.streamFetchgetListArticles(query,subject, begin_date,end_date).subscribeWith(new DisposableObserver<ListArticles>() {

                    @Override
                    public void onNext(ListArticles listArticles) {
                        Build_data_SearchArticles(listArticles);
                        configureRecyclerView();
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
                break;

            case "search":

                this.disposable = NewsStreams.streamFetchgetListArticles(query, null, begin_date,end_date).subscribeWith(new DisposableObserver<ListArticles>() {

                    @Override
                    public void onNext(ListArticles listArticles) {
                        Build_data_SearchArticles(listArticles);
                        configureRecyclerView();
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
                break;

            case "most popular":

                this.disposable = NewsStreams.streamFetchgetMostPopular(subject).subscribeWith(new DisposableObserver<MostPopular>() {

                    @Override
                    public void onNext(MostPopular mostPopular) {
                        Build_data_mostPopular(mostPopular);
                        configureRecyclerView();
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
                break;

            case "top stories":

                this.disposable = NewsStreams.streamFetchgetTopStories(subject).subscribeWith(new DisposableObserver<TopStories>() {

                    @Override
                    public void onNext(TopStories topStories) {
                        Build_data_topStories(topStories);
                        configureRecyclerView();
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
                break;

        }
    }

    private void configureRecyclerView(){

        if(this.adapter == null) {
            // Create adapter passing in the sample user data
            this.adapter = new ArticleAdapter(this.listArticles, getContext());
            // Attach the adapter to the recyclerview to populate items
            this.recyclerView.setAdapter(this.adapter);
            // Set layout manager to position the items
            this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    private String[] getListCheckBoxOK(String liste){
        if(liste!=null)
            return liste.split(",");
        else
            return null;
    }


    @Override
    public void onResume() {
        super.onResume();
        configure_subject_articles(tab_name);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.disposeWhenDestroy();
    }

    private void disposeWhenDestroy(){
        if (this.disposable != null && !this.disposable.isDisposed()) this.disposable.dispose();
    }

    // ----------------------------------- METHODS FOR MOST POPULAR REQUEST -------------------------------

    private void Build_data_mostPopular(MostPopular mostPopular) {

        if(mostPopular != null){

            if(mostPopular.getResults()!=null) {

                List<Result> mResultMostPopular = mostPopular.getResults();
                if (mResultMostPopular != null) {

                    for(Result resultMostPopular : mResultMostPopular) {

                        System.out.println("eeee555   " + getImageUrlMostPopular(resultMostPopular));

                        this.listArticles.add(new Article(getImageUrlMostPopular(resultMostPopular),
                                resultMostPopular.getPublishedDate(), resultMostPopular.getTitle(),
                                resultMostPopular.getSection(), null, resultMostPopular.getUrl(), resultMostPopular.getId()));

                    }
                }
            }
        }
    }

    private String getImageUrlMostPopular(Result result){

      /*  System.out.println("eeee  DEBUT");

        if(result.getMedia()!=null){

            for (Medium med : result.getMedia()) {

                if (med != null) {
                    if (med.getMediaMetadata() != null) {
                        List<MediaMetadatum> mediaMetadata = med.getMediaMetadata();

                        for (MediaMetadatum metamedia : mediaMetadata) {
                            if (metamedia.getUrl() != null)
                                return metamedia.getUrl(); // return image link
                        }
                    }
                }
            }

        }




        Medium medium=null;
        List<Medium> mMediumList=null;

        if (result.getMedia() !=null) {

            Object media = result.getMedia();

            if (media != null) {
                if (media.toString().equals("")) {
                    return null;
                } else {

                    try {
                        medium = (Medium) media;

                        if (medium != null) {

                            if (medium.getMediaMetadata() != null) {
                                List<MediaMetadatum> mediaMetadata = medium.getMediaMetadata();
                                for (MediaMetadatum metamedia : mediaMetadata) {
                                    if (metamedia.getUrl() != null)
                                        return metamedia.getUrl(); // return image link
                                }
                            }
                        }

                    } catch (Throwable e1) {

                        try {
                            mMediumList = (List<Medium>) media;

                            for (Medium med : mMediumList) {

                                System.out.println("eeee2");
                                if (med != null) {
                                    System.out.println("eeee3");
                                    if (med.getMediaMetadata() != null) {
                                        System.out.println("eeee4");
                                        List<MediaMetadatum> mediaMetadata = med.getMediaMetadata();

                                        for (MediaMetadatum metamedia : mediaMetadata) {
                                            if (metamedia.getUrl() != null)
                                                return metamedia.getUrl(); // return image link
                                        }
                                    }
                                }
                            }

                        } catch (Throwable e2) {
                            media = null;
                            System.out.println("eee putain!!!");
                        }
                    }
                }
            }
        }*/

        return null;
    }

    // ----------------------------------- METHODS FOR TOP STORIES REQUEST -------------------------------

    private void Build_data_topStories(TopStories topStories) {

        if(topStories!=null){

            if(topStories.getResultTopS()!=null) {

                mResultTopStories = topStories.getResultTopS();

                if (mResultTopStories != null) {

                    for(ResultTopS result : mResultTopStories) {
                        Article article = new Article(getImageUrlTopStories(result),
                                result.getPublishedDate(),result.getTitle(),
                                result.getSection(),result.getSubsection(),result.getUrl(),result.getUrl());

                        this.listArticles.add(article);

                    }
                }
            }
        }
    }

    private String getImageUrlTopStories(ResultTopS result){

        if (result.getMultimedia() !=null){
            List<MultimediumTopS> multimediumList = result.getMultimedia();

            for(MultimediumTopS multimedium : multimediumList){
                if(multimedium.getUrl()!=null && !multimedium.getUrl().equals("")) {
                    return multimedium.getUrl();
                }
            }
        }
        return null;
    }

    // ----------------------------------- METHODS FOR SEARCH REQUEST -------------------------------

    private void Build_data_SearchArticles(ListArticles listArticles) {

        if(listArticles!=null){
            if(listArticles.getResponse()!=null) {
                if(listArticles.getResponse().getDocs() != null){

                    mDoc = listArticles.getResponse().getDocs();

                    if (mDoc != null) {

                        for(Doc doc : mDoc) {
                            this.listArticles.add(new Article(getImageUrlSearch(doc),
                                    doc.getPubDate(),
                                    doc.getHeadline().getMain(),
                                    doc.getSectionName(),null,doc.getWebUrl(),doc.getId()));

                        }
                    }
                }
            }
        }
    }

    private String getImageUrlSearch(Doc doc){

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
