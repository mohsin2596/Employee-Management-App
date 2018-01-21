package com.filmfactory.ffem.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.filmfactory.ffem.ProfileActivity;
import com.filmfactory.ffem.R;
import com.filmfactory.ffem.dbo.NewsfeedDBO;
import com.filmfactory.ffem.pojo.Newsfeed;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Mohsin on 11/11/2017.
 */

public class NewsfeedAdapter extends BaseAdapter {

    ArrayList<Newsfeed> newsfeeds;
    Context context;
    NewsfeedDBO newsfeedDBO;
    LayoutInflater layoutInflater;

    public NewsfeedAdapter(Context _context, ArrayList<Newsfeed> newsList){
        context = _context;
        newsfeedDBO = new NewsfeedDBO(context);
        newsfeeds = new ArrayList<Newsfeed>();
        newsfeeds.addAll(newsList);
        Collections.reverse(newsfeeds);
        layoutInflater = ((Activity) context).getLayoutInflater();
    }

    @Override
    public int getCount() {
        return newsfeeds.size();
    }

    @Override
    public Object getItem(int i) {
        return newsfeeds.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View currRow = view;

        if(view == null){
            currRow = layoutInflater.inflate(R.layout.newsfeed_item, viewGroup, false);
        }

        TextView title = currRow.findViewById(R.id.title);
        TextView time = currRow.findViewById(R.id.time);
        TextView post = currRow.findViewById(R.id.post);


        title.setText(newsfeeds.get(i).getTitle());
        time.setText(newsfeeds.get(i).getPostTime());
        post.setText(newsfeeds.get(i).getPost());



        return currRow;
    }
}
