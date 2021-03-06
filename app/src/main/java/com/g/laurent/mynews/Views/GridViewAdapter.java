package com.g.laurent.mynews.Views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import com.g.laurent.mynews.R;

public class GridViewAdapter extends BaseAdapter {

    private Context mContext;
    private String[] list_tabs;
    private String[] list_checkbox_OK;
    public TextView title;

    public GridViewAdapter(Context c,String[] list_tabs, String[] list_checkbox_OK){
        mContext = c;
        this.list_tabs = list_tabs;
        this.list_checkbox_OK=list_checkbox_OK;
    }

    @Override
    public int getCount() {
        return list_tabs.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View grid = null;

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(inflater!=null) {
            grid = inflater.inflate(R.layout.checkbox_item,null);

            title = grid.findViewById(R.id.text_CheckBox);
            CheckBox checkBox = grid.findViewById(R.id.CheckBox);

            title.setText(list_tabs[position]);

            if (include_in_list_checkboxOK(list_checkbox_OK, list_tabs[position]))
                checkBox.setChecked(true);
        }

        return grid;
    }

    private boolean include_in_list_checkboxOK(String[] list_checkbox_ok, String title) {

        if(list_checkbox_ok!=null && title != null){
            for(String subject : list_checkbox_ok){

                if(subject.equals(title))
                    return true;
            }
        }
        return false;
    }
}
