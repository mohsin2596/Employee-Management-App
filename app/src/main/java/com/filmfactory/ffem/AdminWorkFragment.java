package com.filmfactory.ffem;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Mohsin on 12/1/2017.
 */

public class AdminWorkFragment extends Fragment {

    public AdminWorkFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_adminwork, container, false);

        Button addUser, updateUser, removeUser;

        addUser = view.findViewById(R.id.AddUser);
        updateUser = view.findViewById(R.id.UpdateUser);
        removeUser = view.findViewById(R.id.RemoveUser);

        addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),AddUser.class);
                startActivity(intent);
            }
        });

        updateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),UpdateUser.class);
                startActivity(intent);
            }
        });

        removeUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),RemoveUser.class);
                startActivity(intent);
            }
        });

        return view;
    }

}
