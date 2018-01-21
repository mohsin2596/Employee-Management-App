package com.filmfactory.ffem.dbo;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.filmfactory.ffem.pojo.RecentChat;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Mohsin on 11/27/2017.
 */

public class ChatDBO {
    DatabaseReference reference;
    Context context;
    public static int count;


    public interface RecentChatCallback<T>{
        void onFetchComplete(T data);
    }

    public ChatDBO (Context _context){
        context = _context;
        reference = FirebaseDatabase.getInstance().getReference("Chat");
    }

    /*public void getRecentChats(final String uid, final RecentChatCallback<ArrayList<RecentChat>> callback){
        reference.child(uid).child("chatList").addValueEventListener(new ValueEventListener() {
            ArrayList<RecentChat> recentChats = new ArrayList<>();
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                recentChats.clear();
                for (DataSnapshot userChat : dataSnapshot.getChildren()) {
                    String key = userChat.getKey();
                    FirebaseDatabase.getInstance().getReference("Messages").child(key).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            Log.d("Recent",dataSnapshot.getValue().toString());
                            //RecentChat recentChat = dataSnapshot.getValue(RecentChat.class);
                            //recentChats.add(recentChat);
                            callback.onFetchComplete(recentChats);
                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }



        });
    }

   */ /*public void getRecentChats(final String uid, final RecentChatCallback<ArrayList<RecentChat>> callback){
        reference.child(uid).child("chatList").addValueEventListener(new ValueEventListener() {
            ArrayList<RecentChat> recentChats = new ArrayList<>();
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                recentChats.clear();
                count = 0;

                ValueEventListener valueEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        RecentChat recentChat = dataSnapshot.getValue(RecentChat.class);
                        recentChats.add(recentChat);
                        //Toast.makeText(context, "change", Toast.LENGTH_SHORT).show();
                        Log.d("Change","change made " + recentChats.size());
                        callback.onFetchComplete(recentChats);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                };

                for (DataSnapshot userChat : dataSnapshot.getChildren()) {

                    String key = userChat.getKey();
                    recentChats.clear();

                    //Toast.makeText(context, "called", Toast.LENGTH_SHORT).show();
                    FirebaseDatabase.getInstance().getReference("Messages").child(key).addValueEventListener(valueEventListener);
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }*/

    public void getRecentChat(final String uid, final RecentChatCallback<ArrayList<RecentChat>> callback){
        reference.child(uid).child("chatList").addValueEventListener(new ValueEventListener() {
            ArrayList<RecentChat> recentChats = new ArrayList<>();
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                recentChats.clear();

                for(DataSnapshot recentChat : dataSnapshot.getChildren()){
                    RecentChat chat = recentChat.getValue(RecentChat.class);
                    recentChats.add(chat);
                }

                callback.onFetchComplete(recentChats);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}
