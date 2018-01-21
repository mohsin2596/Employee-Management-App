package com.filmfactory.ffem;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;

import com.filmfactory.ffem.adapter.UserRemoveAdapter;
import com.filmfactory.ffem.adapter.UserUpdateAdapter;
import com.filmfactory.ffem.dbo.AdminDBO;
import com.filmfactory.ffem.dbo.UserDBO;
import com.filmfactory.ffem.pojo.User;

import java.util.ArrayList;

public class UpdateUser extends AppCompatActivity {

    UserDBO userDBO;
    AdminDBO adminDBO;
    ListView listView;
    EditText filterUser;
    UserUpdateAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);
        getSupportActionBar().setTitle("Update User");
        init();
    }

    private void init(){
        listView = findViewById(R.id.userList);
        filterUser = findViewById(R.id.filterUser);

        userDBO = new UserDBO(this);
        adminDBO = new AdminDBO(this);


        userDBO.getUserList(new UserDBO.ListCallback<ArrayList<User>>() {
            @Override
            public void callback(ArrayList<User> data) {
                adapter = new UserUpdateAdapter(UpdateUser.this,data);
                listView.setAdapter(adapter);
            }
        });

        filterUser.addTextChangedListener(new TextWatcher() {
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
