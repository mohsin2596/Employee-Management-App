package com.filmfactory.ffem;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;

import com.filmfactory.ffem.adapter.RemoveTaskListAdapter;
import com.filmfactory.ffem.dbo.TaskDBO;
import com.filmfactory.ffem.pojo.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class RemoveTask extends AppCompatActivity {

    ListView listView;
    TaskDBO taskDBO;
    RemoveTaskListAdapter adapter;
    EditText filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_task);
        getSupportActionBar().setTitle("Remove Tasks");

        init();
    }

    private void init(){
        listView = findViewById(R.id.removeTaskList);
        taskDBO = new TaskDBO(this);
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        taskDBO.getTasksForRemoval(uid, new TaskDBO.listFetchCompleteCallback<ArrayList<Task>>() {
            @Override
            public void onFetchComplete(ArrayList<Task> data) {
                adapter = new RemoveTaskListAdapter(RemoveTask.this,data);
                listView.setAdapter(adapter);
            }
        });

        filter = findViewById(R.id.taskName);
        filter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter.getFilter().filter(charSequence);
                listView.invalidate();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}
