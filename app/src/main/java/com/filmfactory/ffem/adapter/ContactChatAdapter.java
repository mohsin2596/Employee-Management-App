package com.filmfactory.ffem.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.filmfactory.ffem.Chat;
import com.filmfactory.ffem.R;
import com.filmfactory.ffem.pojo.User;

import java.util.ArrayList;

/**
 * Created by Mohsin on 11/27/2017.
 */

public class ContactChatAdapter extends BaseAdapter implements Filterable {

    Context context;
    ArrayList<User> userList;
    ArrayList<User> filterList;
    LayoutInflater layoutInflater;
    Filter filter;

    public ContactChatAdapter(Context _context, ArrayList<User> list){

        context = _context;
        layoutInflater = ((Activity) context).getLayoutInflater();

        userList = new ArrayList<User>();
        filterList = new ArrayList<User>();

        userList.addAll(list);
        filterList.addAll(list);
    }

    @Override
    public int getCount() {
        return filterList.size();
    }

    @Override
    public Object getItem(int i) {
        return filterList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View currRow = view;
        final int index = i;

        if(view == null){
            currRow = layoutInflater.inflate(R.layout.contact_item, viewGroup, false);
        }

        TextView userName = currRow.findViewById(R.id.userName);
        TextView userRole = currRow.findViewById(R.id.userRole);

        userName.setText(filterList.get(i).getName());
        userRole.setText(filterList.get(i).getRole());

        CardView cardView = currRow.findViewById(R.id.card_view);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,Chat.class);
                intent.putExtra("chatWith",filterList.get(index).getUid());
                intent.putExtra("chatWithName",filterList.get(index).getName());
                context.startActivity(intent);
            }
        });

        return currRow;
    }

    @Override
    public Filter getFilter() {
        if(filter == null)
            filter = new UserFilter();

        return filter;
    }

    private class UserFilter extends android.widget.Filter{

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            FilterResults filterResults = new FilterResults();

            if(charSequence != null && charSequence.length() > 0){
                ArrayList<User> filterdUser = new ArrayList<User>();

                for(int i = 0; i < userList.size(); i++){
                    String uName = userList.get(i).getName().toLowerCase();

                    if(uName.contains(charSequence.toString().toLowerCase())){
                        filterdUser.add(userList.get(i));
                    }
                }

                filterResults.count = filterdUser.size();
                filterResults.values = filterdUser;
            } else{
                filterResults.count = userList.size();
                filterResults.values = userList;
            }

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            filterList = (ArrayList<User>) filterResults.values;
            notifyDataSetChanged();
        }
    }
}
