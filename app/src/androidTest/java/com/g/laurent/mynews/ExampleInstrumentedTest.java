package com.g.laurent.mynews;

import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import com.g.laurent.mynews.Models.ListArticlesSearch;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    @Test
    public void Test_Search_Request() throws Exception {

        final ListArticlesSearch listArticlesSearch = new ListArticlesSearch("trump",null,null,null, null);

        Handler mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message message) {

                new CountDownTimer(3000, 1000) {

                    public void onTick(long millisUntilFinished) {}

                    public void onFinish() {
                        System.out.println("eeee num_results=" + listArticlesSearch.getListArticles().size());
                        assertTrue(listArticlesSearch.getListArticles().size()>0);
                    }
                }.start();

            }
        };



    }


   /* @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.g.laurent.mynews", appContext.getPackageName());
    }*/

    // by clicking on the article, a webview is opened


    //



}
