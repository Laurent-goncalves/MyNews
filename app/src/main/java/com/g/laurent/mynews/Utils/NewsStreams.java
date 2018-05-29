package com.g.laurent.mynews.Utils;

import android.content.Context;
import com.g.laurent.mynews.R;
import com.g.laurent.mynews.Utils.MostPopular.MostPopular;
import com.g.laurent.mynews.Utils.Search.ListArticles;
import com.g.laurent.mynews.Utils.TopStories.TopStories;
import java.util.concurrent.TimeUnit;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;


public class NewsStreams {

    public static Observable<ListArticles> streamFetchgetListArticles(Context context, String query, String filter_q, String begin_date, String end_date){
        NewsService MyNewsService = NewsService.retrofit.create(NewsService.class);

        String api_key = context.getResources().getString(R.string.APIkey);

        return MyNewsService.getSearchListArticles(api_key,query,filter_q,"newest", begin_date, end_date)
                .subscribeOn(Schedulers.io())
               // .observeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.newThread())  // TEST
                .timeout(20, TimeUnit.SECONDS);

    }

    public static Observable<MostPopular> streamFetchgetMostPopular(Context context, String subject){
        NewsService MyNewsService = NewsService.retrofit.create(NewsService.class);

        String api_key = context.getResources().getString(R.string.APIkey);

            return MyNewsService.getMostPopularArticles(subject,api_key)
                    .subscribeOn(Schedulers.io())
                   // .observeOn(AndroidSchedulers.mainThread())
                    .observeOn(Schedulers.newThread()) // TEST
                    .timeout(20, TimeUnit.SECONDS);

    }

    public static Observable<TopStories> streamFetchgetTopStories(Context context, String subject){
        NewsService MyNewsService = NewsService.retrofit.create(NewsService.class);

        String api_key = context.getResources().getString(R.string.APIkey);

        return MyNewsService.getTopStoriesArticles(subject,api_key)
                .subscribeOn(Schedulers.io())
               // .observeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.newThread()) // TEST
                .timeout(20, TimeUnit.SECONDS);

    }

}
