package com.g.laurent.mynews.Controllers.Activities;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.g.laurent.mynews.Controllers.Fragments.MainFragment;
import com.g.laurent.mynews.Controllers.Fragments.NotifFragment;
import com.g.laurent.mynews.Controllers.Fragments.SearchFragment;
import com.g.laurent.mynews.Models.AlarmReceiver;
import com.g.laurent.mynews.Models.Article;
import com.g.laurent.mynews.Models.Callback_list_subjects;
import com.g.laurent.mynews.Models.Callback_search;
import com.g.laurent.mynews.Models.Callback_settings;
import com.g.laurent.mynews.R;
import com.g.laurent.mynews.Utils.NewsStreams;
import com.g.laurent.mynews.Utils.Search.Doc;
import com.g.laurent.mynews.Utils.Search.ListArticles;
import com.g.laurent.mynews.Utils.Search.Multimedium;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;


public class MainActivity extends BaseActivity implements Callback_search, AlarmReceiver.callbackAlarm {

    @BindView(R.id.toolbar_menu_button) ImageButton icon_menu;
    @BindView(R.id.toolbar_menu_search) ImageButton icon_search;
    @BindView(R.id.toolbar_menu_notif) ImageButton icon_notif;
    @BindView(R.id.toolbar_title) TextView title_toolbar;
    @BindView(R.id.relative_layout_toolbar) RelativeLayout mRelativeLayout;
    @BindView(R.id.activity_main_frame_layout) LinearLayout mLinearLayout;
    private MainFragment mainFragment;
    private SearchFragment searchFragment;
    private NotifFragment notifFragment;
    public static final String EXTRA_TAB_NAME = "tab_name";
    private TabLayout tablayout;
    private String fragment_displayed;
    private SharedPreferences sharedPreferences_Notif;
    private Disposable disposable;
    private ArrayList<Article> mlistArticles;
    private ArrayList<String> list_ID_old;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        tab_name=null;
        fragment_displayed="mainfragment";
        String[] list_tabs = recover_list_tabs();

        if(tab_name==null){
            tab_name=list_tabs[0];
        }

        sharedPreferences_Notif = this.getSharedPreferences("NOTIFICATION_settings", Context.MODE_PRIVATE);

        this.configureAndShowMainFragment();
        this.configureAlarmManager();
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("eeee OnResume MainActivity");
        this.configureAndShowMainFragment();

    }

    // -------------- CONFIGURATION Fragment --------------------

    private void configureAndShowMainFragment(){

        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_TAB_NAME,tab_name);

        mainFragment = new MainFragment();
        mainFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.activity_main_frame_layout, mainFragment)
                .commit();

        if(tablayout==null)
            this.configureTabLayout();

        if(tablayout.getVisibility()==View.GONE)
            tablayout.setVisibility(View.VISIBLE);

        this.configureToolbar("MyNews");
        configure_popupmenu_icon_toolbar();
        fragment_displayed="mainfragment";
    }

    @Override
    public void configureAndShowMainFragmentSearchRequest(){

        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_TAB_NAME,"search request");

        mainFragment = new MainFragment();
        mainFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.activity_main_frame_layout, mainFragment)
                .commit();

        tablayout.setVisibility(View.GONE);

        this.configureToolbar("Search Articles");
        fragment_displayed="mainfragment";

    }

    private void configureAndShowNotifFragment(){

        notifFragment = new NotifFragment();
        callback_save_settings = (Callback_settings) notifFragment;
        callback_list_subjects = (Callback_list_subjects) notifFragment;
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.activity_main_frame_layout, notifFragment)
                .commit();

        this.configureToolbar("Notifications");
        fragment_displayed="notiffragment";
        tablayout.setVisibility(View.GONE);
    }

    private void configureAlarmManager(){

        // Configuration of alarm for saving feeling each day
        AlarmReceiver.callbackAlarm mcallbackAlarm=this;
        AlarmReceiver alarmReceiver = new AlarmReceiver();
        alarmReceiver.createCallbackAlarm(mcallbackAlarm);

        Intent alarmIntent = new Intent(getApplicationContext(), alarmReceiver.getClass());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Set the alarm to start at 7:00 a.m.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 7);
        calendar.set(Calendar.MINUTE,0);

        // Create alarm
        AlarmManager manager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        if(manager!=null)
            manager.setRepeating(AlarmManager.RTC,calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    private void configureAndShowSearchFragment(){

        searchFragment = new SearchFragment();
        callback_list_subjects = (Callback_list_subjects) searchFragment;
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.activity_main_frame_layout, searchFragment)
                .commit();

        this.configureToolbar("Search Articles");
        fragment_displayed="searchfragment";
        tablayout.setVisibility(View.GONE);
    }

    // -------------- CONFIGURATION TabLayout --------------------

    private void configureTabLayout(){

        String[] list_tabs = recover_list_tabs();
        tablayout = findViewById(R.id.activity_main_tabs);

        if (list_tabs != null) {
            for (String tab : list_tabs) {

                tablayout.addTab(tablayout.newTab().setText(tab));
                tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        tab_name = tab.getText().toString();
                        configureAndShowMainFragment();
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {
                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {
                    }
                });
            }
        }
    }

    // -------------- CONFIGURATION Toolbar & icons --------------------

    @Override
    protected void configureToolbar(String title){
        super.configureToolbar(title);

        if(toolbar!=null) {
            title_toolbar.setText(title);

            if(getSupportActionBar()!=null) {
                switch (title) {

                    case "MyNews":

                        icon_search.setVisibility(View.VISIBLE);
                        icon_menu.setVisibility(View.VISIBLE);
                        icon_notif.setVisibility(View.VISIBLE);
                        setIconOnClickListener();
                        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

                        break;
                    case "Search Articles":
                        icon_search.setVisibility(View.GONE);
                        icon_menu.setVisibility(View.GONE);
                        icon_notif.setVisibility(View.GONE);
                        // Enable the Up button
                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                        break;
                    case "Notifications":
                        icon_search.setVisibility(View.GONE);
                        icon_menu.setVisibility(View.GONE);
                        icon_notif.setVisibility(View.GONE);
                        // Enable the Up button
                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                        break;
                }
            }
        }
    }

    private void setIconOnClickListener(){

        icon_menu.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
            }
        });

        icon_search.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                configureToolbar("Search Articles");
                configureAndShowSearchFragment();
            }
        });
    }

    private void configure_popupmenu_icon_toolbar(){

        icon_notif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //Creating the instance of PopupMenu
            PopupMenu popup = new PopupMenu(MainActivity.this, icon_notif);
            //Inflating the Popup using xml file
            popup.getMenuInflater().inflate(R.menu.menu_toolbar, popup.getMenu());

            //registering popup with OnMenuItemClickListener
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    Toast toast;
                    switch (item.getItemId()){
                        case R.id.notifications:
                            configureAndShowNotifFragment();
                            return true;
                        case R.id.help:
                            toast = Toast.makeText(getApplicationContext(),"Item help selected",Toast.LENGTH_LONG);
                            toast.show();
                            return true;
                        case R.id.about:
                            toast = Toast.makeText(getApplicationContext(),"Item about selected",Toast.LENGTH_LONG);
                            toast.show();
                            return true;
                        default:
                            return false;
                    }
                }
            });

            popup.show();//showing popup menu
        }
    });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if(fragment_displayed.equals("notiffragment"))
                    callback_save_settings.save_data();
                configureAndShowMainFragment();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void notification_request_checking() {

        Boolean enable = sharedPreferences_Notif.getBoolean("enable_notifications",false);

        if(enable){

            String new_query = sharedPreferences_Notif.getString("query",null);
            String list_subj = sharedPreferences_Notif.getString("list_subjects_notif",null);
            if(list_subj!=null){
                list_ID_old = string_transform_to_list(list_subj);
                // Launch a new search request to check if there are new articles
                launch_search_request(new_query,list_subj,null,null);
            }
        }
    }

    private void launch_search_request(String query,String subject, String begin_date,String end_date){

        this.disposable = NewsStreams.streamFetchgetListArticles(query,subject, begin_date,end_date).subscribeWith(new DisposableObserver<ListArticles>() {

            @Override
            public void onNext(ListArticles listArticles) {

                int count = 0;
                Build_data_SearchArticles(listArticles);
                save_list_ID_articles_notif();

                // For each article, check if it's in the list
                if(mlistArticles!=null){
                    for(Article article : mlistArticles){

                        if(!is_ID_in_the_list(article.getId(),list_ID_old)) { // if the article is NEW
                            send_notification(article.getTitle());
                            count++;
                        }
                    }
                }

                if(count==0)
                    send_notification(null);

                // Save the new list of ID articles
                save_list_ID_articles_notif();
            }

            @Override
            public void onError(Throwable e) {
                Log.e("TAG","On Error"+Log.getStackTraceString(e));
            }

            @Override
            public void onComplete() {
                Log.e("TAG","On Complete !!");
            }
        });
    }

    private void Build_data_SearchArticles(ListArticles listArticles) {

        mlistArticles = new ArrayList<>();

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

    protected void save_list_ID_articles_notif() {

        ArrayList<String> List_ID = new ArrayList<>();

        if(mlistArticles!=null) {
            for (Article article : mlistArticles)
                List_ID.add(article.getId());
        }

        sharedPreferences_Notif.edit().putString("list_subjects_notif",list_transform_to_String(List_ID)).apply();
    }

    protected String list_transform_to_String(ArrayList<String> list){

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

    private String getImageUrlSearch(Doc doc){

        if(doc!=null){
            if (doc.getMultimedia() !=null){
                List<Multimedium> multimediumList = doc.getMultimedia();

                for(Multimedium multimedium : multimediumList){
                    if(multimedium.getUrl()!=null && !multimedium.getUrl().equals("")) {
                        return multimedium.getUrl();
                    }
                }
            }
        }
        return null;
    }

    private ArrayList<String> string_transform_to_list(String list_string){

        ArrayList<String> new_list_subjects = new ArrayList<>();
        String[] mlist_subjects;

        if(list_string!=null){
            mlist_subjects=list_string.split(",");

            for(int i = 0;i<=mlist_subjects.length-1;i++) {
                new_list_subjects.add(mlist_subjects[i]);
            }

        } else {
            new_list_subjects=null;
        }
        return new_list_subjects;
    }

    private void send_notification(String title_article){

        String title_notif = null;

        if(title_article==null)
            title_notif = "No new article!";
        else
            title_notif = "New article!";

        String CHANNEL_ID = "my_channel_01";

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

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

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
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


}
