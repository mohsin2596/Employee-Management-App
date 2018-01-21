package com.filmfactory.ffem.pojo;

/**
 * Created by Mohsin on 11/9/2017.
 */

public class User {
    private String email;
    private String name;
    private String role;
    private String uid;

    public User(){

    }

    public User(String _email, String _name, String _role){
        email = _email;
        name = _name;
        role = _role;
    }

    public void setUid(String _uid){
        uid = _uid;
    }

    public String getUid(){
        return uid;
    }

    public void setRole(String _role){
        role = _role;
    }

    public String getRole ()
    {
        return role;
    }

    public void setName(String _name){
        name = _name;
    }

    public String getName(){
        return name;
    }

    public void setEmail(String _email){
        email = _email;
    }

    public String getEmail(){
        return email;
    }
}
