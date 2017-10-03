package com.example.samarjeet.connect;

/**
* Created by samarjeet on 3/10/17.
*/

import android.util.Log;

public class MakeRequest implements Runnable{

    public static final String  Tag = "Make Request";

   String requestType;

    public MakeRequest(String s){

        requestType = s;
        Log.d(Tag,"Making a request for: "+ requestType);

    }

    public void run() {
        Log.d(Tag,"Requesting...");


        Log.d(Tag,"Request processed");
    }



}
