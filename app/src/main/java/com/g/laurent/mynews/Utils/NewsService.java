package com.g.laurent.mynews.Utils;

import com.g.laurent.mynews.Utils.MostPopular.MostPopular;
import com.g.laurent.mynews.Utils.Search.ListArticles;
import com.g.laurent.mynews.Utils.TopStories.TopStories;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import io.reactivex.Observable;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface NewsService {

    // SEARCH REQUEST
    @GET("search/v2/articlesearch.json")  // "https://api.nytimes.com/svc/articlesearch.json?q=query&page={pagenum}&sort=newest&api-key=225a8498a05b4b7bb4d085d0c32e4ce8")
    Observable<ListArticles> getSearchListArticles(@Query("api-key") String api,
                                                   @Query("q") String query,
                                                   @Query("fq") String filter_q,
                                                   @Query("sort") String sort_type,
                                                   @Query("begin_date") String begin_date,
                                                   @Query("end_date") String end_date);
    // MOST POPULAR ARTICLES REQUEST    // https://api.nytimes.com/svc/mostpopular/v2/mostviewed/Food/1.json?api-key=225a8498a05b4b7bb4d085d0c32e4ce8
    @GET("mostpopular/v2/mostemailed/{section}/30.json") //mostviewed
    Observable<MostPopular> getMostPopularArticles(@Path("section") String subject,
                                                   @Query("api-key") String api);

    // TOP STORIES ARTICLES REQUEST     //https://api.nytimes.com/svc/topstories/v2/home.json?api-key=225a8498a05b4b7bb4d085d0c32e4ce8
    @GET("topstories/v2/{section}.json")
    Observable<TopStories> getTopStoriesArticles(@Path("section") String subject,
                                                 @Query("api-key") String api);

    HttpLoggingInterceptor logging = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);

    OkHttpClient.Builder httpClient = new OkHttpClient.Builder().addInterceptor(logging);

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.nytimes.com/svc/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(httpClient.build())
            .build();
}
