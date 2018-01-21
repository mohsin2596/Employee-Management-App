package com.filmfactory.ffem;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.filmfactory.ffem.dbo.AdminDBO;
import com.filmfactory.ffem.pojo.User;

public class AddRole extends AppCompatActivity {

    AdminDBO adminDBO;
    String email,fullname,uid;
    Spinner spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_role);

        email = getIntent().getStringExtra("email");
        fullname = getIntent().getStringExtra("fullname");
        uid = getIntent().getStringExtra("uid");

        init();
    }

    private void init(){
        adminDBO = new AdminDBO(this);
        spinner = findViewById(R.id.roleSpinner);


        String[] userRoles = getResources().getStringArray(R.array.user_roles);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,userRoles);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

    }

    public void addRole(View v){
        String role = spinner.getSelectedItem().toString();

        User user = new User(email,fullname,role);

        adminDBO.addUser(user,uid);

    }

    @Override
    public void onBackPressed(){
        return;
    }
}
