package com.example.samarjeet.connect;

import android.app.Activity;
import android.widget.Toast;

/**
 * Created by samarjeet on 3/10/17.
 */

public class Constants {

    public static final String http_url = "http://192.168.0.107:8080/Connect/";

    public static void raiseAToast(Activity activity, String message){
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
    }

}
