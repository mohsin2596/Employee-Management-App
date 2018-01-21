package com.filmfactory.ffem;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.filmfactory.ffem.dbo.AdminDBO;

public class UpdateUserTwo extends AppCompatActivity {

    String uid, fullname, email, role;
    TextView emailText;
    EditText nameText;
    Spinner roleSpinner;
    Button updateBtn;
    AdminDBO adminDBO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_two);

        uid = getIntent().getStringExtra("uid");
        email = getIntent().getStringExtra("email");
        fullname = getIntent().getStringExtra("fullname");
        role = getIntent().getStringExtra("role");

        init();
    }

    private void init(){
        emailText = findViewById(R.id.emailText);
        nameText = findViewById(R.id.FullName);
        roleSpinner = findViewById(R.id.roleSpinner);
        updateBtn = findViewById(R.id.updateButton);

        emailText.setText(email + " Role: " + role);
        nameText.setText(fullname);


        String[] userRoles = getResources().getStringArray(R.array.user_roles);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,userRoles);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        roleSpinner.setAdapter(adapter);

        adminDBO = new AdminDBO(this);
    }

    public void updateUser (View view){
        adminDBO.updateUser(uid,fullname,email,roleSpinner.getSelectedItem().toString(),role,!role.equalsIgnoreCase(roleSpinner.getSelectedItem().toString()));
    }
}
