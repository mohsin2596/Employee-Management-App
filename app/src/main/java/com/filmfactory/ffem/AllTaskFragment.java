package com.filmfactory.ffem;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.ListView;

import com.filmfactory.ffem.adapter.AssignedTaskListAdapter;
import com.filmfactory.ffem.dbo.EmployeeDBO;
import com.filmfactory.ffem.pojo.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mohsin on 11/11/2017.
 */

public class AllTaskFragment extends Fragment {

    AssignedTaskListAdapter adapter;

    public AllTaskFragment() {
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

        View view = inflater.inflate(R.layout.fragment_alltask, container, false);

        final ListView listView = view.findViewById(R.id.assignedTaskList);
        EditText taskName = view.findViewById(R.id.taskName);


        EmployeeDBO employeeDBO = new EmployeeDBO(getActivity());
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        employeeDBO.getTasksSenior(uid, new EmployeeDBO.listCallback<ArrayList<Task>>() {
            @Override
            public void listFetchComplete(ArrayList<Task> data) {
                adapter = new AssignedTaskListAdapter(getActivity(),data);
                listView.setAdapter(adapter);
            }
        });

        taskName.addTextChangedListener(new TextWatcher() {
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
