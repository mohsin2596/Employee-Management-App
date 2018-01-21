package com.filmfactory.ffem;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    EditText emailText, passwordText;
    Button loginButton;
    FirebaseAuth firebaseAuth;
    ProgressDialog progress;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("FFEM | Login");
        firebaseAuth = FirebaseAuth.getInstance();

        sharedPreferences = getSharedPreferences("userRole",MODE_PRIVATE);

        if(firebaseAuth.getCurrentUser() != null){
            //Take user to main homepage
            String role = sharedPreferences.getString("role","admin");

            if(role.equalsIgnoreCase("senior") || role.equalsIgnoreCase("junior")){
                startActivity(new Intent(this,EmployeeActivity.class));
            }
            else{
                startActivity(new Intent(this,DbAdminActivity.class));
            }
        }

        setContentView(R.layout.activity_main);

        init();
    }

    private void init(){

        emailText = findViewById(R.id.EmailText);
        passwordText = findViewById(R.id.PasswordText);
        loginButton = findViewById(R.id.LoginButton);
        progress = new ProgressDialog(this);
        progress.setTitle("Logging in");
        progress.setMessage("Please wait ...");

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailText.getText().toString();
                final String password = passwordText.getText().toString();

                if(email.isEmpty() || password.isEmpty()){
                    Toast.makeText(MainActivity.this, "Email and password cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                progress.show();
                //authenticate user with firebase
                firebaseAuth.signInWithEmailAndPassword(email,password)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(!task.isSuccessful()){

                                    Toast.makeText(MainActivity.this, "Error logging in", Toast.LENGTH_SHORT).show();
                                    progress.dismiss();

                                }
                                else{
                                    progress.dismiss();
                                    sharedPreferences.edit().putString("pass",password).commit();
                                    Toast.makeText(MainActivity.this, "Login success", Toast.LENGTH_SHORT).show();
                                    String uid = task.getResult().getUser().getUid();
                                    Intent intent = new Intent(MainActivity.this,WelcomeActivity.class);
                                    intent.putExtra("uid",uid);
                                    startActivity(intent);
                                }
                            }
                        });
            }
        });



    }

    @Override
    public void onBackPressed(){
        return;
    }
}
