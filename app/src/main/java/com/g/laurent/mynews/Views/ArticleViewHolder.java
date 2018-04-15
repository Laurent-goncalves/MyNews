package com.g.laurent.mynews.Views;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.g.laurent.mynews.Controllers.Activities.MainActivity;
import com.g.laurent.mynews.Controllers.Activities.WebActivity;
import com.g.laurent.mynews.Models.Article;
import com.g.laurent.mynews.R;
import butterknife.BindView;
import butterknife.ButterKnife;


class ArticleViewHolder extends RecyclerView.ViewHolder {

    private View article_view;
    @BindView(R.id.news_category) TextView category_view;
    @BindView(R.id.news_date) TextView date_view;
    @BindView(R.id.news_title) TextView title_view;
    @BindView(R.id.image_news) ImageView image_view;
    public static final String EXTRA_LINK = "linkaddress";

    public ArticleViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        article_view = itemView;
    }

    public void updateViews(final Article article, final Context context){

        // Remplissage textviews
        title_view.setText(article.getTitle());
        date_view.setText(article.extract_date());
        category_view.setText(article.extract_category());

        Glide.with(article_view)
                .load(article.getImageUrl())
                .into(image_view);

        article_view.setClickable(true);
        article_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,WebActivity.class);
                intent.putExtra(EXTRA_LINK, article.getWebUrl());
                context.startActivity(intent);
            }
        });
    }
}
