package com.g.laurent.mynews;

import android.util.Log;

import com.g.laurent.mynews.Models.Article;
import com.g.laurent.mynews.Utils.ListArticles;
import com.g.laurent.mynews.Utils.NewsStreams;

import org.junit.Test;

import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void extract_date_correct() throws Exception {

        Article article = new Article("","2018-04-02","","","","");
        assertEquals("02/04/2018", article.extract_date());
    }

    @Test
    public void extract_category() throws Exception {

        Article article = new Article("","2018-04-02","","U.S.","Politics","");
        assertEquals("U.S. > Politics", article.extract_category());

        article.setSection("U.S.");
        article.setSubsection(null);
        assertEquals("U.S.", article.extract_category());

        article.setSection(null);
        article.setSubsection("Politics");
        assertEquals("Politics", article.extract_category());

        article.setSection(null);
        article.setSubsection(null);
        assertEquals(null, article.extract_category());
    }



    @Test
    public void executeHttpRequestWithRetrofit(){

        final Disposable disposable;

        // Execute the stream subscribing to Observable defined inside GithubStream
        //disposable = NewsStreams.streamFetchNYTarticles("225a8498a05b4b7bb4d085d0c32e4ce8","newest",1)


        Callback<Integer> callback = new Callback<Integer>(){
            @Override
            public void onResponse(Call<Integer> call, retrofit2.Response<Integer> response) {
                System.out.println("eeeee " + response.code());
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {

            }};

        disposable = NewsStreams.streamFetchNYTarticles("225a8498a05b4b7bb4d085d0c32e4ce8","newest",1).subscribeWith(new DisposableObserver<ListArticles>() {

                        @Override
                        public void onNext(ListArticles listArticles) {


                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e("TAG","On Error"+Log.getStackTraceString(e));
                        }

                        @Override
                        public void onComplete() {
                            Log.e("TAG","On Complete !!");
                        }
                    });





    }

// for each request sent, a status OK is received


}