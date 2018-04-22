package com.g.laurent.mynews.Models;

import android.util.Log;
import com.g.laurent.mynews.Utils.NewsStreams;
import com.g.laurent.mynews.Utils.TopStories.MultimediumTopS;
import com.g.laurent.mynews.Utils.TopStories.ResultTopS;
import com.g.laurent.mynews.Utils.TopStories.TopStories;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

public class ListArticlesTopStories {

    private Disposable disposable;
    private ArrayList<Article> listarticles;
    private String subject;
    private ArrayList<String> mlist_ID;
    private CallbackMainActivity mCallbackMainActivity;

    public ListArticlesTopStories(String subject, CallbackMainActivity mCallbackMainActivity){
        this.mlist_ID=new ArrayList<>();
        this.listarticles=new ArrayList<>();
        this.subject=subject;
        this.mCallbackMainActivity = mCallbackMainActivity;
        launch_request_top_stories();
    }

    private void launch_request_top_stories(){

         disposable = NewsStreams.streamFetchgetTopStories(subject).subscribeWith(new DisposableObserver<TopStories>() {

            @Override
            public void onNext(TopStories topStories) {
                Build_data_topStories(topStories);

                if(mCallbackMainActivity!=null)
                    mCallbackMainActivity.launch_configure_recycler_view();
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("eeee PROBLEM TOP STORIES");
                Log.e("TAG", "On Error" + Log.getStackTraceString(e));
            }

            @Override
            public void onComplete() {
                Log.e("TAG", "On Complete !!");
            }
        });
       // disposable.dispose();
    }

    private void Build_data_topStories(TopStories topStories) {

        if(topStories!=null){

            if(topStories.getResultTopS()!=null) {

                List<ResultTopS> resultTopStories = topStories.getResultTopS();

                if (resultTopStories != null) {

                    for(ResultTopS result : resultTopStories) {
                        /*Article article = new Article(getImageUrlTopStories(result),
                                result.getPublishedDate(),result.getTitle(),
                                result.getSection(),result.getSubsection(),result.getUrl(),result.getUrl());*/

                        this.listarticles.add(new Article(getImageUrlTopStories(result),
                                result.getPublishedDate(),result.getTitle(),
                                result.getSection(),result.getSubsection(),result.getUrl(),result.getUrl()));

                        mlist_ID.add(result.getUrl());
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

    public ArrayList<Article>  getListArticles(){
        return listarticles;
    }
}
