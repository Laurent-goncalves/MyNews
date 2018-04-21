package com.g.laurent.mynews.Models;

import android.util.Log;
import com.g.laurent.mynews.Utils.NewsStreams;
import com.g.laurent.mynews.Utils.Search.Doc;
import com.g.laurent.mynews.Utils.Search.ListArticles;
import com.g.laurent.mynews.Utils.Search.Multimedium;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

public class ListArticlesSearch {

    private Disposable disposable;
    private ArrayList<Article> mlistArticles;
    private ArrayList<String> mlist_ID;
    private String query;
    private String filterq;
    private String begindate;
    private String enddate;
    private CallbackMainActivity mCallbackMainActivity;

    public ListArticlesSearch(String query, String filterq, String begindate, String enddate,CallbackMainActivity mCallbackMainActivity){
        this.mlist_ID=new ArrayList<>();
        this.mlistArticles=new ArrayList<>();
        this.query=query;
        this.filterq=filterq;
        this.begindate=begindate;
        this.enddate=enddate;
        this.mCallbackMainActivity=mCallbackMainActivity;
        launch_request_search_articles();
    }

    private void launch_request_search_articles(){

        disposable = NewsStreams.streamFetchgetListArticles(query, filterq, begindate,enddate).subscribeWith(new DisposableObserver<ListArticles>() {

            @Override
            public void onNext(ListArticles listArticles) {
                Build_data_SearchArticles(listArticles);

                if(mCallbackMainActivity!=null)
                    mCallbackMainActivity.launch_configure_recycler_view();

                disposable.dispose();
            }

            @Override
            public void onError(Throwable e) {
                Log.e("TAG","On Error"+Log.getStackTraceString(e));
                System.out.println("eeee PROBLEM SEARCH");
            }

            @Override
            public void onComplete() {
                Log.e("TAG","On Complete !!");
            }
        });
    }

    private void Build_data_SearchArticles(ListArticles listArticles) {

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

                            mlist_ID.add(doc.getId());
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
                        return "https://static01.nyt.com/" + multimedium.getUrl();
                    }
                }
            }
        }
        return null;
    }

    public ArrayList<Article> getListArticles(){
        return mlistArticles;
    }
}
