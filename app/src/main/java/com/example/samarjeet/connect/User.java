package com.example.samarjeet.connect;

/**
 * Created by samarjeet on 4/10/17.
 */

public class User {
    public String uName;
    public String uEmail;
    public String uid;

    User(String name, String id, String email){
        uName = name;
        uEmail = email;
        uid = id;
    }

    public String getName(){
        return uName;
    }

    public String getEmail(){
        return uEmail;
    }

    public String getid(){
        return uid;
    }

}
