package com.filmfactory.ffem;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.filmfactory.ffem.pojo.Employee;
import com.filmfactory.ffem.pojo.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class WelcomeActivity extends AppCompatActivity {
    ProgressDialog progressDialog;
    String uid;
    DatabaseReference mDatabase;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        try {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        sharedPreferences = getSharedPreferences("userRole",MODE_PRIVATE);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Fetching Data");
        progressDialog.setMessage("Please wait");
        progressDialog.show();
        uid = getIntent().getStringExtra("uid");
        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        mDatabase.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                Log.d("User name: ", user.getName());
                Log.d("User email",user.getEmail());
                Log.d("User role",user.getRole());
                progressDialog.dismiss();

                String role = user.getRole();

                if(role.equalsIgnoreCase("Database Admin")){
                    sharedPreferences.edit().putString("email",user.getEmail()).commit();
                    sharedPreferences.edit().putString("name",user.getName()).commit();
                    sharedPreferences.edit().putString("role","admin").commit();
                    Intent intent = new Intent(WelcomeActivity.this,DbAdminActivity.class);
                    startActivity(intent);
                }
                else if (role.equalsIgnoreCase("Junior Employee")){
                    sharedPreferences.edit().putString("role","junior").commit();
                    sharedPreferences.edit().putString("email",user.getEmail()).commit();
                    sharedPreferences.edit().putString("name",user.getName()).commit();
                    Log.d("Role","junior saved");
                    Intent intent = new Intent(WelcomeActivity.this, EmployeeActivity.class);
                    startActivity(intent);
                }
                else if(role.equalsIgnoreCase("Senior Employee")){
                    sharedPreferences.edit().putString("role","senior").commit();
                    sharedPreferences.edit().putString("email",user.getEmail()).commit();
                    sharedPreferences.edit().putString("name",user.getName()).commit();
                    Log.d("Role","senior saved");
                    Intent intent = new Intent(WelcomeActivity.this, EmployeeActivity.class);
                    startActivity(intent);
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
