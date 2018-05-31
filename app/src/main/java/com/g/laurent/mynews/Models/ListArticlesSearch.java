package com.g.laurent.mynews.Models;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import com.g.laurent.mynews.R;
import com.g.laurent.mynews.Utils.NewsStreams;
import com.g.laurent.mynews.Utils.Search.Doc;
import com.g.laurent.mynews.Utils.Search.ListArticles;
import com.g.laurent.mynews.Utils.Search.Multimedium;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

public class ListArticlesSearch implements Disposable {

    private Disposable disposable;
    private ArrayList<Article> mlistArticles;
    private final String api_key;
    private String query;
    private String filterq;
    private String begindate;
    private String enddate;
    private String type_search;
    private CallbackPageFragment mCallbackPageFragment;
    private SharedPreferences sharedPreferences_Notif;
    private static final String EXTRA_OLD_ID_NOTIF = "list_old_ID_notif";
    private static final String NOTIF_INITIAL_ID_SEARCH = "create_initial_list_id_notif";
    private static final String NOTIF_NEW_ID_SEARCH = "create_new_list_id_notif_for_checking";
    private Context context;
    public int count;

    // ------------------------------------------ CONSTRUCTORS -----------------------------------------------
    public ListArticlesSearch(Context context, String api_key, Search_request search_request, CallbackPageFragment mCallbackPageFragment){

        // CONSTRUCTOR FOR SEARCH REQUEST (tab "travel" and search request)

        if(search_request!=null){
            this.type_search=search_request.getType_search();
            this.query=search_request.getQuery();
            this.filterq=search_request.getFilterq();
            this.begindate=search_request.getBegindate();
            this.enddate=search_request.getEnddate();
        }

        this.api_key=api_key;
        this.context=context;
        this.mlistArticles=new ArrayList<>();
        this.mCallbackPageFragment = mCallbackPageFragment;

        launch_request_search_articles();
    }

    public ListArticlesSearch(Context context, String api_key, Search_request search_request, SharedPreferences sharedPreferences_Notif){

        // CONSTRUCTOR FOR SEARCH REQUEST FOR NOTIFICATION
        this.sharedPreferences_Notif=sharedPreferences_Notif;

        if(search_request!=null){
            this.type_search=search_request.getType_search();
            this.query=search_request.getQuery();
            this.filterq=search_request.getFilterq();
            this.begindate=search_request.getBegindate();
            this.enddate=search_request.getEnddate();
        }

        this.api_key=api_key;
        this.context=context;
        this.mlistArticles=new ArrayList<>();

        launch_request_search_articles_notification();
    }

    // ------------------------------------------ API REQUESTS -----------------------------------------------

    private void launch_request_search_articles_notification() {

        this.disposable = NewsStreams.streamFetchgetListArticles(api_key, query, filterq, begindate,enddate).subscribeWith(new DisposableObserver<ListArticles>() {

            @Override
            public void onNext(ListArticles listArticles) {
                Build_data_SearchArticles(listArticles);
            }

            @Override
            public void onError(Throwable e) {
                Log.e("TAG","On Error"+Log.getStackTraceString(e));
            }

            @Override
            public void onComplete() {

                if(type_search!=null) {

                    // if the search consists in initializing the list of ID of articles
                    if (type_search.equals(NOTIF_INITIAL_ID_SEARCH))
                        save_list_ID_articles_notif();

                    // if the search consists in getting a new list of ID of articles for comparison with old list of ID
                    if (type_search.equals(NOTIF_NEW_ID_SEARCH))
                        compare_lists_of_id_and_send_notification();
                }
            }
        });
    }

    private void launch_request_search_articles(){

        this.disposable = NewsStreams.streamFetchgetListArticles(api_key, query, filterq, begindate,enddate).subscribeWith(new DisposableObserver<ListArticles>() {

            @Override
            public void onNext(ListArticles listArticles) {
                Build_data_SearchArticles(listArticles);
            }

            @Override
            public void onError(Throwable e) {
                Log.e("TAG","On Error"+Log.getStackTraceString(e));
            }

            @Override
            public void onComplete() {
                if(mCallbackPageFragment !=null)
                    mCallbackPageFragment.launch_configure_recycler_view();
            }
        });
    }

    private void Build_data_SearchArticles(ListArticles listArticles) {

        if(listArticles!=null){
            if(listArticles.getResponse()!=null) {
                if(listArticles.getResponse().getDocs() != null){

                    List<Doc> mDoc = listArticles.getResponse().getDocs();

                    if (mDoc != null) {

                        for(Doc doc : mDoc) {
                            mlistArticles.add(new Article(getImageUrlSearch(doc),
                                    doc.getPubDate(),
                                    doc.getHeadline().getMain(),
                                    doc.getSectionName(),null,doc.getWebUrl(),doc.getId()));

                        }
                    }
                }
            }
        }
    }

    private String getImageUrlSearch(Doc doc){

        if(doc!=null){
            if (doc.getMultimedia() !=null){
                List<Multimedium> multimediumList = doc.getMultimedia();

                for(Multimedium multimedium : multimediumList){
                    if(multimedium.getUrl()!=null && !multimedium.getUrl().equals("")) {
                        return "https://static01.nyt.com/" + multimedium.getUrl();
                    }
                }
            }
        }
        return null;
    }

    // ------------------------------- GETTER and SETTER ----------------------------------------------------

    public void setSharedPreferences_Notif(SharedPreferences sharedPreferences_Notif) {
        this.sharedPreferences_Notif = sharedPreferences_Notif;
    }

    public ArrayList<Article> getListArticles(){
        return mlistArticles;
    }

    public void setMlistArticles(ArrayList<Article> mlistArticles) {
        this.mlistArticles = mlistArticles;
    }

    // ------------------------------- NOTIFICATION AREA -----------------------------------------------------

    public void compare_lists_of_id_and_send_notification(){

        ArrayList<String> list_ID_old;

        if(sharedPreferences_Notif!=null)
            list_ID_old = string_transform_to_list(sharedPreferences_Notif.getString(EXTRA_OLD_ID_NOTIF,null));
        else
            list_ID_old=null;

        count = 0;

        // Save the new list of ID articles
        try{
            save_list_ID_articles_notif();
        } catch(Throwable ignored){}

        // For each article, check if it's in the list
        if(mlistArticles!=null){
            for(Article article : mlistArticles){

                if(!is_ID_in_the_list(article.getId(), list_ID_old)) { // if the article is NEW
                    send_notification(article.getTitle());
                    count++;
                }
            }
        }

        if(count==0)
            send_notification(null);
    }

    private void save_list_ID_articles_notif() {

        ArrayList<String> List_ID = new ArrayList<>();

        if(mlistArticles!=null) {
            for (Article article : mlistArticles)
                List_ID.add(article.getId());
        }

        if(sharedPreferences_Notif != null)
            sharedPreferences_Notif.edit().putString(EXTRA_OLD_ID_NOTIF,list_transform_to_String(List_ID)).apply();
    }

    private String list_transform_to_String(ArrayList<String> list){

        StringBuilder list_subjects = new StringBuilder();

        // Build the list_subjects in a single String (each subject is separated by a ",")
        for(String subject:list) {
            list_subjects.append(subject);
            list_subjects.append(",");
        }

        // Remove the last ","
        if(list_subjects.length()>1){
            if(list_subjects.substring(list_subjects.length()-1,list_subjects.length()).equals(",")){
                list_subjects.deleteCharAt(list_subjects.length()-1);
            }
        }

        return list_subjects.toString();

    }

    private ArrayList<String> string_transform_to_list(String list_string){

        ArrayList<String> new_list_subjects = new ArrayList<>();
        String[] mlist_subjects;

        if(list_string!=null){
            mlist_subjects=list_string.split(",");

            Collections.addAll(new_list_subjects, mlist_subjects);

        } else {
            new_list_subjects=null;
        }
        return new_list_subjects;
    }

    private void send_notification(String title_article){

        String title_notif;

        if(title_article==null)
            title_notif = "No new article!";
        else
            title_notif = "New article!";

        String CHANNEL_ID = "my_channel_01";

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            CharSequence name = "my_channel";
            String Description = "This is my channel";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(Description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(mChannel);
            }
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title_notif)
                .setContentText(title_article);

        if (notificationManager != null)
            notificationManager.notify(1, builder.build());
    }

    private Boolean is_ID_in_the_list(String id, ArrayList<String> list_oldID){

        if(id!=null && list_oldID!=null){
            for(String id_old : list_oldID){
                if(id_old!=null){
                    if(id_old.equals(id)){
                        return true;
                    }
                }
            }
        }

        return false;
    }

    @Override
    public void dispose() {
        if (this.disposable != null && !this.disposable.isDisposed()) this.disposable.dispose();
    }

    @Override
    public boolean isDisposed() {
        return false;
    }
}
