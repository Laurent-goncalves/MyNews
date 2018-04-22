package com.g.laurent.mynews;


import android.content.Context;
import android.content.SharedPreferences;
import android.test.mock.MockContext;

import com.g.laurent.mynews.Controllers.Fragments.NotifFragment;
import com.g.laurent.mynews.Models.Article;
import com.g.laurent.mynews.Models.ListArticlesSearch;
import com.g.laurent.mynews.Models.Search_request;

import org.junit.Test;
import org.mockito.Mockito;
import java.util.ArrayList;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class Notifications_test {

    @Test
    public void Test_recover_data(){

        final SharedPreferences sharedPrefs = mock(SharedPreferences.class);

        Mockito.when(sharedPrefs.getString("query",null)).thenReturn("trump");
        Mockito.when(sharedPrefs.getString("list_subjects",null)).thenReturn("Arts,Politics,Sports");
        Mockito.when(sharedPrefs.getBoolean("enable_notifications",false)).thenReturn(false);

        NotifFragment notifFragment = new NotifFragment();
        notifFragment.setSharedPreferences_Notif(sharedPrefs);

        ArrayList<String> ListSubjects = new ArrayList<>();
        ListSubjects.add("Arts");
        ListSubjects.add("Politics");
        ListSubjects.add("Sports");

        // Re-initialization of data
        notifFragment.setQuery(null);
        notifFragment.setEnable_notif(null);
        notifFragment.setListSubjects(null);

        // Data recovered
        notifFragment.recover_data();

        assertEquals("trump",notifFragment.getQuery());
        assertEquals(false,notifFragment.getEnable_notif());
        assertEquals(ListSubjects,notifFragment.getListSubjects());
    }

    @Test
    public void Test_notification_checking(){

        //final MockContext context = new MockContext();
        final SharedPreferences sharedPrefs = mock(SharedPreferences.class);

        ListArticlesSearch listArticlesSearch = mock(ListArticlesSearch.class);

        // CREATE LIST OF OLD IDs
        Mockito.when(sharedPrefs.getString("list_old_ID_notif",null)).thenReturn("ID1,ID2,ID3");

        // Mock void's
        Mockito.doNothing().when(listArticlesSearch).save_list_ID_articles_notif();
        Mockito.doNothing().when(listArticlesSearch).send_notification(anyString());

        // CREATE LIST OF NEW IDs
        ArrayList<Article> mlist_ID = new ArrayList<>();
        mlist_ID.add(new Article(null,null,null,null,null,null,"ID1"));
        mlist_ID.add(new Article(null,null,null,null,null,null,"ID2"));
        mlist_ID.add(new Article(null,null,null,null,null,null,"ID3"));
        mlist_ID.add(new Article(null,null,null,null,null,null,"ID4"));
        listArticlesSearch.setMlistArticles(mlist_ID);
        assertEquals("ID4",listArticlesSearch.getMlist_ID().get(3));

        // COMPARE
        listArticlesSearch.compare_lists_of_id_and_send_notification();


       //assertEquals(1,listArticlesSearch.count);
    }
}
