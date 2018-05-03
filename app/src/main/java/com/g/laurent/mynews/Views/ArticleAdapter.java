package com.g.laurent.mynews.Views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.g.laurent.mynews.Models.Article;
import com.g.laurent.mynews.R;
import java.util.ArrayList;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleViewHolder>{

    private ArrayList<Article> listArticles;
    private Context context;

    public ArticleAdapter(ArrayList<Article> ListArticles, Context context) {
        this.listArticles=ListArticles;

        if(listArticles==null) {
            Toast toast = Toast.makeText(context, "No article found", Toast.LENGTH_LONG);
            toast.show();
        }
        this.context=context;
    }

    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fragment_item_recycler, parent, false);
        return new ArticleViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {
        holder.updateViews(this.listArticles.get(position),context);
    }

    @Override
    public int getItemCount() {
        if(listArticles!=null)
            return listArticles.size();
        else
            return 0;
    }

}
