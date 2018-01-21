package com.filmfactory.ffem;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {

    TextView name,email,role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setTitle("FFEM | Profile");
        init();
    }

    private void init(){
        name = findViewById(R.id.userName);
        email = findViewById(R.id.email);
        role = findViewById(R.id.userRole);

        name.setText(getIntent().getStringExtra("name"));
        email.setText(getIntent().getStringExtra("email"));
        role.setText(getIntent().getStringExtra("role"));


    }
}
