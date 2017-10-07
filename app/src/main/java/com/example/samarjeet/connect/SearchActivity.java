package com.example.samarjeet.connect;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;

import static com.example.samarjeet.connect.Constants.raiseAToast;
import static com.example.samarjeet.connect.MakeRequest.flag;

public class SearchActivity extends AppCompatActivity {

    Activity mactivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mactivity = this;

        final AutoCompleteTextView userSearch = (AutoCompleteTextView) findViewById(R.id.search_user);
        final Button followButton = (Button) findViewById(R.id.follow_button);
        final Button showPostsButton = (Button) findViewById(R.id.show_posts_button);

        userSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                followButton.setVisibility(View.INVISIBLE);
                showPostsButton.setVisibility(View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {}

        });

        Button cancelButton = (Button) findViewById(R.id.cancel_button);

        cancelButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mactivity,HomeActivity.class);
                startActivity(intent);
            }
        });

        userSearch.setThreshold(3);
        userSearch.setAdapter(new SearchUserAdapter(this,this)); // 'this' is Activity instance

        userSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                final User user = (User) adapterView.getItemAtPosition(position);
                userSearch.setText(user.getName());
                if(user.getFollowed() == 1){
                    followButton.setText((CharSequence) "Unfollow");
                }

                followButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        if(followButton.getText().toString().equals("Unfollow")){
                            MakeRequest req = new MakeRequest("Unfollow",user.getid(),mactivity);
                            Thread reqthread = new Thread(req,"Making request: Unfollow");
                            reqthread.start();
                            while (!req.done);
                            if(flag) followButton.setText((CharSequence) "Follow");
                        }else{
                            MakeRequest req = new MakeRequest("Follow",user.getid(),mactivity);
                            Thread reqthread = new Thread(req,"Making request: Follow");
                            reqthread.start();
                            while (!req.done);
                            if(flag) followButton.setText((CharSequence) "Unfollow");
                        }

                    }
                });

                followButton.setVisibility(View.VISIBLE);
                showPostsButton.setVisibility(View.VISIBLE);



                showPostsButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        raiseAToast(mactivity,"To be implemented show posts");
                    }
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_view_posts) {
            Intent intent = new Intent(this,HomeActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_add_post) {
            Intent intent = new Intent(this,AddPostActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_search) {
            Intent intent = new Intent(this,SearchActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
