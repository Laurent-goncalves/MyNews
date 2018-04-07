package com.g.laurent.mynews.Controllers;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.view.menu.ListMenuItemView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Switch;
import android.widget.TextView;

import com.g.laurent.mynews.R;
import com.g.laurent.mynews.Views.GridViewAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends Fragment {

    @BindView(R.id.query_area) EditText query_area;
    @BindView(R.id.gridview_check_box) GridView grid_checkbox;
    @BindView(R.id.toggle_enabling_notif) Switch toggle_notif;
    @BindView(R.id.button_search) Button search_button;
    @BindView(R.id.text_begin_date) TextView text_begin_date;
    @BindView(R.id.text_end_date) TextView text_end_date;
    @BindView(R.id.list_begin_date) ListMenuItemView begin_date_list;
    @BindView(R.id.list_end_date) ListMenuItemView end_date_list;
    @BindView(R.id.line_separator) View line_separator;


    public SettingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.settings_fragment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    private void configure_settings_areas(){



    }

    private void configure_checkboxes(){
        grid_checkbox.setAdapter(new GridViewAdapter(getContext(),getResources().getStringArray(R.array.list_checkbox)));
    }

}
