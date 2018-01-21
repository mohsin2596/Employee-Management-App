package com.filmfactory.ffem;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.filmfactory.ffem.dbo.AdminDBO;
import com.filmfactory.ffem.pojo.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class AddUser extends AppCompatActivity {

    FirebaseAuth auth;
    EditText fullName,userEmail,userPass;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        getSupportActionBar().setTitle("Add User");

        init();
    }

    private void init(){
        fullName = findViewById(R.id.FullName);
        userEmail = findViewById(R.id.UserEmail);
        userPass = findViewById(R.id.PasswordText);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Authenticating User");
        progressDialog.setMessage("Please wait ...");

    }

    public void addUser(View view){
        final String fname = fullName.getText().toString().trim();
        final String uemail = userEmail.getText().toString().trim();
        String upass = userPass.getText().toString().trim();

        if(fname.isEmpty() || uemail.isEmpty() || upass.isEmpty())
            Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show();

        else{
            //All fields are entered

            //Add to firebase auth
            auth = FirebaseAuth.getInstance();

            progressDialog.show();

            auth.createUserWithEmailAndPassword(uemail,upass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progressDialog.dismiss();

                    if(!task.isSuccessful()){
                        Toast.makeText(AddUser.this, "Error adding user!", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        //Redirect to assign role
                        Intent intent = new Intent(AddUser.this,AddRole.class);
                        intent.putExtra("email",uemail);
                        intent.putExtra("fullname",fname);
                        intent.putExtra("uid",task.getResult().getUser().getUid());
                        startActivity(intent);
                    }
                }
            });


        }
    }
}
