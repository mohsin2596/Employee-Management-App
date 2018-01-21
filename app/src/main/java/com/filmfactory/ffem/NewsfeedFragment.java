package com.filmfactory.ffem;

/**
 * Created by Mohsin on 11/11/2017.
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.filmfactory.ffem.adapter.NewsfeedAdapter;
import com.filmfactory.ffem.dbo.NewsfeedDBO;
import com.filmfactory.ffem.pojo.Newsfeed;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class NewsfeedFragment extends Fragment{

    SharedPreferences sharedPreferences;

    NewsfeedDBO newsfeedDBO;

    public NewsfeedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_newsfeed, container, false);

        CardView cardView = view.findViewById(R.id.card_view);
        sharedPreferences = getActivity().getSharedPreferences("userRole", Context.MODE_PRIVATE);
        String role = sharedPreferences.getString("role","null");
        final String name = sharedPreferences.getString("name","null");

        if(role.equalsIgnoreCase("junior") || role.equalsIgnoreCase("admin"))
            cardView.setVisibility(View.GONE);



        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater li = LayoutInflater.from(getContext());
                View dialogView = li.inflate(R.layout.custom_post,null ,false);

                final AlertDialog.Builder alert;
                alert = new AlertDialog.Builder(getContext());
                alert.setTitle("Add Post");
                alert.setView(dialogView);

                newsfeedDBO = new NewsfeedDBO(getContext());

                final EditText userPost = dialogView.findViewById(R.id.userPost);

                alert.setPositiveButton("Post", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String post = userPost.getText().toString();

                        if(post.isEmpty()){
                            Toast.makeText(getContext(), "Please enter something!", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Calendar c = Calendar.getInstance();
                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String formattedDate = df.format(c.getTime());

                            newsfeedDBO.addPost(name, post, formattedDate, new NewsfeedDBO.addCallback() {
                                @Override
                                public void postAdded(boolean data) {
                                    Toast.makeText(getContext(), "Post callback", Toast.LENGTH_SHORT).show();
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
                alert.show();
            }
        });

        newsfeedDBO = new NewsfeedDBO(getActivity());
        //ListView
        final ListView listView = view.findViewById(R.id.newsfeedList);
        newsfeedDBO.getNewsfeed(new NewsfeedDBO.ListCallback<ArrayList<Newsfeed>>() {
            @Override
            public void callback(ArrayList<Newsfeed> data) {
                NewsfeedAdapter adapter = new NewsfeedAdapter(getActivity(),data);
                listView.setAdapter(adapter);
            }
        });


        return view;
    }

}
