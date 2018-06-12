package com.g.laurent.mynews.Controllers.Activities;

import android.content.SharedPreferences;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.g.laurent.mynews.Controllers.Fragments.PageFragment;
import com.g.laurent.mynews.Models.Article;
import com.g.laurent.mynews.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.util.ArrayList;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

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

        waiting_time(2000);

        mPageFragment =mActivityTestRule.getActivity().getPageAdapter().getItem(1);

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

        waiting_time(3000);
        click_on_recyclerView_article(1);
        click_on_recyclerView_article(2);
        click_on_recyclerView_article(0);

        String list_articles_read = mSharedPreferences.getString("ID_ARTICLES_READ",null);
        Assert.assertTrue(list_articles_read.equals("ID1,ID3,ID2,"));
        mActivityTestRule.finishActivity();
    }

    private void click_on_recyclerView_article(int position){

        onView(allOf(withId(R.id.fragment_recycler_view),
                childAtPosition(withClassName(is("android.widget.FrameLayout")),
                        position)));
        waiting_time(1000);
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }

    private void waiting_time(int time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

