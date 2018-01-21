package com.filmfactory.ffem.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;

import com.filmfactory.ffem.R;
import com.filmfactory.ffem.UpdateUserTwo;
import com.filmfactory.ffem.dbo.AdminDBO;
import com.filmfactory.ffem.pojo.User;

import java.util.ArrayList;

/**
 * Created by Mohsin on 11/11/2017.
 */

public class UserUpdateAdapter extends BaseAdapter implements Filterable {

    ArrayList<User> users;
    Context context;
    LayoutInflater layoutInflater;
    ArrayList<User> filteredApps;
    Filter filter;
    AdminDBO adminDBO;

    public UserUpdateAdapter(Context _context, ArrayList<User> list){
        users = new ArrayList<User>();
        filteredApps = new ArrayList<User>();
        users.addAll(list);
        filteredApps.addAll(list);
        context = _context;
        adminDBO = new AdminDBO(context);
        layoutInflater = ((Activity) context).getLayoutInflater();
    }

    @Override
    public int getCount() {
        return filteredApps.size();
    }

    @Override
    public Object getItem(int i) {
        return filteredApps.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View currRow = view;

        if(view == null){
            currRow = layoutInflater.inflate(R.layout.user_item_update, viewGroup, false);
        }

        TextView userEmail = currRow.findViewById(R.id.email);
        Button removeUser = currRow.findViewById(R.id.updateButton);

        final int updateIndex = i;

        userEmail.setText(filteredApps.get(i).getEmail());

        removeUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpdateUserTwo.class);
                intent.putExtra("email",filteredApps.get(updateIndex).getEmail());
                intent.putExtra("fullname",filteredApps.get(updateIndex).getName());
                intent.putExtra("role",filteredApps.get(updateIndex).getRole());
                intent.putExtra("uid",filteredApps.get(updateIndex).getUid());

                context.startActivity(intent);

            }
        });

        return currRow;
    }

    @Override
    public android.widget.Filter getFilter() {
        if(filter == null){
            filter = new UserFilter();
        }
        return filter;
    }

    private class UserFilter extends android.widget.Filter{

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            FilterResults filterResults = new FilterResults();

            if(charSequence != null && charSequence.length() > 0){
                ArrayList<User> filterdUser = new ArrayList<User>();

                for(int i = 0; i < users.size(); i++){
                    String uEmail = users.get(i).getEmail();

                    if(uEmail.contains(charSequence.toString().toLowerCase())){
                        filterdUser.add(users.get(i));
                    }
                }

                filterResults.count = filterdUser.size();
                filterResults.values = filterdUser;
            } else{
                filterResults.count = users.size();
                filterResults.values = users;
            }

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            filteredApps = (ArrayList<User>) filterResults.values;
            notifyDataSetChanged();
        }
    }
}
