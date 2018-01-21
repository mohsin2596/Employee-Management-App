package com.filmfactory.ffem.dbo;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.filmfactory.ffem.pojo.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Mohsin on 11/14/2017.
 */

public class EmployeeDBO {

    public interface listCallback<T>{
        void listFetchComplete(T data);
    }

    Context context;
    FirebaseDatabase database;
    ProgressDialog progressDialog;

    public EmployeeDBO(Context _context){
        context = _context;
        progressDialog = new ProgressDialog(context);
        database = FirebaseDatabase.getInstance();
    }

    public void getTasksSenior(String uid, final listCallback<ArrayList<Task>> callback){

        progressDialog.setTitle("Fetching data");
        progressDialog.setMessage("Please wait ...");

        Log.d("Senior","UID: "+uid);

       // progressDialog.show();

        final DatabaseReference reference = database.getReference("Senior Employee");
        reference.child(uid).child("tasks").addValueEventListener(new ValueEventListener() {
            ArrayList<Task> tasks = new ArrayList<>();
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tasks.clear();

                for(DataSnapshot taskID : dataSnapshot.getChildren()){
                    final String taskKey = taskID.getKey();
                    Log.d("Senior","TaskKey: "+taskKey);
                    database.getReference("tasks").child(taskKey).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Task task = dataSnapshot.getValue(Task.class);
                            try {
                                Log.d("Senior", "TaskName:" + task.getTaskName());
                                task.setTaskID(taskKey);
                                tasks.add(task);
                                callback.listFetchComplete(tasks);
                            } catch (NullPointerException e){
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                //callback.listFetchComplete(tasks);
                //progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void getPendingTasks(String uid, final listCallback<ArrayList<Task>> callback){

        progressDialog.setTitle("Fetching data");
        progressDialog.setMessage("Please wait ...");

        Log.d("Junior","UID: "+uid);

        // progressDialog.show();

        final DatabaseReference reference = database.getReference("Junior Employee");
        reference.child(uid).child("tasks").addValueEventListener(new ValueEventListener() {
            ArrayList<Task> tasks = new ArrayList<>();
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tasks.clear();

                for(DataSnapshot taskID : dataSnapshot.getChildren()){
                    String taskKey = taskID.getKey();
                    Log.d("Junior","TaskKey: "+taskKey);
                    database.getReference("tasks").child(taskKey).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Task task = dataSnapshot.getValue(Task.class);
                            try {
                                if(!task.getTaskStatus().equalsIgnoreCase("completed")) {
                                    Log.d("Junior", "TaskName:" + task.getTaskName());
                                    task.setTaskID(dataSnapshot.getKey());
                                    tasks.add(task);
                                    callback.listFetchComplete(tasks);
                                }
                            } catch (NullPointerException e){
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                //callback.listFetchComplete(tasks);
                //progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getCompletedTasks(String uid, final listCallback<ArrayList<Task>> callback){

        progressDialog.setTitle("Fetching data");
        progressDialog.setMessage("Please wait ...");

        Log.d("Junior","UID: "+uid);

        // progressDialog.show();

        final DatabaseReference reference = database.getReference("Junior Employee");
        reference.child(uid).child("tasks").addValueEventListener(new ValueEventListener() {
            ArrayList<Task> tasks = new ArrayList<>();
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tasks.clear();

                for(DataSnapshot taskID : dataSnapshot.getChildren()){
                    String taskKey = taskID.getKey();
                    Log.d("Junior","TaskKey: "+taskKey);
                    database.getReference("tasks").child(taskKey).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Task task = dataSnapshot.getValue(Task.class);
                            try {
                                if(task.getTaskStatus().equalsIgnoreCase("completed")) {
                                    Log.d("Junior", "TaskName:" + task.getTaskName());
                                    task.setTaskID(dataSnapshot.getKey());
                                    tasks.add(task);
                                    callback.listFetchComplete(tasks);
                                }
                            } catch (NullPointerException e){
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                //callback.listFetchComplete(tasks);
                //progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
