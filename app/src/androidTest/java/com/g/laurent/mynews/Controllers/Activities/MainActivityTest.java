package com.g.laurent.mynews.Controllers.Activities;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.g.laurent.mynews.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void mainActivityTest() {
        // Click on toolbar 3 dots
        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.toolbar_menu_notif),
                        childAtPosition(
                                allOf(withId(R.id.relative_layout_toolbar),
                                        childAtPosition(
                                                withId(R.id.activity_main_toolbar),
                                                0)),
                                3),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        // Click on notification item
        ViewInteraction appCompatTextView = onView(
                allOf(withId(R.id.title), withText("Notifications"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.v7.view.menu.ListMenuItemView")),
                                        0),
                                0),
                        isDisplayed()));
        appCompatTextView.perform(click());

        // Click in query area & add the query "query"
        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.query_area),
                        childAtPosition(
                                allOf(withId(R.id.setting_fragment_layout),
                                        childAtPosition(
                                                withId(R.id.activity_main_frame_layout),
                                                2)),
                                0),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("query"), closeSoftKeyboard());

        //pressBack(); // press back to remove keyboard

        // select subject in gridView

 /*       ViewInteraction appCompatCheckBox = onView(
                allOf(withId(R.id.CheckBox),
                        childAtPosition(
                                allOf(withId(R.id.item_checkbox),
                                        withParent(withId(R.id.gridview_check_box))),
                                2),
                        isDisplayed()));
        appCompatCheckBox.perform(click());

        ViewInteraction appCompatCheckBox2 = onView(
                allOf(withId(R.id.CheckBox),
                        childAtPosition(
                                allOf(withId(R.id.item_checkbox),
                                        withParent(withId(R.id.gridview_check_box))),
                                1),
                        isDisplayed()));
        appCompatCheckBox2.perform(click());

    */









        // click on switch button to enable notification
        ViewInteraction switch_ = onView(
                allOf(withId(R.id.toggle_enabling_notif), withText("Enable notifications (once per day)"),
                        childAtPosition(
                                allOf(withId(R.id.setting_fragment_layout),
                                        childAtPosition(
                                                withId(R.id.activity_main_frame_layout),
                                                2)),
                                4),
                        isDisplayed()));
        switch_.perform(click());

        // click on switch button to disable notification
        ViewInteraction switch_2 = onView(
                allOf(withId(R.id.toggle_enabling_notif), withText("Enable notifications (once per day)"),
                        childAtPosition(
                                allOf(withId(R.id.setting_fragment_layout),
                                        childAtPosition(
                                                withId(R.id.activity_main_frame_layout),
                                                2)),
                                4),
                        isDisplayed()));
        switch_2.perform(click());

        // click on arrow to return to mainfragment
        ViewInteraction appCompatImageButton2 = onView(
                allOf(withContentDescription("Navigate up"),
                        childAtPosition(
                                allOf(withId(R.id.activity_main_toolbar),
                                        childAtPosition(
                                                withId(R.id.activity_main_frame_layout),
                                                0)),
                                3),
                        isDisplayed()));
        appCompatImageButton2.perform(click());

        // click on search button
        ViewInteraction appCompatImageButton3 = onView(
                allOf(withId(R.id.toolbar_menu_search),
                        childAtPosition(
                                allOf(withId(R.id.relative_layout_toolbar),
                                        childAtPosition(
                                                withId(R.id.activity_main_toolbar),
                                                0)),
                                2),
                        isDisplayed()));
        appCompatImageButton3.perform(click());

        // click in query area & write "query"
        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.query_area),
                        childAtPosition(
                                allOf(withId(R.id.setting_fragment_layout),
                                        childAtPosition(
                                                withId(R.id.activity_main_frame_layout),
                                                2)),
                                0),
                        isDisplayed()));
        appCompatEditText2.perform(replaceText("query"), closeSoftKeyboard());

        // select begin date
        ViewInteraction appCompatImageView = onView(
                allOf(withId(R.id.icon_expand),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.begin_date_selector),
                                        1),
                                1),
                        isDisplayed()));
        appCompatImageView.perform(click());

       // pressBack();

        // select end date
        ViewInteraction appCompatImageView2 = onView(
                allOf(withId(R.id.icon_expand),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.end_date_selector),
                                        1),
                                1),
                        isDisplayed()));
        appCompatImageView2.perform(click());

        // select subjects
        ViewInteraction appCompatCheckBox3 = onView(
                allOf(withId(R.id.CheckBox),
                        childAtPosition(
                                allOf(withId(R.id.item_checkbox),
                                        withParent(withId(R.id.gridview_check_box))),
                                0),
                        isDisplayed()));
        appCompatCheckBox3.perform(click());

        // click on search
        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.button_search), withText("SEARCH"),
                        childAtPosition(
                                allOf(withId(R.id.setting_fragment_layout),
                                        childAtPosition(
                                                withId(R.id.activity_main_frame_layout),
                                                2)),
                                4),
                        isDisplayed()));
        appCompatButton.perform(click());

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
