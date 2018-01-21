package com.filmfactory.ffem.pojo;

/**
 * Created by Mohsin on 11/11/2017.
 */

public class Newsfeed {

    String title, post, postTime;

    public Newsfeed(){

    }

    public Newsfeed(String _title, String _post, String _postTime){
        title = _title;
        post = _post;
        postTime = _postTime;
    }

    public void setTitle(String _title){
        title = _title;
    }

    public String getTitle(){
        return title;
    }

    public void setPost(String _post){
        post = _post;
    }

    public String getPost(){
        return post;
    }

    public void setPostTime(String _time){
        postTime = _time;
    }

    public String getPostTime(){
        return postTime;
    }

}
