package com.g.laurent.mynews.Controllers.Activities;

import android.content.SharedPreferences;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import com.g.laurent.mynews.Controllers.Fragments.PageFragment;
import com.g.laurent.mynews.Models.Article;
import com.g.laurent.mynews.R;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.util.ArrayList;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    ArrayList<Article> listarticles;
    PageFragment mPageFragment;

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class,false,false);

    @Test
    public void Test_read_articles() {
        mActivityTestRule.launchActivity(null);
        mPageFragment =mActivityTestRule.getActivity().getPageFragment();
        SharedPreferences mSharedPreferences = getDefaultSharedPreferences(mActivityTestRule.getActivity().getApplicationContext());
        mSharedPreferences.edit().putString("ID_ARTICLES_READ",null).apply();

        // Create list of fake articles
        listarticles = new ArrayList<>();
        listarticles.add(new Article(null,"dd/mm/yyyy","article1",null,null,null,"ID1"));
        listarticles.add(new Article(null,"dd/mm/yyyy","article2",null,null,null,"ID2"));
        listarticles.add(new Article(null,"dd/mm/yyyy","article3",null,null,null,"ID3"));
        listarticles.add(new Article(null,"dd/mm/yyyy","article4",null,null,null,"ID4"));

        // configure and test recyclerView
        try {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    mPageFragment.configureRecyclerView(listarticles);
                }
            });
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        waiting_time(1000);
        click_on_recyclerView_article(1);
        click_on_recyclerView_article(2);
        click_on_recyclerView_article(0);


        String list_articles_read = mSharedPreferences.getString("ID_ARTICLES_READ",null);
        //System.out.println("eee list_articles_read="+list_articles_read);
        Assert.assertTrue(list_articles_read.equals("ID1,ID3,ID2,"));
        mActivityTestRule.finishActivity();
    }

    private void click_on_recyclerView_article(int position){
        onView(withId(R.id.fragment_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(position,
                        click()));
        waiting_time(1000);
    }

    private void waiting_time(int time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

