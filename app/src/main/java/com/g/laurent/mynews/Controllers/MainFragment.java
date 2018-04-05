package com.g.laurent.mynews.Controllers;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.g.laurent.mynews.Models.Article;
import com.g.laurent.mynews.R;
import com.g.laurent.mynews.Utils.Doc;
import com.g.laurent.mynews.Utils.ListArticles;
import com.g.laurent.mynews.Utils.Multimedium;
import com.g.laurent.mynews.Utils.NewsStreams;
import com.g.laurent.mynews.Views.ArticleAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    @BindView(R.id.fragment_recycler_view) RecyclerView recyclerView;
    private List<Doc> mDoc;
    private Disposable disposable;
    private ListArticles mListArticles;
    private ArticleAdapter adapter;
    ArrayList<Article> listArticles = new ArrayList<>();

    public MainFragment() {} // Required empty public constructor

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recycler, container, false);
        ButterKnife.bind(this, view);
        //this.executeHttpRequestWithRetrofit();
        return view;
    }

    private void configureRecyclerView(){

        // Create adapter passing in the sample user data
        this.adapter = new ArticleAdapter(this.listArticles);
        // Attach the adapter to the recyclerview to populate items
        this.recyclerView.setAdapter(this.adapter);
        // Set layout manager to position the items
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

    private void setNYTarticles(ListArticles listArticles) {
        this.mListArticles=listArticles;
    }

    private String getImageUrl(Doc doc){

        if (doc.getMultimedia() !=null){

            List<Multimedium> ListMultimedium = doc.getMultimedia();

            if(ListMultimedium !=null){

                for(Multimedium multi : ListMultimedium){
                    if(multi.getUrl()!=null && !multi.getUrl().equals("")) {
                        return multi.getUrl();
                    }
                }
            }
        }
        return null;
    }

    private void Build_data(ListArticles listArticles) {

        if(listArticles!=null){
            if(listArticles.getResponse()!=null) {
                if(listArticles.getResponse().getDocs() != null){

                    mDoc = listArticles.getResponse().getDocs();

                    if (mDoc != null) {

                        for (Doc doc : mDoc) {
                            /*this.listArticles.add(new Article(getImageUrl(doc),
                                    doc.getSnippet(),
                                    doc.getHeadline().getMain(),
                                    doc.getSource(),
                                    doc.getWebUrl()));*/

                        }
                    }
                }
            }
        }
    }

    private void executeHttpRequestWithRetrofit(){

        // 1.2 - Execute the stream subscribing to Observable defined inside GithubStream
        this.disposable = NewsStreams.streamFetchNYTarticles("225a8498a05b4b7bb4d085d0c32e4ce8", "newest", 1).subscribeWith(new DisposableObserver<ListArticles>() {

            @Override
            public void onNext(ListArticles listArticles) {
                setNYTarticles(listArticles);
                Build_data(listArticles);
                configureRecyclerView();
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.disposeWhenDestroy();
    }

    private void disposeWhenDestroy(){
        if (this.disposable != null && !this.disposable.isDisposed()) this.disposable.dispose();
    }

}



