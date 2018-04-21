package com.g.laurent.mynews;

import com.g.laurent.mynews.Models.ListArticlesMostPopular;
import com.g.laurent.mynews.Models.ListArticlesSearch;
import com.g.laurent.mynews.Models.ListArticlesTopStories;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class RequestHTTP_test {

    @Test
    public void Test_Search_Request() throws Exception {

        final ListArticlesSearch listArticlesSearch = new ListArticlesSearch("trump",null,null,null, null);

        Thread.sleep(3000);

        assertTrue(listArticlesSearch.getListArticles().size()>0);
    }

    /*@Test
    public void Test_Most_Popular_Request() throws Exception {

        final ListArticlesMostPopular listArticlesMostPopular = new ListArticlesMostPopular("travel",null);

        Thread.sleep(3000);

        assertTrue(listArticlesMostPopular.getListArticles().size()>0);
    }*/

    @Test
    public void Test_TopStories_Request() throws Exception {

        final ListArticlesTopStories listArticlesTopStories = new ListArticlesTopStories("travel",null);

        Thread.sleep(3000);

        assertTrue(listArticlesTopStories.getListArticles().size()>0);
    }




/*


    @Test
    public void extract_date_correct() throws Exception {

        Article article = new Article("","2018-04-02","","","","","");
        assertEquals("02/04/2018", article.extract_date());
    }

    @Test
    public void extract_category() throws Exception {

        Article article = new Article("","2018-04-02","","","U.S.","Politics","");
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

@Test
public void recover_list(){



}



    @Test
    public void test_date(){

        assertEquals(true, is_the_date_ok("Begin date",2018,2,28));
        assertEquals(false, is_the_date_ok("Begin date",2018,3,21));
        assertEquals(true, is_the_date_ok("Begin date",2018,3,1));
        assertEquals(false, is_the_date_ok("Begin date",2018,4,21));

        assertEquals(true, is_the_date_ok("End date",2018,2,28));
        assertEquals(true, is_the_date_ok("End date",2018,2,21));
        assertEquals(true, is_the_date_ok("End date",2018,3,1));
        assertEquals(false, is_the_date_ok("End date",2018,4,21));


        /*01/04/2018     28/03/2018
          01/04/2018     21/03/2018
          01/04/2018     01/04/2018
          01/04/2018     21/04/2018


    }

    public boolean is_the_date_ok(String type_date, int year, int month, int day){

        boolean answer = true;

        Calendar dateToValidate = Calendar.getInstance();
        dateToValidate.set(Calendar.YEAR, year);
        dateToValidate.set(Calendar.MONTH, month);
        dateToValidate.set(Calendar.DAY_OF_MONTH, day);

        Calendar date_to_compare=null; // = Calendar.getInstance();
        /*date_to_compare.set(Calendar.YEAR, 2018);
        date_to_compare.set(Calendar.MONTH, 3);
        date_to_compare.set(Calendar.DAY_OF_MONTH, 1);

        switch(type_date){

            case "Begin date":

                // Check if begin date is before today's date and begin date before end date
                if(IsStrictlyAfter(dateToValidate,Calendar.getInstance()))
                    answer = false;

                if(date_to_compare!=null) {
                    if (IsStrictlyAfter(dateToValidate, date_to_compare))
                        answer = false;
                }
                break;

            case "End date":

                // Check if end date is before today's date and end date after begin date
                if(IsStrictlyAfter(dateToValidate,Calendar.getInstance()))
                    answer = false;

                if(date_to_compare!=null) {
                    if (IsStrictlyBefore(dateToValidate,date_to_compare))
                        answer = false;
                }
                break;
        }
        return answer;
    }

    public boolean IsStrictlyBefore(Calendar dateComp, Calendar dateRef){

        if (dateComp.get(Calendar.YEAR) < dateRef.get(Calendar.YEAR))
            return true;
        else if (dateComp.get(Calendar.YEAR) == dateRef.get(Calendar.YEAR)){
            if (dateComp.get(Calendar.MONTH) < dateRef.get(Calendar.MONTH))
                return true;
            else if (dateComp.get(Calendar.MONTH) == dateRef.get(Calendar.MONTH)) {
                if (dateComp.get(Calendar.DAY_OF_MONTH) < dateRef.get(Calendar.DAY_OF_MONTH))
                    return true;
                else
                    return false;
            }
            else
                return false;
        } else
            return false;
    }

    public boolean IsStrictlyAfter(Calendar dateComp, Calendar dateRef){

        if (dateComp.get(Calendar.YEAR) > dateRef.get(Calendar.YEAR))
            return true;
        else if (dateComp.get(Calendar.YEAR) == dateRef.get(Calendar.YEAR)){
            if (dateComp.get(Calendar.MONTH) > dateRef.get(Calendar.MONTH))
                return true;
            else if (dateComp.get(Calendar.MONTH) == dateRef.get(Calendar.MONTH)) {
                if (dateComp.get(Calendar.DAY_OF_MONTH) > dateRef.get(Calendar.DAY_OF_MONTH))
                    return true;
                else
                    return false;
            }
            else
                return false;
        } else
            return false;

    }

    @Test
    public void test_date_format(){
        assertEquals("20180201", create_date_format_yyyymmdd("01/02/2018"));
    }

    protected String create_date_format_yyyymmdd(String date){

        if(date.substring(4,5).equals("-")){
            String year = date.substring(0,4);
            String month = date.substring(5,7);
            String day = date.substring(8,10);

            return year.toUpperCase() + month.toUpperCase() + day.toUpperCase();

        } else if (date.substring(2,3).equals("/")){ // 01/02/2018

            String year = date.substring(6,10);
            String month = date.substring(3,5);
            String day = date.substring(0,2);

            return year.toUpperCase() + month.toUpperCase() + day.toUpperCase();

        } else
            return null;
    }

    @Test
    public void test_subjects(){

        assertEquals(true, is_tab_name_among_subjects("travel"));
        assertEquals(false, is_tab_name_among_subjects(null));
        assertEquals(false, is_tab_name_among_subjects("politic"));

    }

    @Test
    public void test_list_subjects(){

        assertEquals("arts,sport,travel,business", create_string_list());
    }

    @Test
    public void separate_subjects(){

        assertEquals("arts", getListCheckBoxOK("arts,sport,travel,business")[0]);
        assertEquals("sport", getListCheckBoxOK("arts,sport,travel,business")[1]);
        assertEquals("travel", getListCheckBoxOK("arts,sport,travel,business")[2]);
        assertEquals("business", getListCheckBoxOK("arts,sport,travel,business")[3]);

    }

    private String[] getListCheckBoxOK(String liste){

        return liste.split(",");

    }

    private String create_string_list(){

        ArrayList<String> ListSubjects = new ArrayList<String>();

        ListSubjects.add("arts");
        ListSubjects.add("sport");
        ListSubjects.add("travel");
        ListSubjects.add("business");

        StringBuilder list_subjects = new StringBuilder();

        for(String subject:ListSubjects) {
            list_subjects.append(subject);
            list_subjects.append(",");
        }

        if(list_subjects.length()>1 && list_subjects.substring(list_subjects.length()-1,list_subjects.length()).equals(","))
            list_subjects.deleteCharAt(list_subjects.length()-1);

        return list_subjects.toString();
    }


    private boolean is_tab_name_among_subjects(String tab_name) {
        boolean answer = false;
        /*String[] list_subjects = getResources().getStringArray(R.array.list_checkbox);

        for(String subject : list_subjects){
            if(subject.toLowerCase().equals(tab_name))
                answer = true;
        }
        return answer;
    }


    @Test
    public void test_articles_read(){

        String[] table = new String[5];

        table[0]="1";


        assertEquals("0,1," + null + "," + null + "," + null, add_id_articles_in_list_articles_read(table,"0"));
    }


    private String add_id_articles_in_list_articles_read(String[] list_id_articles_read, String id_article) {

        StringBuilder new_table = new StringBuilder();

        if(list_id_articles_read!=null){

            int size_table = list_id_articles_read.length;

            if(size_table < 5){

                for(int i = 0;i<size_table;i++){
                    if(list_id_articles_read[i]==null){
                        list_id_articles_read[i]=id_article;
                        break;
                    }
                }

            } else {
                // Shifting all id's from "index" to "index+1" in the table
                if(size_table>=2){

                    for(int i = size_table-2;i>=0;i--)
                        list_id_articles_read[i+1] = list_id_articles_read[i];
                    } else
                        list_id_articles_read[1] = list_id_articles_read[0];

                // Put the new id at the first place of the table
                list_id_articles_read[0]=id_article;
            }


            for (int i = 0; i < list_id_articles_read.length; i++) {
                if(i==list_id_articles_read.length-1)
                    new_table.append(list_id_articles_read[i]);
                else
                    new_table.append(list_id_articles_read[i]).append(",");
            }

        } else
            new_table.append(id_article);


        return new_table.toString();
        //mSharedPreferences.edit().putString("ID_ARTICLES_READ", new_table.toString()).apply();

    }


    @Test
    public void test_request(){

        assertEquals(1,1);
    }*/

}
