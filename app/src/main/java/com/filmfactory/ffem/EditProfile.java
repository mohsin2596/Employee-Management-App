package com.filmfactory.ffem;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.filmfactory.ffem.dbo.NewsfeedDBO;
import com.filmfactory.ffem.pojo.Employee;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class EditProfile extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    String email, password, userName, userRole, uid;

    TextView userNameV, userRoleV, emailV;

    Button updatePassword, updateName;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        getSupportActionBar().setTitle("Update Profile");
        init();

    }

    private void init() {
        sharedPreferences = getSharedPreferences("userRole", MODE_PRIVATE);
        email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        password = sharedPreferences.getString("pass", "");
        userName = sharedPreferences.getString("name", "");
        userRole = sharedPreferences.getString("role", "");
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        emailV = findViewById(R.id.email);
        emailV.setText(email);

        userNameV = findViewById(R.id.userName);
        userNameV.setText(userName);

        userRoleV = findViewById(R.id.userRole);
        userRoleV.setText(userRole);

        updateName = findViewById(R.id.updateName);
        updatePassword = findViewById(R.id.updatePass);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Updating profile");
        progressDialog.setMessage("Please wait ...");

        LayoutInflater li = LayoutInflater.from(EditProfile.this);
        View dialogView = li.inflate(R.layout.custom_post, null, false);

        final EditText entry = dialogView.findViewById(R.id.userPost);
        entry.setHint("Enter name ...");

        final AlertDialog.Builder alert;
        alert = new AlertDialog.Builder(EditProfile.this);
        alert.setTitle("Update Name");
        alert.setView(dialogView);

        alert.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                final String post = entry.getText().toString().trim();

                if (post.isEmpty()) {
                    Toast.makeText(EditProfile.this, "Please enter something!", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.show();
                    FirebaseDatabase.getInstance().getReference("users").child(uid).child("name")
                            .setValue(post).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {
                                String role = "";
                                boolean needTo = false;

                                if(userRole.equalsIgnoreCase("junior")) {
                                    role = "Junior Employee";
                                    needTo = true;
                                }
                                else if(userRole.equalsIgnoreCase("senior")) {
                                    role = "Senior Employee";
                                    needTo = true;
                                }

                                if(needTo){
                                    FirebaseDatabase.getInstance().getReference(role).child("name")
                                            .setValue(post).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            progressDialog.dismiss();
                                            if(task.isSuccessful()){
                                                sharedPreferences.edit().putString("name", post).commit();
                                                startActivity(new Intent(EditProfile.this, EditProfile.class));

                                            }
                                        }
                                    });
                                }
                            }
                        }
                    });
                }
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });


        View passView = li.inflate(R.layout.custom_post, null, false);

        final EditText newPass = passView.findViewById(R.id.userPost);
        newPass.setHint("Enter pass ...");

        final AlertDialog.Builder passAlert;
        passAlert = new AlertDialog.Builder(EditProfile.this);
        passAlert.setTitle("Update Password");
        passAlert.setView(passView);

        passAlert.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                final String post = newPass.getText().toString().trim();

                if (post.isEmpty()) {
                    Toast.makeText(EditProfile.this, "Please enter something!", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.show();
                    FirebaseAuth.getInstance().getCurrentUser().updatePassword(post).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressDialog.dismiss();
                            if(task.isSuccessful()){
                                sharedPreferences.edit().putString("pass",post).commit();
                                startActivity(new Intent(EditProfile.this,EditProfile.class));
                            }
                        }
                    });
                }
            }
        });

        passAlert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        updateName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.show();
            }
        });

        updatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passAlert.show();
            }
        });
    }

    @Override
    public void onBackPressed(){
        if(userRole.equalsIgnoreCase("admin")){
            startActivity(new Intent(this,DbAdminActivity.class));
        }
        else
            startActivity(new Intent(this, EmployeeActivity.class));
    }
}
