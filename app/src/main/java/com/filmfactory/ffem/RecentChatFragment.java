package com.filmfactory.ffem;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.filmfactory.ffem.adapter.RecentChatAdapter;
import com.filmfactory.ffem.dbo.ChatDBO;
import com.filmfactory.ffem.pojo.RecentChat;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

/**
 * Created by Mohsin on 11/27/2017.
 */

public class RecentChatFragment extends Fragment {

    ListView recentChatList;
    RecentChatAdapter adapter;

    public RecentChatFragment() {
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
        View view = inflater.inflate(R.layout.fragment_recent_chat, container, false);

        recentChatList = view.findViewById(R.id.recentChatList);

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        ChatDBO chatDBO = new ChatDBO(getActivity());
        chatDBO.getRecentChat(uid, new ChatDBO.RecentChatCallback<ArrayList<RecentChat>>() {
            @Override
            public void onFetchComplete(ArrayList<RecentChat> data) {
                for(RecentChat chat : data){
                   try{
                        Log.d("Recent",String.valueOf(data.size()));
                   }
                   catch (NullPointerException e){}
                    adapter = new RecentChatAdapter(getActivity(),data);
                    recentChatList.setAdapter(adapter);
                    recentChatList.invalidate();

                }
            }
        });



        return view;
    }
}
