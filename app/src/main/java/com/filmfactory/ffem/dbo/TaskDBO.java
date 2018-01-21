package com.filmfactory.ffem.dbo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.filmfactory.ffem.EmployeeActivity;
import com.filmfactory.ffem.pojo.Task;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mohsin on 11/14/2017.
 */

public class TaskDBO {

    public interface listFetchCompleteCallback<T>{
        void onFetchComplete(T data);
    }

    FirebaseDatabase database;
    Context context;
    ProgressDialog progressDialog;

    public TaskDBO(Context _context){
        context = _context;
        progressDialog = new ProgressDialog(context);
        database = FirebaseDatabase.getInstance();
    }

    public void addTask(final Task taskS){
        progressDialog.setTitle("Adding Task");
        progressDialog.setMessage("Please wait ...");

        progressDialog.show();

        final String taskKey = database.getReference("tasks").push().getKey();

        database.getReference("tasks").child(taskKey).setValue(taskS).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
                String seniorUID = taskS.getSeniorEmployee();
                HashMap<String,String> juniors = taskS.getJuniorEmployees();

                //add reference of task to senior employee
                database.getReference("Senior Employee").child(seniorUID).child("tasks").child(taskKey).setValue("true");

                for(Map.Entry<String,String> junior : juniors.entrySet()){
                    database.getReference("Junior Employee").child(junior.getKey()).child("tasks").child(taskKey).setValue("true");
                }
                progressDialog.dismiss();
                Toast.makeText(context, "Task added!", Toast.LENGTH_SHORT).show();
                context.startActivity(new Intent(context, EmployeeActivity.class));
            }
        }
        );
    }

    public void getTasksForRemoval(String uid, final listFetchCompleteCallback<ArrayList<Task>> callback){
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
                    String taskKey = taskID.getKey();
                    Log.d("Senior","TaskKey: "+taskKey);
                    database.getReference("tasks").child(taskKey).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Task task = dataSnapshot.getValue(Task.class);
                            try {
                                if (!task.getTaskStatus().equalsIgnoreCase("completed"))
                                    tasks.add(task);
                                callback.onFetchComplete(tasks);
                            }catch (NullPointerException e){}
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

    public void removeTask(final Task task){

        final Task taskC = task;


        progressDialog.setTitle("Removing Task");
        progressDialog.setMessage("Please wait...");

        progressDialog.show();
        database.getReference("tasks").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot taskSnapshot: dataSnapshot.getChildren()){

                    Task taskS = taskSnapshot.getValue(Task.class);
                    final String taskKey = taskSnapshot.getKey();

                    final String seniorKey = taskS.getSeniorEmployee();
                    final HashMap<String,String> juniors = taskS.getJuniorEmployees();

                    boolean timeCheck = taskS.getTaskAddedOn().equalsIgnoreCase(taskC.getTaskAddedOn());
                    boolean seniorCheck = taskS.getSeniorEmployee().equalsIgnoreCase(taskC.getSeniorEmployee());
                    boolean taskName = taskS.getTaskName().equalsIgnoreCase(taskC.getTaskName());

                    if(timeCheck && seniorCheck && taskName){
                        database.getReference("tasks").child(taskKey).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
                                database.getReference("Senior Employee").child(seniorKey).child("tasks")
                                        .child(taskKey).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
                                        for(Map.Entry<String,String> junior : juniors.entrySet()){
                                            database.getReference("Junior Employee").child(junior.getKey()).child("tasks")
                                                    .child(taskKey).removeValue();
                                        }
                                        progressDialog.dismiss();

                                    }
                                });

                            }
                        });

                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
