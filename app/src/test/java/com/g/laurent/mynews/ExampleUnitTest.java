package com.g.laurent.mynews;

import com.g.laurent.mynews.Models.Article;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;

import io.reactivex.disposables.Disposable;
import retrofit2.Call;
import retrofit2.Callback;

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



    @Test
    public void executeHttpRequestWithRetrofit(){

        final Disposable disposable;

        // Execute the stream subscribing to Observable defined inside GithubStream
        //disposable = NewsStreams.streamFetchNYTarticles("225a8498a05b4b7bb4d085d0c32e4ce8","newest",1)


        Callback<Integer> callback = new Callback<Integer>(){
            @Override
            public void onResponse(Call<Integer> call, retrofit2.Response<Integer> response) {
                System.out.println("eeeee " + response.code());
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {

            }};




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

         */
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
        date_to_compare.set(Calendar.DAY_OF_MONTH, 1);*/

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
        assertEquals("20180615", create_date_format_yyyymmdd("2018-06-15 GMT "));
    }

    protected String create_date_format_yyyymmdd(String date){

        String year = date.substring(0,4);
        String month = date.substring(5,7);
        String day = date.substring(8,10);

        return year.toUpperCase() + month.toUpperCase() + day.toUpperCase();
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
        }*/
        return answer;
    }

}