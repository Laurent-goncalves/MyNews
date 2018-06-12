package com.g.laurent.mynews.Models;


public interface CallbackPageFragment {

    void finish_configure_recyclerView_mainActivity();

    void finish_configure_recyclerView_settingActivity();

    void display_error_message_mainActivity(String error);

    void display_error_message_settingActivity(String error);
}
