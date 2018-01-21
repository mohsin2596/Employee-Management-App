package com.filmfactory.ffem;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;

import com.filmfactory.ffem.adapter.SearchUserAdapter;
import com.filmfactory.ffem.dbo.UserDBO;
import com.filmfactory.ffem.pojo.User;

import java.util.ArrayList;

public class SearchUser extends AppCompatActivity {

    ListView listView;
    EditText filter;
    SearchUserAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);
        getSupportActionBar().setTitle("Search User");
        init();
    }

    private void init(){
        listView = findViewById(R.id.userList);
        filter = findViewById(R.id.userName);

        UserDBO userDBO = new UserDBO(this);
        userDBO.getUserList(new UserDBO.ListCallback<ArrayList<User>>() {
            @Override
            public void callback(ArrayList<User> data) {
                adapter = new SearchUserAdapter(SearchUser.this,data);
                listView.setAdapter(adapter);
                listView.invalidate();
            }
        });

        filter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    adapter.getFilter().filter(charSequence);
                    listView.invalidate();
                } catch (Exception e){}
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }
}
