package com.example.samarjeet.connect;

/**
* Created by samarjeet on 3/10/17.
*/

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.example.samarjeet.connect.Constants.http_url;
import static com.example.samarjeet.connect.Constants.raiseAToast;
import static com.example.samarjeet.connect.UserPostActivity.lastuid;

public class MakeRequest implements Runnable{

    public static final String  TAG = "Make Request";

    String requestType;
    Activity mactivity;
    String content;
    String cpostid;
    String prevActivity;

    ListView lView = null;
    PostAdapter postAdapter;

    public List<User> userlist;
    public boolean done = false;
    public static boolean flag = false;

    public MakeRequest(String s, Activity a){

        requestType = s;
        mactivity = a;

        Log.d(TAG,"Making a request for: "+ requestType);

    }

    public MakeRequest(String s, String c, Activity a){

        requestType = s;
        mactivity = a;
        content = c;
        Log.d(TAG,"Making a request for: "+ requestType);

    }

    public MakeRequest(String s, String c, String p, String an, Activity a){

        requestType = s;
        mactivity = a;
        content = c;
        cpostid = p;
        prevActivity = an;
        Log.d(TAG,"Making a request for: "+ requestType);

    }



    public void run() {
        Log.d(TAG,"Requesting...");

        if(requestType.equals("SeePosts")) SeePosts();
        else if (requestType.equals("CreatePost")) CreatePost();
        else if (requestType.equals("SearchUser")) SearchUser();
        else if (requestType.equals("NewComment")) NewComment();
        else if(requestType.equals("Follow")) Follow();
        else if(requestType.equals("Unfollow")) Unfollow();
        else if (requestType.equals("SeeUserPosts")) SeeUserPosts();

        Log.d(TAG,"Request processed");
    }

    public void SeeUserPosts(){

        HttpURLConnection conn =  null;

        try {
            URL url = new URL(http_url+requestType);
            conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream out = new BufferedOutputStream(conn.getOutputStream());
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(out, "UTF-8"));

            String post = "uid="+content;

            writer.write(post);
            writer.flush();

            int responseCode=conn.getResponseCode();

            Log.d(TAG,"Got response: " + responseCode);

            String response = "";
            if (responseCode == HttpURLConnection.HTTP_OK) {
                String line;
                BufferedReader reader=new BufferedReader(new InputStreamReader(conn.getInputStream()));

                while ((line=reader.readLine()) != null) {
                    response+=line;
                }
                Log.d(TAG,"Read: " + response);
            }
            else {
                Log.d(TAG,"No response code");
                response="";
            }

            JSONObject jsonObject = new JSONObject(response);

            String attr1 = jsonObject.getString("status");

            if(attr1.equals("true")){
                Log.d(TAG,"YAY Successful Receipt of Posts: "+ attr1);

                ArrayList<String> pidList = new ArrayList<>(),textList = new ArrayList<>(),timestampList = new ArrayList<>();
                ArrayList<CommentsAdapter> commentsAdapter = new ArrayList<>();


                JSONArray data = new JSONArray(jsonObject.getString("data"));
                if(data.length() != 0){

                    for(int i=0; i<data.length(); i++){
                        JSONObject temp = data.getJSONObject(i);
                        pidList.add(temp.getString("postid"));
                        textList.add(temp.getString("text"));
                        timestampList.add(temp.getString("timestamp"));

                        ArrayList<String> tcnameList = new ArrayList<>(), tctextList = new ArrayList<>(),tctimestampList = new ArrayList<>();

                        JSONArray comments = new JSONArray(temp.getString("Comment"));

                        if(comments.length() != 0){
                            for(int j=0; j<comments.length(); j++){
                                JSONObject ctemp = comments.getJSONObject(j);
                                tcnameList.add(ctemp.getString("name"));
                                tctextList.add(ctemp.getString("text"));
                                tctimestampList.add(ctemp.getString("timestamp"));
                            }
                        }

                        Log.d(TAG,"Comments list: "+tctextList.toString());

                        CommentsAdapter tcommentsAdapter = new CommentsAdapter(tcnameList,tctextList,tctimestampList,mactivity);
                        commentsAdapter.add(tcommentsAdapter);

                    }
                }


                postAdapter = new PostAdapter(pidList,textList,timestampList,commentsAdapter,mactivity);

                mactivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Log.d(TAG, "List Adapter : "+ postAdapter);
                        lView = (ListView) mactivity.findViewById(R.id.uposts_list);
                        lView.setAdapter(postAdapter);
                    }});



            }else{
                Log.d(TAG,"Could not receive posts !");
                conn.disconnect();
            }

            Log.d(TAG,"Craxx");


        }catch (Exception e){
            e.printStackTrace();
            conn.disconnect();
        }
    }

    public void Follow(){
        HttpURLConnection conn =  null;

        try {
            URL url = new URL(http_url+requestType);
            conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream out = new BufferedOutputStream(conn.getOutputStream());
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(out, "UTF-8"));

            String post = "uid="+content;

            writer.write(post);
            writer.flush();

            int responseCode=conn.getResponseCode();

            Log.d(TAG,"Got response: " + responseCode);

            String response = "";
            if (responseCode == HttpURLConnection.HTTP_OK) {
                String line;
                BufferedReader reader=new BufferedReader(new InputStreamReader(conn.getInputStream()));

                while ((line=reader.readLine()) != null) {
                    response+=line;
                }
                Log.d(TAG,"Read: " + response);
            }
            else {
                Log.d(TAG,"No response code");
                response="";
            }

            JSONObject jsonObject = new JSONObject(response);

            String attr1 = jsonObject.getString("status");

            if(attr1.equals("true")){
                Log.d(TAG,"YAY Successfully followed: "+content+"!");

                mactivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        raiseAToast(mactivity,"Successfully followed!");
                    }});
                done = true;
                flag = true;
                conn.disconnect();

            }else{
                Log.d(TAG,"Could not unfollow!");

                mactivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        raiseAToast(mactivity,"Not able to follow!");
                    }});
                done =true;
                conn.disconnect();
            }

            Log.d(TAG,"Craxx");


        }catch (Exception e){
            done = true;
            e.printStackTrace();
            conn.disconnect();
        }
    }

    public void Unfollow(){
        HttpURLConnection conn =  null;

        try {
            URL url = new URL(http_url+requestType);
            conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream out = new BufferedOutputStream(conn.getOutputStream());
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(out, "UTF-8"));

            String post = "uid="+content;

            writer.write(post);
            writer.flush();

            int responseCode=conn.getResponseCode();

            Log.d(TAG,"Got response: " + responseCode);

            String response = "";
            if (responseCode == HttpURLConnection.HTTP_OK) {
                String line;
                BufferedReader reader=new BufferedReader(new InputStreamReader(conn.getInputStream()));

                while ((line=reader.readLine()) != null) {
                    response+=line;
                }
                Log.d(TAG,"Read: " + response);
            }
            else {
                Log.d(TAG,"No response code");
                response="";
            }

            JSONObject jsonObject = new JSONObject(response);

            String attr1 = jsonObject.getString("status");

            if(attr1.equals("true")){
                Log.d(TAG,"YAY Successfully unfollowed: "+content+"!");

                mactivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        raiseAToast(mactivity,"Successfully unfollowed!");
                    }});
                done = true;
                flag = true;
                conn.disconnect();

            }else{
                Log.d(TAG,"Could not unfollow!");

                mactivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        raiseAToast(mactivity,"Not able to unfollow!");
                    }});
                done = true;
                conn.disconnect();
            }

            Log.d(TAG,"Craxx");


        }catch (Exception e){
            done = true;
            e.printStackTrace();
            conn.disconnect();
        }
    }

    public void NewComment(){
        HttpURLConnection conn =  null;

        try {
            URL url = new URL(http_url+requestType);
            conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream out = new BufferedOutputStream(conn.getOutputStream());
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(out, "UTF-8"));

            String post = "content="+content+"&postid="+cpostid;

            writer.write(post);
            writer.flush();

            int responseCode=conn.getResponseCode();

            Log.d(TAG,"Got response: " + responseCode);

            String response = "";
            if (responseCode == HttpURLConnection.HTTP_OK) {
                String line;
                BufferedReader reader=new BufferedReader(new InputStreamReader(conn.getInputStream()));

                while ((line=reader.readLine()) != null) {
                    response+=line;
                }
                Log.d(TAG,"Read: " + response);
            }
            else {
                Log.d(TAG,"No response code");
                response="";
            }

            JSONObject jsonObject = new JSONObject(response);

            String attr1 = jsonObject.getString("status");

            if(attr1.equals("true")){
                Log.d(TAG,"YAY Successfully commented!");

                mactivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        raiseAToast(mactivity,"Successfully commented!");
                    }});
                Log.d(TAG,"Prev activity name :"+prevActivity);
                if(prevActivity.equals("HomeActivity")){
                    Intent intent = new Intent(mactivity,HomeActivity.class);
                    mactivity.startActivity(intent);
                }
                else{
                    Intent intent = new Intent(mactivity,UserPostActivity.class);
                    intent.putExtra(SearchActivity.EXTRA_MESSAGE2,lastuid);
                    mactivity.startActivity(intent);
                }


            }else{
                Log.d(TAG,"Could not comment!");

                mactivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        raiseAToast(mactivity,"Could not comment!");
                    }});

                conn.disconnect();
            }

            Log.d(TAG,"Craxx");


        }catch (Exception e){
            e.printStackTrace();
            conn.disconnect();
        }
    }

    public void SearchUser(){

        HttpURLConnection conn =  null;

        try {
            URL url = new URL(http_url+requestType);
            conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream out = new BufferedOutputStream(conn.getOutputStream());
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(out, "UTF-8"));

            String post = "searchstring="+content;

            writer.write(post);
            writer.flush();

            Log.d(TAG,"Posted: "+post + " to url: "+url);

            int responseCode=conn.getResponseCode();

            Log.d(TAG,"Got response: " + responseCode);

            String response = "";
            if (responseCode == HttpURLConnection.HTTP_OK) {
                String line;
                BufferedReader reader=new BufferedReader(new InputStreamReader(conn.getInputStream()));

                while ((line=reader.readLine()) != null) {
                    response+=line;
                }
                Log.d(TAG,"Read: " + response);
            }
            else {
                Log.d(TAG,"No response code");
                response="";
            }

            JSONObject jsonObject = new JSONObject(response);

            String attr1 = jsonObject.getString("status");

            if(attr1.equals("true")){
                Log.d(TAG,"YAY Successfully posted!");

                mactivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        raiseAToast(mactivity,"Successfully got userlist!");
                    }});

                userlist = new ArrayList<>();
                JSONArray data = new JSONArray(jsonObject.getString("data"));
                data =  data.getJSONArray(0);
                if(data.length()!=0){

                    for(int i=0; i<data.length(); i++){
                        JSONObject temp = data.getJSONObject(i);

                        User tempuser = new User(temp.getString("name"),temp.getString("uid"),temp.getString("email"),temp.getInt("follow"));
                        userlist.add(tempuser);

                    }
                }
                done = true;

            }else{
                Log.d(TAG,"Could not fetch userlist!");

                mactivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        raiseAToast(mactivity,"Could not fetch userlist!");
                    }});
                done = true;
                conn.disconnect();
            }

            Log.d(TAG,"Craxx");


        }catch (Exception e){
            done = true;
            e.printStackTrace();
            conn.disconnect();
        }
    }

    public void CreatePost(){
        HttpURLConnection conn =  null;

        try {
            URL url = new URL(http_url+requestType);
            conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream out = new BufferedOutputStream(conn.getOutputStream());
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(out, "UTF-8"));

            String post = "content="+content;

            writer.write(post);
            writer.flush();

            int responseCode=conn.getResponseCode();

            Log.d(TAG,"Got response: " + responseCode);

            String response = "";
            if (responseCode == HttpURLConnection.HTTP_OK) {
                String line;
                BufferedReader reader=new BufferedReader(new InputStreamReader(conn.getInputStream()));

                while ((line=reader.readLine()) != null) {
                    response+=line;
                }
                Log.d(TAG,"Read: " + response);
            }
            else {
                Log.d(TAG,"No response code");
                response="";
            }

            JSONObject jsonObject = new JSONObject(response);

            String attr1 = jsonObject.getString("status");

            if(attr1.equals("true")){
                Log.d(TAG,"YAY Successfully posted!");

                mactivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        raiseAToast(mactivity,"Successfully posted!");
                    }});
                Intent intent = new Intent(mactivity,HomeActivity.class);
                mactivity.startActivity(intent);

            }else{
                Log.d(TAG,"Could not post!");

                mactivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        raiseAToast(mactivity,"Could not post!");
                    }});

                conn.disconnect();
            }

            Log.d(TAG,"Craxx");


        }catch (Exception e){
            e.printStackTrace();
            conn.disconnect();
        }
    }

    public void SeePosts(){

        HttpURLConnection conn =  null;

        try {
            URL url = new URL(http_url+requestType);
            conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setDoOutput(false);

            int responseCode=conn.getResponseCode();

            Log.d(TAG,"Got response: " + responseCode);

            String response = "";
            if (responseCode == HttpURLConnection.HTTP_OK) {
                String line;
                BufferedReader reader=new BufferedReader(new InputStreamReader(conn.getInputStream()));

                while ((line=reader.readLine()) != null) {
                    response+=line;
                }
                Log.d(TAG,"Read: " + response);
            }
            else {
                Log.d(TAG,"No response code");
                response="";
            }

            JSONObject jsonObject = new JSONObject(response);

            String attr1 = jsonObject.getString("status");

            if(attr1.equals("true")){
                Log.d(TAG,"YAY Successful Receipt of Posts: "+ attr1);

                ArrayList<String> pidList = new ArrayList<>(),textList = new ArrayList<>(),timestampList = new ArrayList<>();
                ArrayList<CommentsAdapter> commentsAdapter = new ArrayList<>();
                ArrayList<Integer> commentsCount = new ArrayList<>();

                JSONArray data = new JSONArray(jsonObject.getString("data"));
                if(data.length() != 0){

                    for(int i=0; i<data.length(); i++){
                        JSONObject temp = data.getJSONObject(i);
                        pidList.add(temp.getString("postid"));
                        textList.add(temp.getString("text"));
                        timestampList.add(temp.getString("timestamp"));

                        ArrayList<String> tcnameList = new ArrayList<>(), tctextList = new ArrayList<>(),tctimestampList = new ArrayList<>();

                        JSONArray comments = new JSONArray(temp.getString("Comment"));

                        if(comments.length() != 0){
                            for(int j=0; j<comments.length(); j++){
                                JSONObject ctemp = comments.getJSONObject(j);
                                tcnameList.add(ctemp.getString("name"));
                                tctextList.add(ctemp.getString("text"));
                                tctimestampList.add(ctemp.getString("timestamp"));
                            }
                        }

                        Log.d(TAG,"Comments list: "+tctextList.toString());

                        CommentsAdapter tcommentsAdapter = new CommentsAdapter(tcnameList,tctextList,tctimestampList,mactivity);
                        commentsAdapter.add(tcommentsAdapter);
                        commentsCount.add(tcnameList.size());
                    }
                }


                postAdapter = new PostAdapter(pidList,textList,timestampList,commentsAdapter,mactivity);

                mactivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        Log.d(TAG, "List Adapter : "+ postAdapter);
                        lView = (ListView) mactivity.findViewById(R.id.posts_list);
                        lView.setAdapter(postAdapter);
                    }});



            }else{
                Log.d(TAG,"Could not receive posts !");
                conn.disconnect();
            }

            Log.d(TAG,"Craxx");


        }catch (Exception e){
            e.printStackTrace();
            conn.disconnect();
        }
    }
}
