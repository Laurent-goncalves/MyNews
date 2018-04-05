package com.g.laurent.mynews.Utils;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import io.reactivex.Observable;
import retrofit2.http.Query;


public interface NewsService {

    @GET("articlesearch.json")
                     // "articlesearch.json?q=query&page={pagenum}&sort=newest&api-key=")
    Observable<ListArticles> getListArticles(@Query("api-key") String api_key,
                                            @Query("sort") String sort,
                                            @Query("page") int page);

    HttpLoggingInterceptor logging = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);

    OkHttpClient.Builder httpClient = new OkHttpClient.Builder().addInterceptor(logging);


    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://api.nytimes.com/svc/search/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(httpClient.build())
            .build();


    // TOP STORIES
    // https://api.nytimes.com/svc/topstories/v2/home.json?api-key=225a8498a05b4b7bb4d085d0c32e4ce8

}
