package com.filmfactory.ffem.Services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.filmfactory.ffem.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Mohsin on 12/1/2017.
 */

public class JuniorTaskService extends Service {

    DatabaseReference taskRef;
    NotificationCompat.Builder builder;

    @Override
    public void onCreate() {
        super.onCreate();

        taskRef = FirebaseDatabase.getInstance().getReference("Junior Employee");
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        builder = new NotificationCompat.Builder(getApplicationContext(),"12")
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("Task update")
                .setContentText("Your tasks have been updated!")
                .setPriority(Notification.PRIORITY_HIGH);

        Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show();

        taskRef.child(uid).child("tasks").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(12, builder.build());
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

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
