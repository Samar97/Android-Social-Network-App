package com.example.samarjeet.connect;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by samarjeet on 4/10/17.
 */

public class CommentsAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<String> cnameList = new ArrayList<String>();
    private ArrayList<String> textList = new ArrayList<String>();
    private ArrayList<String> timestampList = new ArrayList<String>();
    private Context context;

    public static final String TAG = "Adapter";

    public CommentsAdapter(ArrayList<String> lcnameList, ArrayList<String> ltextList, ArrayList<String> ltimestampList, Context mycontext) {
        cnameList = lcnameList;
        textList = ltextList;
        timestampList = ltimestampList;
        context = mycontext;
    }

    @Override
    public int getCount() {
        return cnameList.size();
    }

    @Override
    public Object getItem(int pos) {
        return cnameList.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return 0;
        //just return 0 if your list items do not have an Id variable.
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.comments_list_layout, null);
        }

        //Handle TextView and display string from your list
        final TextView pidText = (TextView)view.findViewById(R.id.list_item_cname);
        pidText.setText(cnameList.get(position));

        final TextView textText = (TextView)view.findViewById(R.id.list_item_ctext);
        textText.setText(textList.get(position));

        final TextView timestampText = (TextView)view.findViewById(R.id.list_item_ctimestamp);
        timestampText.setText(timestampList.get(position));


        return view;
    }
}
