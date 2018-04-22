package com.g.laurent.mynews.Views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.g.laurent.mynews.Models.Article;
import com.g.laurent.mynews.R;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

class ArticleViewHolder extends RecyclerView.ViewHolder {

    private View article_view;
    private SharedPreferences mSharedPreferences;
    @BindView(R.id.news_category) TextView category_view;
    @BindView(R.id.news_date) TextView date_view;
    @BindView(R.id.news_title) TextView title_view;
    @BindView(R.id.image_news) ImageView image_view;
    private static final String EXTRA_LINK = "linkaddress";
    private List<String> list_id_articles_read;
    private String list_articles_read;
    private Context context;


    public ArticleViewHolder(View itemView, Context context, SharedPreferences mSharedPreferences) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        article_view = itemView;
        list_id_articles_read = new ArrayList<>();
        this.mSharedPreferences=mSharedPreferences;
        this.context = context;

        if(mSharedPreferences!=null)
            list_articles_read=mSharedPreferences.getString("ID_ARTICLES_READ",null);
        else
            list_articles_read=null;

        mSharedPreferences.edit().putString("ID_ARTICLES_READ", null).apply();

        configure_list_articles_read();
    }

    private void configure_list_articles_read(){

        String[] new_list_articles_read;

        if(list_articles_read!=null){
            new_list_articles_read=list_articles_read.split(",");

            for(int i = 0;i<=new_list_articles_read.length-1;i++) {
                list_id_articles_read.add(new_list_articles_read[i]);
            }

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
                if(!has_been_read(article.getId())){
                    add_id_articles_in_list_articles_read(article.getId());
                    change_color_for_read_articles();
                }
                /*Intent intent = new Intent(context,WebActivity.class);
                intent.putExtra(EXTRA_LINK, article.getWebUrl());
                context.startActivity(intent);*/
            }
        });

        System.out.println("eeee end configure_list_articles_read");
    }

    @SuppressLint("ResourceAsColor")
    private void change_color_for_read_articles(){
        title_view.setTextColor(context.getResources().getColor(R.color.color_article_read));
        date_view.setTextColor(context.getResources().getColor(R.color.color_article_read));
        category_view.setTextColor(context.getResources().getColor(R.color.color_article_read));
    }

    private Boolean has_been_read(String article_id){

        if(list_id_articles_read!=null){
            for(String id : list_id_articles_read){
                if(id!=null){
                    if(id.equals(article_id)){
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private void add_id_articles_in_list_articles_read(String id_article) {
        list_id_articles_read=shift_values_from_table(list_id_articles_read,50,id_article);
        save_list_articles_read_in_sharedpref();
    }

    private List<String> shift_values_from_table(List<String> table, int max, String new_value){

        List<String> new_table = new ArrayList<>();;
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

            mSharedPreferences.edit().putString("ID_ARTICLES_READ", list_to_be_saved).apply();
        }
    }

}

