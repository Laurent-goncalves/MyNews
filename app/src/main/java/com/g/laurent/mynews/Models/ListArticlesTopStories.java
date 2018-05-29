package com.g.laurent.mynews.Models;

import android.content.Context;
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

    private ArrayList<Article> listarticles;
    private String subject;
    private CallbackMainFragment mCallbackMainFragment;
    private Context context;

    public ListArticlesTopStories(Context context, String subject, CallbackMainFragment mCallbackMainFragment){
        this.listarticles=new ArrayList<>();
        this.subject=subject;
        this.context=context;
        this.mCallbackMainFragment = mCallbackMainFragment;
        launch_request_top_stories();
    }

    private void launch_request_top_stories(){

        Disposable disposable = NewsStreams.streamFetchgetTopStories(context, subject).subscribeWith(new DisposableObserver<TopStories>() {

            @Override
            public void onNext(TopStories topStories) {
                Build_data_topStories(topStories);
            }

            @Override
            public void onError(Throwable e) {
                Log.e("TAG", "On Error" + Log.getStackTraceString(e));
            }

            @Override
            public void onComplete() {
                if (mCallbackMainFragment != null)
                    mCallbackMainFragment.launch_configure_recycler_view();
                Log.e("TAG", "On Complete !!");
            }
        });

    }

    private void Build_data_topStories(TopStories topStories) {

        if(topStories!=null){

            if(topStories.getResultTopS()!=null) {

                List<ResultTopS> resultTopStories = topStories.getResultTopS();

                if (resultTopStories != null) {

                    for(ResultTopS result : resultTopStories) {
                        this.listarticles.add(new Article(getImageUrlTopStories(result),
                                result.getPublishedDate(),result.getTitle(),
                                result.getSection(),result.getSubsection(),result.getUrl(),result.getUrl()));
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
