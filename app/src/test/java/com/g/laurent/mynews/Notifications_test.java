package com.g.laurent.mynews;


import android.content.SharedPreferences;
import android.test.mock.MockContext;

import com.g.laurent.mynews.Controllers.Fragments.NotifFragment;
import com.g.laurent.mynews.Models.ListArticlesSearch;
import com.g.laurent.mynews.Models.Search_request;

import org.junit.Test;
import org.mockito.Mockito;
import java.util.ArrayList;
import static org.junit.Assert.assertEquals;

public class Notifications_test {

    @Test
    public void Test_recover_data(){

        final SharedPreferences sharedPrefs = Mockito.mock(SharedPreferences.class);

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

        final MockContext context = new MockContext();
        Search_request search_request = new Search_request("search","trump",null,null,null);
        ListArticlesSearch listArticlesSearch = new ListArticlesSearch(context, search_request, null, null);

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }



    }


}
