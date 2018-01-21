package com.filmfactory.ffem.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.filmfactory.ffem.Chat;
import com.filmfactory.ffem.R;
import com.filmfactory.ffem.pojo.RecentChat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Created by Mohsin on 11/27/2017.
 */

public class RecentChatAdapter extends BaseAdapter implements Filterable {

    Context context;
    ArrayList<RecentChat> recentList;
    LayoutInflater layoutInflater;

    public RecentChatAdapter(Context _context, ArrayList<RecentChat> data){
        context = _context;
        recentList = new ArrayList<RecentChat>();
        recentList.addAll(data);
        Collections.sort(recentList);
        layoutInflater = ((Activity) context).getLayoutInflater();
    }

    @Override
    public int getCount() {
        return recentList.size();
    }

    @Override
    public Object getItem(int i) {
        return recentList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View currRow = view;
        final int index = i;

        if (view == null){
            currRow = layoutInflater.inflate(R.layout.recent_chat_item,viewGroup,false);
        }

        TextView chatName = currRow.findViewById(R.id.chatName);
        TextView chatDate = currRow.findViewById(R.id.chatDate);
        TextView lastMessage = currRow.findViewById(R.id.lastMessage);

        String lastMsg = recentList.get(i).getLastMessage();
        if(lastMsg.length() > 100){
            lastMsg = lastMsg.substring(0,100);
        }

        chatName.setText(recentList.get(i).getChatWith());
        chatDate.setText(recentList.get(i).getDate());
        lastMessage.setText(lastMsg);

        currRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,Chat.class);
                intent.putExtra("chatWith",recentList.get(index).getChatWithUID());
                intent.putExtra("chatWithName",recentList.get(index).getChatWith());
                context.startActivity(intent);
            }
        });

        return currRow;
    }

    @Override
    public Filter getFilter() {
        return null;
    }
}
