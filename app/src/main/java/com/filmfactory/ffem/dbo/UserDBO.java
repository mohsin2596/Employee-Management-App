package com.filmfactory.ffem.dbo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;

import com.filmfactory.ffem.pojo.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;

/**
 * Created by Mohsin on 11/9/2017.
 */



public class UserDBO {

    public interface ListCallback<T> {
        void callback(T data);
    }

    Context context;
    DatabaseReference reference;
    ProgressDialog progressDialog;
    FirebaseAuth auth;
    SharedPreferences sharedPreferences;


    public UserDBO(Context _context){
        context = _context;
        progressDialog = new ProgressDialog(context);
        reference = FirebaseDatabase.getInstance().getReference("users");
        auth = FirebaseAuth.getInstance();
        sharedPreferences = context.getSharedPreferences("userRole",Context.MODE_PRIVATE);
    }

    public void getUserList(@NonNull final ListCallback<ArrayList<User>> finishedCallback ){

        progressDialog.setTitle("Fetching User List");
        progressDialog.setMessage("Please wait");
        progressDialog.show();

        reference.addValueEventListener(new ValueEventListener() {
            ArrayList<User> userList = new ArrayList<User>();

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userList.clear();
                String email = sharedPreferences.getString("email","null");
                for(DataSnapshot userSnapshot: dataSnapshot.getChildren()){
                    User user = userSnapshot.getValue(User.class);
                    user.setUid(userSnapshot.getKey());
                    if(!email.equalsIgnoreCase(user.getEmail())){
                        Log.d("getEmail",user.getEmail());
                        userList.add(user);
                    }
                    //userList.add(user);

                }
                finishedCallback.callback(userList);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
