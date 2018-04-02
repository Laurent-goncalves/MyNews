package com.g.laurent.mynews.Views;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.g.laurent.mynews.Models.Article;
import com.g.laurent.mynews.R;
import java.util.ArrayList;


public class ArticleAdapter extends RecyclerView.Adapter<ArticleViewHolder>{

    private ArrayList<Article> listArticles;

    public ArticleAdapter(ArrayList<Article> ListNYTarticles) {
        this.listArticles=ListNYTarticles;
    }

    @Override
    public ArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fragment_item_recycler, parent, false);
        return new ArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ArticleViewHolder holder, int position) {
        holder.updateViews(this.listArticles.get(position));
    }

    @Override
    public int getItemCount() {
        return listArticles.size();
    }

}
