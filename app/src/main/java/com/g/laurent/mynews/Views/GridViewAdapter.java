package com.g.laurent.mynews.Views;


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

    public GridViewAdapter(Context c,String[] list_tabs){
        mContext = c;
        this.list_tabs = list_tabs;
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        grid = inflater.inflate(R.layout.checkbox_item, null);

        TextView title = (TextView) grid.findViewById(R.id.text_CheckBox);
        CheckBox checkBox = (CheckBox) grid.findViewById(R.id.CheckBox);

        title.setText(list_tabs[position]);

        return grid;
    }
}
