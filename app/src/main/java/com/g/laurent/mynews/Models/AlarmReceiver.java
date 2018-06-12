package com.g.laurent.mynews.Models;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class AlarmReceiver extends BroadcastReceiver {

    private static final String EXTRA_NOTIF_SETTINGS = "NOTIFICATION_settings";
    private static final String EXTRA_QUERY_NOTIF = "query_notif";
    private static final String EXTRA_SUBJECTS_NOTIF = "list_subjects_notif";
    private static final String EXTRA_API_KEY = "api_key";
    private static final String NOTIF_NEW_ID_SEARCH = "create_new_list_id_notif_for_checking";
    private String api_key;

    @Override
    public void onReceive(Context context, Intent intent) {

        // recover the variable query and list subjects in sharedpreferences
        SharedPreferences sharedPreferences_Notif = context.getSharedPreferences(EXTRA_NOTIF_SETTINGS, Context.MODE_PRIVATE);
        String new_query = sharedPreferences_Notif.getString(EXTRA_QUERY_NOTIF,null);
        String list_subj = sharedPreferences_Notif.getString(EXTRA_SUBJECTS_NOTIF,null);

        if(intent!=null){
            if(intent.getExtras()!=null)
                api_key = intent.getExtras().getString(EXTRA_API_KEY,null);
        }

        if(list_subj!=null && new_query!=null && api_key!=null) {

            // create a new request for search
            Search_request search_request = new Search_request(NOTIF_NEW_ID_SEARCH,new_query, list_subj, null, null);
            // Launch a new search request to check if there are new articles
            new ListArticlesSearch(context,api_key, search_request, sharedPreferences_Notif);
        }
    }
}
