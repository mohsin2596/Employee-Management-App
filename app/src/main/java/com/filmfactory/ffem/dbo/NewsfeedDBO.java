package com.filmfactory.ffem.dbo;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;

import com.filmfactory.ffem.pojo.Newsfeed;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Mohsin on 11/11/2017.
 */

public class NewsfeedDBO {

    public interface ListCallback<T> {
        void callback(T data);
    }

    public interface addCallback {
        void postAdded(boolean data);
    }

    Context context;
    FirebaseDatabase mDatabase;
    ProgressDialog progressDialog;

    public NewsfeedDBO(Context _context){
        context = _context;
        progressDialog = new ProgressDialog(context);
        mDatabase = FirebaseDatabase.getInstance();
    }

    public void addPost(String title, String post, String time, final addCallback finishedCallback){
        progressDialog.setTitle("Adding post");
        progressDialog.setMessage("Please wait ...");

        progressDialog.show();

        Newsfeed newsfeed = new Newsfeed(title,post,time);

        mDatabase.getReference("newsfeed").push().setValue(newsfeed).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.dismiss();
                finishedCallback.postAdded(task.isSuccessful());
            }
        });

    }

    public void getNewsfeed(final ListCallback<ArrayList<Newsfeed>> feedCallback){

        mDatabase.getReference("newsfeed").addValueEventListener(new ValueEventListener() {
            ArrayList<Newsfeed> newsfeeds = new ArrayList<Newsfeed>();

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                newsfeeds.clear();

                for(DataSnapshot newsfeedItem : dataSnapshot.getChildren()){
                    Newsfeed newsfeed = newsfeedItem.getValue(Newsfeed.class);
                    newsfeeds.add(newsfeed);
                }

                feedCallback.callback(newsfeeds);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }


}
