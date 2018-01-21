package com.filmfactory.ffem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AssignNewTask extends AppCompatActivity {

    EditText taskName, taskDesc, duration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_new_task);
        getSupportActionBar().setTitle("Assign Task");

        init();
    }

    private void init(){
        taskName = findViewById(R.id.TaskName);
        taskDesc = findViewById(R.id.TaskDesc);
        duration = findViewById(R.id.TaskDuration);
    }

    public void addJuniors (View view){
        String name = taskName.getText().toString().trim();
        String desc = taskDesc.getText().toString().trim();
        String durationS = duration.getText().toString().trim();

        if(name.isEmpty() || desc.isEmpty() || durationS.isEmpty()){
            Toast.makeText(this, "Please enter all fields!", Toast.LENGTH_LONG).show();
        }
        else{
            Intent intent = new Intent(this,SelectJuniors.class);
            intent.putExtra("name",name);
            intent.putExtra("desc",desc);
            intent.putExtra("duration",durationS);
            startActivity(intent);
        }
    }
}
