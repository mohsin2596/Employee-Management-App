package com.filmfactory.ffem;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.filmfactory.ffem.adapter.SelectJuniorAdapter;
import com.filmfactory.ffem.dbo.TaskDBO;
import com.filmfactory.ffem.dbo.UserDBO;
import com.filmfactory.ffem.pojo.Task;
import com.filmfactory.ffem.pojo.User;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class SelectJuniors extends AppCompatActivity {

    public static HashMap<String,Boolean> toggleStates;
    ArrayList<User> fetchedData;
    HashMap<String,String> juniorsInvolved;
    String taskName, taskDesc, taskDuration;
    UserDBO userDBO;
    EditText filter;
    ListView listView;
    SelectJuniorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_juniors);
        getSupportActionBar().setTitle("Assign Task | Select Juniors");

        taskName = getIntent().getStringExtra("name");
        taskDesc = getIntent().getStringExtra("desc");
        taskDuration = getIntent().getStringExtra("duration");
        toggleStates = new HashMap<>();

        init();
    }

    private void init(){
        filter = findViewById(R.id.juniorFilter);
        listView = findViewById(R.id.juniorList);

        fetchedData = new ArrayList<>();
        juniorsInvolved = new HashMap<>();

        userDBO = new UserDBO(this);

        userDBO.getUserList(new UserDBO.ListCallback<ArrayList<User>>() {
            @Override
            public void callback(ArrayList<User> data) {
                fetchedData.clear();
                fetchedData.addAll(data);
                adapter = new SelectJuniorAdapter(SelectJuniors.this,data);
                listView.setAdapter(adapter);
            }
        });

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

    public void makeTask(View view){
        fillUpJuniors();

            if(juniorsInvolved.isEmpty()){
                Toast.makeText(this, "Please select atleast one junior!", Toast.LENGTH_SHORT).show();
            }
            else{
                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String formattedDate = df.format(c.getTime());

                String seniorUID = FirebaseAuth.getInstance().getCurrentUser().getUid();

                Task task = new Task(taskName,taskDesc,"pending",formattedDate,taskDuration,seniorUID,juniorsInvolved);

                //Make entry in taskDBO
                TaskDBO taskDBO = new TaskDBO(this);
                taskDBO.addTask(task);
            }
    }

    private void fillUpJuniors(){
        for(Map.Entry<String,Boolean> entry : toggleStates.entrySet()){
            if(entry.getValue()){
                //Need to add this user in
                String uid = fetchUID(entry.getKey());
                juniorsInvolved.put(uid,"true");
            }
        }
    }

    private String fetchUID(String email){
        String uid = null;

        for(User user : fetchedData){
            if(user.getEmail().equalsIgnoreCase(email)){
                uid = user.getUid();
                break;
            }
        }

        return uid;
    }
}
