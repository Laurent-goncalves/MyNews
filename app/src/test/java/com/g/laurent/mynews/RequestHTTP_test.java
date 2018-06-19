package com.g.laurent.mynews;

import android.content.SharedPreferences;
import android.test.mock.MockContext;

import com.g.laurent.mynews.Controllers.Activities.MainActivity;
import com.g.laurent.mynews.Controllers.Fragments.BaseFragment;
import com.g.laurent.mynews.Models.Article;
import com.g.laurent.mynews.Models.ListArticlesMostPopular;
import com.g.laurent.mynews.Models.ListArticlesSearch;
import com.g.laurent.mynews.Models.ListArticlesTopStories;
import com.g.laurent.mynews.Models.Search_request;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class RequestHTTP_test {

    //------------------------------------------------------------------------------------------------
    //----------------------------  TEST FOR HTTP REQUEST ------------------------------------------
    //------------------------------------------------------------------------------------------------

    @Test
    public void Test_Search_Request() throws Exception {

        final MockContext context = new MockContext();
        String api_key = "225a8498a05b4b7bb4d085d0c32e4ce8";
        Search_request search_request = new Search_request("search","trump",null,null,null);
        ListArticlesSearch listArticlesSearch = new ListArticlesSearch(context,api_key,search_request, (SharedPreferences) null);

        Thread.sleep(3000);

        assertTrue(listArticlesSearch.getListArticles().size()>0);
    }

    @Test
    public void Test_Most_Popular_Request() throws Exception {

        String api_key = "225a8498a05b4b7bb4d085d0c32e4ce8";
        final ListArticlesMostPopular listArticlesMostPopular = new ListArticlesMostPopular(null,api_key,"travel",null);

        Thread.sleep(3000);

        assertTrue(listArticlesMostPopular.getListArticles().size()>0);
    }

    @Test
    public void Test_TopStories_Request() throws Exception {

        String api_key = "225a8498a05b4b7bb4d085d0c32e4ce8";
        final ListArticlesTopStories listArticlesTopStories = new ListArticlesTopStories(null,api_key,"travel",null);

        Thread.sleep(3000);

        assertTrue(listArticlesTopStories.getListArticles().size()>0);
    }

    //------------------------------------------------------------------------------------------------
    //----------------------------  TEST ON ARTICLE METHODS ------------------------------------------
    //------------------------------------------------------------------------------------------------

    @Test
    public void Test_extract_date_correct() throws Exception {

        Article article = new Article("","2018-04-02","","","","","");
        assertEquals("02/04/2018", article.extract_date());
    }

    @Test
    public void Test_extract_category() throws Exception {

        Article article = new Article("","2018-04-02","","U.S.","Politics","","");
        assertEquals("U.S. > Politics", article.extract_category());

        article.setSection("U.S.");
        article.setSubsection(null);
        assertEquals("U.S.", article.extract_category());

        article.setSection(null);
        article.setSubsection("Politics");
        assertEquals("Politics", article.extract_category());

        article.setSection(null);
        article.setSubsection(null);
        assertEquals(null, article.extract_category());
    }

    //------------------------------------------------------------------------------------------------
    //----------------------------  TEST ON SEARCH METHODS ------------------------------------------
    //------------------------------------------------------------------------------------------------

    @Test
    public void test_date_OK_for_calendarView(){

        BaseFragment baseFragment = new BaseFragment();

        baseFragment.setDate_begin(null);

        Calendar date_end= Calendar.getInstance();
        date_end.set(Calendar.YEAR, 2018);
        date_end.set(Calendar.MONTH, 3);
        date_end.set(Calendar.DAY_OF_MONTH, 1);
        baseFragment.setDate_end(date_end);

        assertEquals(true, baseFragment.is_the_date_ok("Begin date",2018,2,28));
        assertEquals(false, baseFragment.is_the_date_ok("Begin date",2018,3,21));
        assertEquals(true, baseFragment.is_the_date_ok("Begin date",2018,3,1));
        assertEquals(false, baseFragment.is_the_date_ok("Begin date",2020,4,21));

        Calendar date_begin= Calendar.getInstance();
        date_begin.set(Calendar.YEAR, 2018);
        date_begin.set(Calendar.MONTH, 3);
        date_begin.set(Calendar.DAY_OF_MONTH, 1);
        baseFragment.setDate_begin(date_begin);

        baseFragment.setDate_end(null);

        assertEquals(false, baseFragment.is_the_date_ok("End date",2018,2,28));
        assertEquals(true, baseFragment.is_the_date_ok("End date",2018,3,2));
        assertEquals(true, baseFragment.is_the_date_ok("End date",2018,3,1));
        assertEquals(false, baseFragment.is_the_date_ok("End date",2020,4,21));

    }

    @Test
    public void test_date_format(){
        assertEquals("20180201", new BaseFragment().create_date_format_yyyymmdd("01/02/2018"));
    }

}
