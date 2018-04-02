package com.g.laurent.mynews.Utils;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class NewsStreams {

    public static Observable<ListArticles> streamFetchNYTarticles(String api_key, String sort, int page){
        NewsService MyNYTService = NewsService.retrofit.create(NewsService.class);
        return MyNYTService.getListArticles(api_key,sort,page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(25, TimeUnit.SECONDS);
    }
}
