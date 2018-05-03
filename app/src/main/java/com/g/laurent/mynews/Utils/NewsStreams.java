package com.g.laurent.mynews.Utils;

import com.g.laurent.mynews.Utils.MostPopular.MostPopular;
import com.g.laurent.mynews.Utils.Search.ListArticles;
import com.g.laurent.mynews.Utils.TopStories.TopStories;
import java.util.concurrent.TimeUnit;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class NewsStreams {


    public static Observable<ListArticles> streamFetchgetListArticles(String query, String filter_q, String begin_date, String end_date){
        NewsService MyNewsService = NewsService.retrofit.create(NewsService.class);

        return MyNewsService.getSearchListArticles("225a8498a05b4b7bb4d085d0c32e4ce8",query,filter_q,"newest", begin_date, end_date)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
               // .observeOn(Schedulers.newThread())  // TEST
                .timeout(20, TimeUnit.SECONDS);

    }

    public static Observable<MostPopular> streamFetchgetMostPopular(String subject){
        NewsService MyNewsService = NewsService.retrofit.create(NewsService.class);

            return MyNewsService.getMostPopularArticles(subject,"225a8498a05b4b7bb4d085d0c32e4ce8")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    //.observeOn(Schedulers.newThread()) // TEST
                    .timeout(20, TimeUnit.SECONDS);

    }

    public static Observable<TopStories> streamFetchgetTopStories(String subject){
        NewsService MyNewsService = NewsService.retrofit.create(NewsService.class);

        return MyNewsService.getTopStoriesArticles(subject,"225a8498a05b4b7bb4d085d0c32e4ce8")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                //.observeOn(Schedulers.newThread()) // TEST
                .timeout(20, TimeUnit.SECONDS);

    }

}
