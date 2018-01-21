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
import com.filmfactory.ffem.dbo.EmployeeDBO;
import com.filmfactory.ffem.pojo.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by Mohsin on 12/1/2017.
 */

public class SeniorTaskService extends Service {

    DatabaseReference taskRef;
    NotificationCompat.Builder builder;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        taskRef = FirebaseDatabase.getInstance().getReference("tasks");
        builder = new NotificationCompat.Builder(getApplicationContext(),"12")
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("Task update")
                .setContentText("Your tasks have been updated!")
                .setPriority(Notification.PRIORITY_HIGH);
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Toast.makeText(this, "Service started", Toast.LENGTH_SHORT).show();

        EmployeeDBO dbo = new EmployeeDBO(getApplicationContext());
        dbo.getTasksSenior(uid, new EmployeeDBO.listCallback<ArrayList<Task>>() {
            @Override
            public void listFetchComplete(ArrayList<Task> data) {
                for(Task task : data){
                    taskRef.child(task.getTaskID()).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                           /* NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                            notificationManager.notify(12, builder.build());*/
                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                            notificationManager.notify(12, builder.build());
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
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }
}
