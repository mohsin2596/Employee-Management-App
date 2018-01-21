package com.filmfactory.ffem.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.filmfactory.ffem.R;
import com.filmfactory.ffem.SelectJuniors;
import com.filmfactory.ffem.pojo.JuniorEmployee;
import com.filmfactory.ffem.pojo.User;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Mohsin on 11/13/2017.
 */

public class SelectJuniorAdapter extends BaseAdapter implements Filterable {

    ArrayList<User> juniorEmployees;
    ArrayList<User> filteredUser;
    Context context;
    LayoutInflater layoutInflater;
    Filter filter;


    public SelectJuniorAdapter(Context _context, ArrayList<User> users){
        juniorEmployees = fillJuniors(users);
        filteredUser = new ArrayList<User>();
        filteredUser.addAll(juniorEmployees);

        fillHashMap();

        context = _context;
        layoutInflater = ((Activity) context).getLayoutInflater();
    }

    private void fillHashMap(){

        for(int i = 0; i < juniorEmployees.size(); i++){
            String email = juniorEmployees.get(i).getEmail();
            SelectJuniors.toggleStates.put(email,false);
        }
    }

    private ArrayList<User> fillJuniors(ArrayList<User> users){
        ArrayList<User> juniors = new ArrayList<User>();

        for(User user : users){
            if(user.getRole().equalsIgnoreCase("Junior Employee")){
                juniors.add(user);
            }
        }

        return juniors;

    }

    @Override
    public int getCount() {
        return filteredUser.size();
    }

    @Override
    public Object getItem(int i) {
        return filteredUser.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder holder;
        View currRow = view;

        if(currRow == null){
            currRow = layoutInflater.inflate(R.layout.add_junior_item, viewGroup, false);
            View row = currRow;
            holder = new ViewHolder(row);
            currRow.setTag(holder);
            holder.switchButton = (CheckBox) currRow.findViewById(R.id.lockToggle);
        }
        else {
            holder = (ViewHolder) currRow.getTag();
        }

        TextView userName = currRow.findViewById(R.id.userName);
        TextView userEmail = currRow.findViewById(R.id.userEmail);

        userName.setText(filteredUser.get(i).getName());
        userEmail.setText(filteredUser.get(i).getEmail());

        if(SelectJuniors.toggleStates.get(filteredUser.get(i).getEmail()))
            holder.switchButton.setChecked(true);
        else
            holder.switchButton.setChecked(false);

        final int index = i;

        holder.switchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBox tbtn = (CheckBox) view;
                String email = filteredUser.get(index).getEmail();

                if(tbtn.isChecked()){
                    //Follow this route as a workaround of android recycling bug
                    tbtn.setChecked(true);
                    SelectJuniors.toggleStates.put(email,true);
                }
                else{
                    tbtn.setChecked(false);
                    SelectJuniors.toggleStates.put(email,false);
                }
            }
        });

        return currRow;
    }

    @Override
    public Filter getFilter() {
        if (filter == null)
            filter = new UserSearchFilter();

        return filter;
    }

    private class UserSearchFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            FilterResults results = new FilterResults();

            if (charSequence != null && charSequence.length() > 0) {
                ArrayList<User> filteredList = new ArrayList<User>();

                for (int i = 0; i < juniorEmployees.size(); i++) {
                    String name = juniorEmployees.get(i).getName().toLowerCase();
                    if (name.contains(charSequence.toString().toLowerCase())) {
                        filteredList.add(juniorEmployees.get(i));
                    }
                }
                results.count = filteredList.size();
                results.values = filteredList;

            } else {
                results.count = juniorEmployees.size();
                results.values = juniorEmployees;
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            filteredUser = (ArrayList<User>) filterResults.values;
            notifyDataSetChanged();
        }
    }

    public class ViewHolder {
        CheckBox switchButton = null;

        ViewHolder(View base) {
            this.switchButton = (CheckBox) base.findViewById(R.id.lockToggle);
        }

    }
}
