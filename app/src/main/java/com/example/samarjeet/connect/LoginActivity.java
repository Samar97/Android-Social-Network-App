package com.example.samarjeet.connect;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.os.AsyncTask;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import static com.example.samarjeet.connect.Constants.http_url;
import static com.example.samarjeet.connect.Constants.raiseAToast;

public class LoginActivity extends AppCompatActivity{

    public static final String TAG = "LoginActivity";
    EditText uid;
    EditText password;
    Button login_button;
    Activity myactivity;

    BufferedWriter writer;
    BufferedReader reader;
    HttpURLConnection conn;

    public static CookieManager cookieManager;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        cookieManager =new CookieManager();
        CookieHandler.setDefault(cookieManager);

        myactivity = this;
        uid = (EditText) findViewById(R.id.uid);
        password = (EditText) findViewById(R.id.password);
        login_button = (Button) findViewById(R.id.login_button);

        login_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                new Login().execute();
            }
        });
    }


    private class Login extends AsyncTask<Void, Void, Void>{

        String username,pass;

        @Override
        protected Void doInBackground(Void... args) {

            myactivity.runOnUiThread(new Runnable(){
                @Override
                public void run(){
                    username = uid.getText().toString();
                    pass = password.getText().toString();
                }
            });

            try {
                URL url = new URL(http_url+"Login");
                Log.d(TAG,"Connecting to: " + url);
                conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);


                OutputStream out = new BufferedOutputStream(conn.getOutputStream());
                writer = new BufferedWriter(
                        new OutputStreamWriter(out, "UTF-8"));

                String post = "id="+username+"&password="+pass;

                writer.write(post);
                writer.flush();

                Log.d(TAG,"Posted: "+post + "to url: "+url);

                int responseCode=conn.getResponseCode();

                Log.d(TAG,"Got response: " + responseCode);

                String response = "";
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    String line;
                    reader=new BufferedReader(new InputStreamReader(conn.getInputStream()));

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
;
                String attr1 = jsonObject.getString("status");
                if(attr1.equals("true")){
                    Log.d(TAG,"YAY Successful Login: "+ attr1);

                    conn.disconnect();
                    Intent intent = new Intent(myactivity,HomeActivity.class);
                    startActivity(intent);

                }else{
                    raiseAToast(myactivity,"Invalid Credentials !");
                    conn.disconnect();
                }

                Log.d(TAG,"Craxx");


            }catch (Exception e){
                e.printStackTrace();
                conn.disconnect();
            }

            return null;
        }

    }

}

