package com.example.samarjeet.connect;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import static com.example.samarjeet.connect.Constants.raiseAToast;

public class AddCommentActivity extends AppCompatActivity {

    Activity mactivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_comment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        final String postid =  intent.getStringExtra(PostAdapter.EXTRA_MESSAGE1);

        mactivity = this;

        final EditText commentText = (EditText) findViewById(R.id.comment_text);
        Button commentButton = (Button) findViewById(R.id.comment_button);
        commentButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(!commentText.getText().toString().equals("")){
                    MakeRequest req = new MakeRequest("NewComment",commentText.getText().toString(),postid,mactivity);
                    Thread reqthread = new Thread(req,"Making request: NewComment");
                    reqthread.start();
                }
                else{
                    raiseAToast(mactivity,"Please enter text");
                }
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