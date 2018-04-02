package com.g.laurent.mynews;

import com.g.laurent.mynews.Models.Article;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void extract_date_correct() throws Exception {

        Article article = new Article("","2018-04-02","","","","");
        assertEquals("02/04/2018", article.extract_date());
    }

    @Test
    public void extract_category() throws Exception {

        Article article = new Article("","2018-04-02","","U.S.","Politics","");
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


}