package com.g.laurent.mynews.Models;

import android.util.Log;

import com.g.laurent.mynews.Utils.MostPopular.Medium;
import com.g.laurent.mynews.Utils.MostPopular.MostPopular;
import com.g.laurent.mynews.Utils.MostPopular.Result;
import com.g.laurent.mynews.Utils.NewsStreams;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

public class ListArticlesMostPopular {

    private Disposable disposable;
    private final String subject;
    private ArrayList<Article> listarticles;
    private ArrayList<String> mlist_ID;
    private CallbackMainActivity mCallbackMainActivity;

    public ListArticlesMostPopular(String subject, CallbackMainActivity mCallbackMainActivity){
        this.listarticles=new ArrayList<>();
        this.mlist_ID=new ArrayList<>();
        this.subject=subject;
        this.mCallbackMainActivity=mCallbackMainActivity;
        System.out.println("eeee START");
        launch_request_most_popular();
        System.out.println("eeee END");
    }

    private void launch_request_most_popular(){
        disposable = NewsStreams.streamFetchgetMostPopular(subject).subscribeWith(new DisposableObserver<MostPopular>() {

            @Override
            public void onNext(MostPopular mostPopular) {
                System.out.println("eeee COUCOU");
                Build_data_mostPopular(mostPopular);
                System.out.println("eeee BEAUUUAAA");
                mCallbackMainActivity.launch_configure_recycler_view();
                System.out.println("eeee FENETRE");
                disposable.dispose();
            }

            @Override
            public void onError(Throwable e) {
                Log.e("TAG","On Error"+Log.getStackTraceString(e));
                System.out.println("eeee PROBLEM");
            }

            @Override
            public void onComplete() {
                Log.e("TAG","On Complete !!");
            }
        });


    }

    private void Build_data_mostPopular(MostPopular mostPopular) {

        if(mostPopular != null){

            if(mostPopular.getResults()!=null) {

                List<Result> mResultMostPopular = mostPopular.getResults();
                if (mResultMostPopular != null) {

                    for(Result resultMostPopular : mResultMostPopular) {

                        listarticles.add(new Article(getImageUrlMostPopular(resultMostPopular),
                                resultMostPopular.getPublishedDate(), resultMostPopular.getTitle(),
                                resultMostPopular.getSection(), null, resultMostPopular.getUrl(), resultMostPopular.getId()));

                        System.out.println("eeee " + listarticles.toString());

                        mlist_ID.add(resultMostPopular.getId());
                    }
                }
            }
        }
    }

    private String getImageUrlMostPopular(Result result){



        ArrayList<Medium> list = new ArrayList<>();
        JSONArray jsonArray = result.getMedia();
        if (jsonArray != null) {
            int len = jsonArray.length();
            for (int i=0;i<len;i++){
                try {
                    list.add((Medium) jsonArray.get(i));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("eeee  list.size()="+list.size());

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

    public ArrayList<Article>  getListArticles(){
        return listarticles;
    }
}
