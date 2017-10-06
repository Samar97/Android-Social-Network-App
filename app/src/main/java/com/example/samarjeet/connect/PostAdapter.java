package com.example.samarjeet.connect; /**
 * Created by samarjeet on 4/10/17.
 */

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.samarjeet.connect.R;

import java.util.ArrayList;


public class PostAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<String> pidList = new ArrayList<String>();
    private ArrayList<String> textList = new ArrayList<String>();
    private ArrayList<String> timestampList = new ArrayList<String>();
    private ArrayList<CommentsAdapter> commentsAdapter;
    private Context context;

    public static final String TAG = "Adapter";

    public PostAdapter(ArrayList<String> lpidList, ArrayList<String> ltextList, ArrayList<String> ltimestampList, ArrayList<CommentsAdapter> lcommentsAdapter, Context mycontext) {
        pidList = lpidList;
        textList = ltextList;
        timestampList = ltimestampList;
        commentsAdapter = lcommentsAdapter;
        context = mycontext;
    }

    @Override
    public int getCount() {
        return pidList.size();
    }

    @Override
    public Object getItem(int pos) {
        return pidList.get(pos);
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
            view = inflater.inflate(R.layout.posts_list_layout, null);
        }

        //Handle TextView and display string from your list
        final TextView pidText = (TextView)view.findViewById(R.id.list_item_pid);
        pidText.setText(pidList.get(position));

        final TextView textText = (TextView)view.findViewById(R.id.list_item_ptext);
        textText.setText(textList.get(position));

        final TextView timestampText = (TextView)view.findViewById(R.id.list_item_ptimestamp);
        timestampText.setText(timestampList.get(position));

        ListView lView = (ListView) view.findViewById(R.id.comments_list);

        lView.setAdapter(commentsAdapter.get(position));

        return view;
    }
}

