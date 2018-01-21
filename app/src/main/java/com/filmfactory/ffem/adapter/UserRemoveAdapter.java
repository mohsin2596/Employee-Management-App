package com.filmfactory.ffem.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;

import com.filmfactory.ffem.R;
import com.filmfactory.ffem.dbo.AdminDBO;
import com.filmfactory.ffem.pojo.User;

import java.util.ArrayList;


/**
 * Created by Mohsin on 11/9/2017.
 */

public class UserRemoveAdapter extends BaseAdapter implements Filterable {

    ArrayList<User> users;
    Context context;
    LayoutInflater layoutInflater;
    ArrayList<User> filteredApps;
    Filter filter;
    AdminDBO adminDBO;

    public UserRemoveAdapter(Context _context, ArrayList<User> list){
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
            currRow = layoutInflater.inflate(R.layout.user_item, viewGroup, false);
        }

        TextView userEmail = currRow.findViewById(R.id.email);
        ImageButton removeUser = currRow.findViewById(R.id.removeButton);

        final int removeIndex = i;

        userEmail.setText(filteredApps.get(i).getEmail());

        removeUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("Remove User");
                alert.setMessage("Are you sure you want to remove user?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        adminDBO.removeUser(filteredApps.get(removeIndex));
                        users.remove(filteredApps.get(removeIndex));
                        filteredApps.remove(removeIndex);
                        notifyDataSetChanged();
                    }
                });

                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                alert.show();
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
