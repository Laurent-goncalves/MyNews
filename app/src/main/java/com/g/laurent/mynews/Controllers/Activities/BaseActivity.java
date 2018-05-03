package com.g.laurent.mynews.Controllers.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.g.laurent.mynews.Models.Callback_list_subjects;
import com.g.laurent.mynews.Models.Callback_settings;
import com.g.laurent.mynews.R;

public class BaseActivity extends AppCompatActivity {

    /** DESCRIPTION : In the BaseActivity, we define and configure the toolbar and
     *  the tablayout.
     *  **/

    protected Callback_list_subjects callback_list_subjects;
    protected Callback_settings callback_save_settings;
    protected String tab_name;
    protected Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    protected String[] recover_list_tabs() {
        return getResources().getStringArray(R.array.list_tab);
    }

    protected void configureToolbar(String title){
        // Assign toolbar
        toolbar = findViewById(R.id.activity_main_toolbar);
        // Sets the Toolbar
        setSupportActionBar(toolbar);
    }

    public void update_list_subjects(View view) {

        String subject = null;

        // Find the relativelayout which is the parent of the view
        RelativeLayout relativeLayout = (RelativeLayout) view.getParent();

        // Find the textview related to the relativelayout
        for(int index = 0; index<relativeLayout.getChildCount(); index++) {
            View Child = relativeLayout.getChildAt(index);

            // for each child of the relativelayout, check if it's an instance of textview
            if(Child instanceof TextView){
                TextView textview = (TextView) Child;
                subject = textview.getText().toString();
            }
        }

        // Send information to fragment settings
        if(((CheckBox) view).isChecked())
            callback_list_subjects.update_list_subjects_in_fragment("add",subject);
        else
            callback_list_subjects.update_list_subjects_in_fragment("remove",subject);
    }

    public void setToolbar(Toolbar toolbar) {
        this.toolbar = toolbar;
    }
}
