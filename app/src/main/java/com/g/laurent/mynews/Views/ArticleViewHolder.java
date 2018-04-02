package com.g.laurent.mynews.Views;


import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
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


    public ArticleViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        article_view = itemView;
    }

    public void updateViews(Article article){

        // Remplissage textviews
        title_view.setText(article.getTitle());
        date_view.setText(article.extract_date());
        category_view.setText(article.extract_category());

        Glide.with(article_view)
                .load(article.getImageUrl())
                .apply(RequestOptions.circleCropTransform())
                .into(image_view);


        title_view.setClickable(true);
        title_view.setMovementMethod(LinkMovementMethod.getInstance());
        String text = "<a href='"+ article.getWebUrl() + "'> " + article.getTitle() + " </a>";
        title_view.setText(Html.fromHtml(text));
        title_view.setLinkTextColor(Color.BLACK);
    }

}
