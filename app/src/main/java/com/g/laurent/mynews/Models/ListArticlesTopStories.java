package com.g.laurent.mynews.Models;

import android.content.Context;
import android.widget.Toast;

import com.g.laurent.mynews.Utils.NewsStreams;
import com.g.laurent.mynews.Utils.TopStories.MultimediumTopS;
import com.g.laurent.mynews.Utils.TopStories.ResultTopS;
import com.g.laurent.mynews.Utils.TopStories.TopStories;
import java.util.ArrayList;
import java.util.List;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

public class ListArticlesTopStories implements Disposable {

    private Context context;
    private Disposable disposable;
    private ArrayList<Article> listarticles;
    private String subject;
    private CallbackPageFragment mCallbackPageFragment;
    private final String api_key;

    public ListArticlesTopStories(Context context,  String api_key, String subject, CallbackPageFragment mCallbackPageFragment){
        this.listarticles=new ArrayList<>();
        this.subject=subject;
        this.api_key=api_key;
        this.context= context;
        this.mCallbackPageFragment = mCallbackPageFragment;
        launch_request_top_stories();
    }

    private void launch_request_top_stories(){

        disposable = NewsStreams.streamFetchgetTopStories(api_key, subject).subscribeWith(new DisposableObserver<TopStories>() {

            @Override
            public void onNext(TopStories topStories) {
                Build_data_topStories(topStories);
            }

            @Override
            public void onError(Throwable e) {
                if (mCallbackPageFragment != null)
                    mCallbackPageFragment.display_error_message_mainActivity(e.getMessage());
            }

            @Override
            public void onComplete() {

                if(listarticles.size()==0) {
                    Toast toast = Toast.makeText(context, "No article found", Toast.LENGTH_LONG);
                    toast.show();
                }

                if (mCallbackPageFragment != null)
                    mCallbackPageFragment.finish_configure_recyclerView_mainActivity();
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

    @Override
    public void dispose() {
        if (this.disposable != null && !this.disposable.isDisposed()) this.disposable.dispose();
    }

    @Override
    public boolean isDisposed() {
        return false;
    }
}
