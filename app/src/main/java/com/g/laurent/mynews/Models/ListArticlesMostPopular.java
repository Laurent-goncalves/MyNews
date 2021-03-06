package com.g.laurent.mynews.Models;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.g.laurent.mynews.Utils.MostPopular.MediaMetadatum;
import com.g.laurent.mynews.Utils.MostPopular.Medium;
import com.g.laurent.mynews.Utils.MostPopular.MostPopular;
import com.g.laurent.mynews.Utils.MostPopular.Result;
import com.g.laurent.mynews.Utils.NewsStreams;
import java.util.ArrayList;
import java.util.List;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

public class ListArticlesMostPopular implements Disposable {

    private Context context;
    private Disposable disposable;
    private final String subject;
    private final String api_key;
    private ArrayList<Article> listarticles;
    private CallbackPageFragment mCallbackPageFragment;

    public ListArticlesMostPopular(Context context, String api_key, String subject, CallbackPageFragment mCallbackPageFragment){
        this.listarticles=new ArrayList<>();
        this.subject=subject;
        this.api_key = api_key;
        this.context= context;
        this.mCallbackPageFragment = mCallbackPageFragment;
        launch_request_most_popular();
    }

    private void launch_request_most_popular(){
        disposable = NewsStreams.streamFetchgetMostPopular(api_key , subject).subscribeWith(new DisposableObserver<MostPopular>() {

            @Override
            public void onNext(MostPopular mostPopular) {
                Build_data_mostPopular(mostPopular);
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
                Log.e("TAG", "On Complete !!");
            }
        });
    }

    private void Build_data_mostPopular(MostPopular mostPopular) {

        if(mostPopular != null){

            if(mostPopular.getResults()!=null) {

                List<Result> mResultMostPopular = mostPopular.getResults();
                if (mResultMostPopular != null) {

                    for(Result resultMostPopular : mResultMostPopular) {

                        // define the listarticles and mlist_ID
                        listarticles.add(new Article(getImageUrlMostPopular(resultMostPopular),
                                resultMostPopular.getPublishedDate(), resultMostPopular.getTitle(),
                                resultMostPopular.getSection(), null, resultMostPopular.getUrl(), resultMostPopular.getUrl()));
                    }
                }
            }
        }
    }

    private String getImageUrlMostPopular(Result result){

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
