package com.filmfactory.ffem;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import com.filmfactory.ffem.adapter.ContactChatAdapter;
import com.filmfactory.ffem.dbo.UserDBO;
import com.filmfactory.ffem.pojo.User;

import java.util.ArrayList;

/**
 * Created by Mohsin on 11/27/2017.
 */

public class ContactChatFragment extends Fragment {

    ContactChatAdapter adapter;
    ListView listView;
    EditText filterUser;

    public ContactChatFragment() {
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
        View view =  inflater.inflate(R.layout.fragment_contact_chat, container, false);

        listView = view.findViewById(R.id.userList);
        filterUser = view.findViewById(R.id.userFilter);

        UserDBO userDBO = new UserDBO(getActivity());


        userDBO.getUserList(new UserDBO.ListCallback<ArrayList<User>>() {
            @Override
            public void callback(ArrayList<User> data) {
                adapter = new ContactChatAdapter(getActivity(),data);
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

        return view;
    }
}
