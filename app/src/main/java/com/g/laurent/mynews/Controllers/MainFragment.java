package com.g.laurent.mynews.Controllers;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.g.laurent.mynews.Models.Article;
import com.g.laurent.mynews.R;
import com.g.laurent.mynews.Utils.Doc;
import com.g.laurent.mynews.Utils.ListArticles;
import com.g.laurent.mynews.Utils.MostPopular;
import com.g.laurent.mynews.Utils.Multimedium;
import com.g.laurent.mynews.Utils.NewsStreams;
import com.g.laurent.mynews.Utils.Result;
import com.g.laurent.mynews.Utils.TopStories;
import com.g.laurent.mynews.Views.ArticleAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    @BindView(R.id.fragment_recycler_view) RecyclerView recyclerView;
    private List<Doc> mDoc;
    private List<Result> mResult;
    private Disposable disposable;
    private ArticleAdapter adapter;
    public static final String EXTRA_TAB_NAME = "tab_name";
    public static final String EXTRA_QUERY = "query";
    public static final String EXTRA_SUBJECT = "subject";
    public static final String EXTRA_BEGIN = "begin_date";
    public static final String EXTRA_END = "end_date";
    ArrayList<Article> listArticles = new ArrayList<>();
    private String tab_name;
    private String query;
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

        tab_name = getArguments().getString(EXTRA_TAB_NAME);
        query = getArguments().getString(EXTRA_QUERY);
        subject = define_subject(getArguments().getString(EXTRA_SUBJECT));

        if(subject==null)
            subject = "food";

        begin_date = getArguments().getString(EXTRA_BEGIN);
        end_date = getArguments().getString(EXTRA_END);
        configure_subject_articles(tab_name);
        return view;
    }


    private void configure_subject_articles(String tab_name) {

        String type_request = define_type_request(tab_name);


        switch(type_request){

            case "search":

                this.disposable = NewsStreams.streamFetchgetListArticles(query, begin_date,end_date).subscribeWith(new DisposableObserver<ListArticles>() {

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

        }
    }

    private void Build_data_mostPopular(MostPopular mostPopular) {

        if(mostPopular!=null){
            if(mostPopular.getResults()!=null) {

                mResult = mostPopular.getResults();

                if (mResult != null) {

                    for(Result result : mResult) {
                        this.listArticles.add(new Article(getImageUrl(result),
                                result.getPublishedDate(),result.getTitle(),
                                result.getSection(),result.getSubsection(),result.getUrl()));

                    }
                }
            }
        }
    }

    private void Build_data_topStories(TopStories topStories) {

        if(topStories!=null){

            if(topStories.getResults()!=null) {

                mResult = topStories.getResults();

                if (mResult != null) {

                    for(Result result : mResult) {
                        Article article = new Article(getImageUrl(result),
                                result.getPublishedDate(),result.getTitle(),
                                result.getSection(),result.getSubsection(),result.getUrl());

                        this.listArticles.add(article);
                       // article.ArticleToString();

                    }
                }
            }
        }
    }

    private void Build_data_SearchArticles(ListArticles listArticles) {

        if(listArticles!=null){
            if(listArticles.getResponse()!=null) {
                if(listArticles.getResponse().getDocs() != null){

                    mDoc = listArticles.getResponse().getDocs();

                    if (mDoc != null) {

                        for(Doc doc : mDoc) {
                            this.listArticles.add(new Article(getImageUrl(doc),
                                    doc.getPubDate(),
                                    doc.getHeadline().getMain(),
                                    doc.getSectionName(),null,doc.getWebUrl()));

                        }
                    }
                }
            }
        }
    }

    private void configureRecyclerView(){

        // Create adapter passing in the sample user data
        this.adapter = new ArticleAdapter(this.listArticles);
        // Attach the adapter to the recyclerview to populate items
        this.recyclerView.setAdapter(this.adapter);
        // Set layout manager to position the items
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

    private String getImageUrl(Doc doc){

        if (doc.getMultimedia() !=null){

            List<Multimedium> ListMultimedium = doc.getMultimedia();

            if(ListMultimedium !=null){

                for(Multimedium multi : ListMultimedium){
                    if(multi.getUrl()!=null && !multi.getUrl().equals("")) {
                        return multi.getUrl();
                    }
                }
            }
        }
        return null;
    }

    private String getImageUrl(Result result){

        if (result.getMultimedia() !=null){

            List<Multimedium> ListMultimedium = result.getMultimedia();

            if(ListMultimedium !=null){

                for(Multimedium multi : ListMultimedium){
                    if(multi.getUrl()!=null && !multi.getUrl().equals("")) {
                        return multi.getUrl();
                    }
                }
            }
        }
        return null;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.disposeWhenDestroy();
    }

    private void disposeWhenDestroy(){
        if (this.disposable != null && !this.disposable.isDisposed()) this.disposable.dispose();
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



