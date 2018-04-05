package com.g.laurent.mynews.Utils;

import java.util.concurrent.TimeUnit;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.*;

public class NewsStreams {

    public static Observable<ListArticles> streamFetchNYTarticles(String api_key, String sort, int page){
        NewsService MyNewsService = NewsService.retrofit.create(NewsService.class);

        return MyNewsService.getListArticles(api_key,sort,page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(25, TimeUnit.SECONDS);
    }
}
