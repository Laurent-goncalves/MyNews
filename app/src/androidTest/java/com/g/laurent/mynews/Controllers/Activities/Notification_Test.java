package com.g.laurent.mynews.Controllers.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import com.g.laurent.mynews.Controllers.Fragments.NotifFragment;
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
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.core.IsAnything.anything;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class Notification_Test {

    NotifFragment notifFragment;

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);


    @Test
    public void Test_notification_settings_saving(){

        SharedPreferences mSharedPreferences = mActivityTestRule.getActivity().getApplicationContext().getSharedPreferences("NOTIFICATION_settings", Context.MODE_PRIVATE);
        mSharedPreferences.edit().putString("list_subjects_notif",null).apply();
        mSharedPreferences.edit().putString("query_notif",null).apply();
        mSharedPreferences.edit().putBoolean("enable_notifications",false).apply();


        try {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    mActivityTestRule.getActivity().configureAndShowNotifFragment();
                }
            });
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        waiting_time(500);
        onData(anything()).inAdapterView(withId(R.id.gridview_check_box)).atPosition(0).
                onChildView(withId(R.id.CheckBox)).perform(click());

        waiting_time(1000);
        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.query_area),
                        childAtPosition(
                                allOf(withId(R.id.setting_fragment_layout),
                                        childAtPosition(
                                                withId(R.id.activity_main_frame_layout),
                                                2)),
                                0),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("trump"), closeSoftKeyboard());

        waiting_time(1000);
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


        waiting_time(1000);
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

        waiting_time(1000);

        // Recover sharedpreferrences
        String list_subjects = mSharedPreferences.getString("list_subjects_notif",null);
        String query = mSharedPreferences.getString("query_notif", null);
        Boolean enable_end = mSharedPreferences.getBoolean("enable_notifications", false);

        // Check if data are saved correctly
        Assert.assertTrue(list_subjects.equals("Arts"));
        Assert.assertTrue(query.equals("trump"));
        Assert.assertTrue(enable_end);

    }

    private void waiting_time(int time){
        try {
            Thread.sleep(time);
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
