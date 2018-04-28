package com.g.laurent.mynews.Views;


import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.g.laurent.mynews.Models.Article;
import com.g.laurent.mynews.R;
import java.util.ArrayList;


public class ArticleAdapter extends RecyclerView.Adapter<ArticleViewHolder>{

    private ArrayList<Article> listArticles;
    private Context context;
    private SharedPreferences mSharedPreferences;
    private ArticleViewHolder mArticleViewHolder;

    public ArticleViewHolder getArticleViewHolder() {
        return mArticleViewHolder;
    }

    public ArticleAdapter(ArrayList<Article> ListArticles, Context context) {
        System.out.println("eeee START");
        this.listArticles=ListArticles;
        this.context=context;
        if(context!=null)
            mSharedPreferences=context.getSharedPreferences("LIST_ARTICLES_READ",Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        System.out.println("eeee onCreateViewHolder");
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fragment_item_recycler, parent, false);
        mArticleViewHolder=new ArticleViewHolder(view,context,mSharedPreferences);
        return new ArticleViewHolder(view,context,mSharedPreferences);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {
        holder.updateViews(this.listArticles.get(position),context);
    }

    @Override
    public int getItemCount() {
        return listArticles.size();
    }

}
