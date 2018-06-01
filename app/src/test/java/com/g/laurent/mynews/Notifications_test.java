package com.g.laurent.mynews;

import android.content.SharedPreferences;
import android.test.mock.MockContext;
import com.g.laurent.mynews.Models.Article;
import com.g.laurent.mynews.Models.ListArticlesSearch;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import java.util.ArrayList;
import static org.mockito.Mockito.when;

public class Notifications_test {

    @Test
    public void Test_notification_sending(){

        MockContext context = new MockContext();
        SharedPreferences sharedpref = Mockito.mock(SharedPreferences.class);
        String api_key = "225a8498a05b4b7bb4d085d0c32e4ce8";
        ListArticlesSearch listArticlesSearch =new ListArticlesSearch(context,api_key,null, (SharedPreferences) null);

        // create list of new Articles with the new ID4
        ArrayList<Article> list_new_articles = new ArrayList<>();
        list_new_articles.add(new Article(null,null,null,null,null,null,"ID1"));
        list_new_articles.add(new Article(null,null,null,null,null,null,"ID2"));
        list_new_articles.add(new Article(null,null,null,null,null,null,"ID3"));
        list_new_articles.add(new Article(null,null,null,null,null,null,"ID4"));
        list_new_articles.add(new Article(null,null,null,null,null,null,"ID5"));

        listArticlesSearch.setMlistArticles(list_new_articles);
        listArticlesSearch.setSharedPreferences_Notif(sharedpref);

        when(sharedpref.getString("list_old_ID_notif",null)).thenReturn("ID1,ID2,ID3");

        listArticlesSearch.compare_lists_of_id_and_send_notification();

        waiting_time(1000);

        Assert.assertEquals(2, listArticlesSearch.count);
    }

    private void waiting_time(int time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



}
