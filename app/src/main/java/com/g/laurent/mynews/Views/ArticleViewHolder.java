package com.g.laurent.mynews.Views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.g.laurent.mynews.Controllers.Activities.WebActivity;
import com.g.laurent.mynews.Models.Article;
import com.g.laurent.mynews.R;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import static android.preference.PreferenceManager.getDefaultSharedPreferences;

class ArticleViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.news_category) TextView category_view;
    @BindView(R.id.news_date) TextView date_view;
    @BindView(R.id.news_title) TextView title_view;
    @BindView(R.id.image_news) ImageView image_view;
    private static final String EXTRA_LIST_ID = "ID_ARTICLES_READ";
    private static final String EXTRA_LINK = "linkaddress";
    private List<String> list_id_articles_read;
    private View article_view;
    private SharedPreferences mSharedPreferences;
    private String list_articles_read;
    private Context context;

    ArticleViewHolder(View itemView, Context context) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        article_view = itemView;
        list_id_articles_read = new ArrayList<>();
        this.mSharedPreferences=getDefaultSharedPreferences(context);
        this.context = context;

        if(mSharedPreferences!=null)
            list_articles_read = mSharedPreferences.getString(EXTRA_LIST_ID, null);
        else
            list_articles_read = null;

        configure_list_articles_read();
    }

    private void configure_list_articles_read(){

        String[] new_list_articles_read;

        if(list_articles_read!=null){
            new_list_articles_read=list_articles_read.split(",");

            list_id_articles_read = new ArrayList<>();

            Collections.addAll(list_id_articles_read, new_list_articles_read);

        } else {
            list_id_articles_read=null;
        }
    }

    public void updateViews(final Article article, final Context context){

        // Filling textviews
        title_view.setText(article.getTitle());
        date_view.setText(article.extract_date());
        category_view.setText(article.extract_category());

        if(has_been_read(article.getId()))
            change_color_for_read_articles();

        // Put image article into imageView
        Glide.with(article_view)
                .load(article.getImageUrl())
                .into(image_view);

        article_view.setClickable(true);
        article_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            setAsArticleRead(article.getId());
                if(article.getWebUrl()!=null){
                    /*System.out.println("eee article.getWebUrl()="+article.getWebUrl());
                    Intent intent = new Intent(context,WebActivity.class);
                    intent.putExtra(EXTRA_LINK, article.getWebUrl());
                    context.startActivity(intent);*/
                }
            }
        });
    }

    private void change_color_for_read_articles(){
        title_view.setTextColor(ContextCompat.getColor(context, R.color.color_article_read));
        date_view.setTextColor(ContextCompat.getColor(context, R.color.color_article_read));
        category_view.setTextColor(ContextCompat.getColor(context, R.color.color_article_read));
    }

    private void setAsArticleRead(String id){
        if(!has_been_read(id)){
            list_id_articles_read=shift_values_from_table(list_id_articles_read,id);
            save_list_articles_read_in_sharedpref();
            change_color_for_read_articles();
        }
    }

    private Boolean has_been_read(String article_id){

        list_articles_read = mSharedPreferences.getString(EXTRA_LIST_ID, null);
        configure_list_articles_read();

        if(list_id_articles_read!=null){ // if the list of read articles is not null
            for(String id : list_id_articles_read){ // for each ID in the list, check if it corresponds to the article_id
                if(id!=null){
                    if(id.equals(article_id)){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private List<String> shift_values_from_table(List<String> table, String new_value){

        int max = 50;
        List<String> new_table = new ArrayList<>();
        new_table.add(new_value);

        if(table!=null){

            if(table.size() < max){ // if the max is NOT reached

                if(table.size()==1)
                    new_table.add(table.get(0));
                else {
                    new_table.addAll(table);
                }

            } else if (table.size() == max){ // if the max is reached

                for(int i = 0; i < max-1; i++)
                    new_table.add(table.get(i));
            }
        }
        return new_table;
    }

    private String create_list_articles_read(){

        StringBuilder articles_read = new StringBuilder();

        // Concatenate all ID's from the list in a single String
        for (int i = 0; i < list_id_articles_read.size(); i++){
            articles_read.append(list_id_articles_read.get(i)).append(",");
        }

        return articles_read.toString();
    }

    private void save_list_articles_read_in_sharedpref(){

        String list_to_be_saved;
        if(list_id_articles_read!=null){

            // create list in String
            list_to_be_saved=create_list_articles_read();

            // save the list built in a single string in sharedpreferences
            mSharedPreferences.edit().putString(EXTRA_LIST_ID, list_to_be_saved).apply();
        }
    }
}

