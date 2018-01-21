package com.filmfactory.ffem.dbo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.filmfactory.ffem.AddRole;
import com.filmfactory.ffem.AdminActivity;
import com.filmfactory.ffem.pojo.JuniorEmployee;
import com.filmfactory.ffem.pojo.SeniorEmployee;
import com.filmfactory.ffem.pojo.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Mohsin on 11/9/2017.
 */

public class AdminDBO {
    Context context;
    FirebaseAuth auth;
    FirebaseDatabase mDatabase;
    ProgressDialog progressDialog;
    boolean addUserReturn = false;

    public AdminDBO(Context _context){
        context = _context;
        progressDialog = new ProgressDialog(context);
        auth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
    }

    public boolean addUser (final User user, final String uid){

        progressDialog.setTitle("Updating Database");
        progressDialog.setMessage("Please wait ...");

        progressDialog.show();
        mDatabase.getReference("users").child(uid).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                addUserReturn = task.isSuccessful();
                progressDialog.dismiss();
                Toast.makeText(context, "User added!", Toast.LENGTH_SHORT).show();
                if(!user.getRole().equalsIgnoreCase("Database Admin")) {
                    String role = user.getRole();
                    User addType = null;

                    if(role.equalsIgnoreCase("Junior Employee"))
                        addType = new JuniorEmployee(user.getEmail(),user.getName(),user.getRole());
                    else if(role.equalsIgnoreCase("Senior Employee"))
                        addType = new SeniorEmployee(user.getEmail(),user.getName(),user.getRole());

                    mDatabase.getReference(role).child(uid).setValue(addType);
                }
                context.startActivity(new Intent(context,AdminActivity.class));


            }
        });
        return addUserReturn;
    }

    public void removeUser(final User user){

        final User userC = user;


        progressDialog.setTitle("Removing User");
        progressDialog.setMessage("Please wait...");

        progressDialog.show();
        mDatabase.getReference("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot userSnapshot: dataSnapshot.getChildren()){
                    User userS = userSnapshot.getValue(User.class);
                    if(userS.getEmail().equalsIgnoreCase(userC.getEmail())){
                        //Need to remove this user
                        mDatabase.getReference("users").child(userSnapshot.getKey()).removeValue().addOnCompleteListener(
                                new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        mDatabase.getReference(user.getRole()).child(user.getUid()).removeValue();
                                        context.startActivity(new Intent(context,AdminActivity.class));
                                        progressDialog.dismiss();
                                    }
                                }
                        );
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void updateUser(final String uid, final String name, final String email , final String role,final String oldRole, final boolean neeedToUpdate){
        progressDialog.setTitle("Updating user");
        progressDialog.setMessage("Please wait ...");

        progressDialog.show();


        mDatabase.getReference("users").child(uid).child("name").setValue(name).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                mDatabase.getReference("users").child(uid).child("role").setValue(role).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //Check if need to update
                        if(neeedToUpdate){
                            mDatabase.getReference(oldRole).child(uid).removeValue();

                            User addNew = null;

                            //Add new role data here
                            if(role.equalsIgnoreCase("Junior Employee"))
                                addNew = new JuniorEmployee(email,name,role);
                            else if(role.equalsIgnoreCase("Senior Employee"))
                                addNew = new SeniorEmployee(email,name,role);


                            mDatabase.getReference(role).child(uid).setValue(addNew);
                        }

                        progressDialog.dismiss();
                    }
                });
            }
        });

    }
}
