package com.example.samarjeet.connect; /**
 * Created by samarjeet on 4/10/17.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class PostAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<String> pidList = new ArrayList<>();
    private ArrayList<String> textList = new ArrayList<>();
    private ArrayList<String> timestampList = new ArrayList<>();
    private ArrayList<CommentsAdapter> commentsAdapter = new ArrayList<>();

    private Context context;
    public static final String EXTRA_MESSAGE1 = "com.example.samarjeet.db_assign_trial.MESSAGE";
    public static final String PREV_ACT = "com.example.samarjeet.db_assign_trial.ACT";
    public static final String TAG = "Adapter";

    public PostAdapter(ArrayList<String> lpidList, ArrayList<String> ltextList, ArrayList<String> ltimestampList,
                       ArrayList<CommentsAdapter> lcommentsAdapter, Context mycontext) {
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

        final TextView addcomment = (TextView) view.findViewById(R.id.list_item_add_comment);

        addcomment.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                    Intent intent = new Intent(context, AddCommentActivity.class);
                    intent.putExtra(EXTRA_MESSAGE1,pidList.get(position));
                    intent.putExtra(PREV_ACT,context.getClass().getSimpleName());
                    context.startActivity(intent);
            }
        });

        ListView lView =  (ListView) view.findViewById(R.id.comments_list);
        if(commentsAdapter.get(position).getActualCount()>3 && !commentsAdapter.get(position).more){
            final TextView viewMoreComments = (TextView)view.findViewById(R.id.view_more_comments);
            viewMoreComments.setVisibility(View.VISIBLE);
            viewMoreComments.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    commentsAdapter.get(position).more = true;
                    commentsAdapter.get(position).notifyDataSetChanged();
                    notifyDataSetChanged();
                    viewMoreComments.setVisibility(View.GONE);
                }
            });
        }

        lView.setAdapter(commentsAdapter.get(position));
        setListViewHeightBasedOnChildren(lView);

        return view;
    }

    private static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));
            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
}