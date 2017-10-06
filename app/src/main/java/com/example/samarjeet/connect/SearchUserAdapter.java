package com.example.samarjeet.connect;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static com.example.samarjeet.connect.MakeRequest.done;
import static com.example.samarjeet.connect.MakeRequest.userlist;

public class SearchUserAdapter extends BaseAdapter implements Filterable {

    private static final int MAX_RESULTS = 10;
    private Context mContext;
    private Activity mactivity;
    private List<User> resultList = new ArrayList<>();

    public SearchUserAdapter(Context context, Activity activity)
    {
        mContext = context;
        mactivity = activity;
    }

    @Override
    public int getCount() {
        return resultList.size();
    }

    @Override
    public User getItem(int index) {
        return resultList.get(index);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.user_list_layout, parent, false);
        }
        ((TextView) convertView.findViewById(R.id.uname)).setText(getItem(position).getName());
        ((TextView) convertView.findViewById(R.id.uemail)).setText(getItem(position).getEmail());
        ((TextView) convertView.findViewById(R.id.uid)).setText(getItem(position).getid());
        return convertView;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    List<User> users = findUsers(constraint.toString());

                    // Assign the data to the FilterResults
                    filterResults.values = users;
                    filterResults.count = users.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    resultList = (List<User>) results.values;
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }};
        return filter;
    }

    private List<User> findUsers(String search) {

        MakeRequest req = new MakeRequest("SearchUser",search,mactivity);
        Thread reqthread = new Thread(req,"Making request: CreatePosts");
        reqthread.start();

        while(!done);
        return userlist;
    }
}