package com.g.laurent.mynews.Controllers.Activities;

import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import com.g.laurent.mynews.Controllers.Fragments.SearchFragment;
import com.g.laurent.mynews.R;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.core.IsAnything.anything;
import static org.hamcrest.core.IsNot.not;


@LargeTest
@RunWith(AndroidJUnit4.class)
public class Search_request_Test {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class,false,false);

    @Test
    public void Test_enable_or_disable_button_search() throws Exception {

        mActivityTestRule.launchActivity(null);

        mActivityTestRule.getActivity().setToolbar(null);
        mActivityTestRule.getActivity().configureAndShowSearchFragment();

        // check if search button is disabled
        onView(withId(R.id.button_search)).check(matches(not(isEnabled())));

        // click on one checkbox
        onData(anything()).inAdapterView(withId(R.id.gridview_check_box)).atPosition(0).
                onChildView(withId(R.id.CheckBox)).perform(click());

        // add a query
        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.query_area),
                        childAtPosition(
                                allOf(withId(R.id.setting_fragment_layout),
                                        childAtPosition(
                                                withId(R.id.activity_main_frame_layout),
                                                2)),
                                0),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("text"), closeSoftKeyboard());

        // wait 1 sec
        waiting_time();

        // check if search button is enabled
        onView(withId(R.id.button_search)).check(matches(isClickable()));

        // click again on the checkbox to deselect it
        onData(anything()).inAdapterView(withId(R.id.gridview_check_box)).atPosition(0).
                onChildView(withId(R.id.CheckBox)).perform(click());

        // check if search button is disabled
        onView(withId(R.id.button_search)).check(matches(not(isClickable())));
        mActivityTestRule.finishActivity();
    }

    @Test
    public void Test_gridview_update_list_subjects(){

        mActivityTestRule.launchActivity(null);
        mActivityTestRule.getActivity().setToolbar(null);
        mActivityTestRule.getActivity().configureAndShowSearchFragment();
        SearchFragment searchFragment = mActivityTestRule.getActivity().getSearchFragment();

        // click on Arts checkbox to add it and check the list updated
        onData(anything()).inAdapterView(withId(R.id.gridview_check_box)).atPosition(0).
                onChildView(withId(R.id.CheckBox)).perform(click());
        Assert.assertTrue(searchFragment.getListSubjects().toString().equals("[Arts]"));

        // click on Business checkbox to add it and check the list updated
        onData(anything()).inAdapterView(withId(R.id.gridview_check_box)).atPosition(1).
                onChildView(withId(R.id.CheckBox)).perform(click());
        Assert.assertTrue(searchFragment.getListSubjects().toString().equals("[Arts, Business]"));

        // click on Travel checkbox to add it and check the list updated
        onData(anything()).inAdapterView(withId(R.id.gridview_check_box)).atPosition(5).
                onChildView(withId(R.id.CheckBox)).perform(click());
        Assert.assertTrue(searchFragment.getListSubjects().toString().equals("[Arts, Business, Travel]"));

        // click on Arts checkbox to remove it and check the list updated
        onData(anything()).inAdapterView(withId(R.id.gridview_check_box)).atPosition(0).
                onChildView(withId(R.id.CheckBox)).perform(click());
        Assert.assertTrue(searchFragment.getListSubjects().toString().equals("[Business, Travel]"));
        mActivityTestRule.finishActivity();
    }

    private void waiting_time(){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
}
