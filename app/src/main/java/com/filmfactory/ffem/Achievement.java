package com.filmfactory.ffem;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Achievement extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievement);
        getSupportActionBar().setTitle("My Achievements");
    }
}
