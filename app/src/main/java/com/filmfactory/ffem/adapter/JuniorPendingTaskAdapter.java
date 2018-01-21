package com.filmfactory.ffem.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.filmfactory.ffem.EmployeeActivity;
import com.filmfactory.ffem.R;
import com.filmfactory.ffem.pojo.Employee;
import com.filmfactory.ffem.pojo.Task;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * Created by Mohsin on 11/14/2017.
 */

public class JuniorPendingTaskAdapter extends BaseAdapter implements Filterable {

    Context context;
    ArrayList<Task> allTasks;
    ArrayList<Task> filteredList;
    LayoutInflater layoutInflater;
    Filter filter;

    public JuniorPendingTaskAdapter(Context _context, ArrayList<Task> tasksList){
        context = _context;
        allTasks = new ArrayList<>();
        filteredList = new ArrayList<>();

        allTasks.addAll(tasksList);
        filteredList.addAll(tasksList);

        layoutInflater = ((Activity) context).getLayoutInflater();
    }

    @Override
    public int getCount() {
        return filteredList.size();
    }

    @Override
    public Object getItem(int i) {
        return filteredList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View currRow = view;
        final int index = i;

        if (currRow == null){
            currRow = layoutInflater.inflate(R.layout.pending_task_item,viewGroup,false);
        }

        CardView cardView = currRow.findViewById(R.id.card_view);
        final TextView taskName = currRow.findViewById(R.id.taskName);
        final TextView taskStatus = currRow.findViewById(R.id.taskStatus);
        TextView taskDate = currRow.findViewById(R.id.taskDate);
        TextView taskDuration = currRow.findViewById(R.id.taskDuration);
        Button infoButton = currRow.findViewById(R.id.viewInfo);
        Button updateStatus = currRow.findViewById(R.id.updateStatus);

        taskName.setText(filteredList.get(i).getTaskName());
        taskStatus.setText("Status: "+filteredList.get(i).getTaskStatus());
        taskDate.setText(filteredList.get(i).getTaskAddedOn());
        taskDuration.setText(filteredList.get(i).getDurationInDays() +" Days");

/*        Date date = new Date(filteredList.get(i).getTaskAddedOn());
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, Integer.parseInt(filteredList.get(i).getDurationInDays()));

        Calendar currDate = Calendar.getInstance();
        currDate.setTime(new Date());

        int lateCheck = currDate.compareTo(c);

        if(lateCheck>0){
            cardView.setCardBackgroundColor(Color.RED);
        }
*/

        //Info button on click definition
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater li = LayoutInflater.from(context);
                View dialogView = li.inflate(R.layout.task_dialog_info_item,null ,false);
                final TextView juniorsT = dialogView.findViewById(R.id.juniorsAssigned);

                final StringBuffer stringBuffer = new StringBuffer();

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Junior Employee");

                for(final Map.Entry<String,String> juniors : filteredList.get(index).getJuniorEmployees().entrySet()){
                    reference.child(juniors.getKey()).child("name").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            stringBuffer.append(dataSnapshot.getValue()+"\n");
                            juniorsT.setText(stringBuffer);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

                TextView taskDesc = dialogView.findViewById(R.id.taskDesc);
                taskDesc.setText(filteredList.get(index).getTaskDesc());

                final AlertDialog.Builder alert;
                alert = new AlertDialog.Builder(context);
                alert.setTitle("Task Info");
                alert.setView(dialogView);

                alert.show();

            }
        });

        updateStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater li = LayoutInflater.from(context);
                View dialogView = li.inflate(R.layout.custom_update_status,null ,false);

                final Spinner statusSpinner = dialogView.findViewById(R.id.statusSpinner);
                TextView taskTitle = dialogView.findViewById(R.id.taskTitle);
                taskTitle.setText(filteredList.get(index).getTaskName());

                String[] userRoles = context.getResources().getStringArray(R.array.task_status_list);

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item,userRoles);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                statusSpinner.setAdapter(adapter);

                AlertDialog.Builder alert;
                alert = new AlertDialog.Builder(context);
                alert.setTitle("Update Status");
                alert.setView(dialogView);

                final ProgressDialog progressDialog = new ProgressDialog(context);
                progressDialog.setTitle("Updating Status");
                progressDialog.setMessage("Please wait ...");

                alert.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        progressDialog.show();
                        String updatedStatus = statusSpinner.getSelectedItem().toString();
                        //Toast.makeText(context, updatedStatus, Toast.LENGTH_SHORT).show();
                        FirebaseDatabase.getInstance().getReference("tasks").child(filteredList.get(index).getTaskID())
                                .child("taskStatus").setValue(updatedStatus).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
                                progressDialog.dismiss();
                                Toast.makeText(context, "Task Updated!", Toast.LENGTH_SHORT).show();
                                context.startActivity(new Intent(context, EmployeeActivity.class));
                            }
                        });
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

        return currRow;
    }

    @Override
    public Filter getFilter() {

        if(filter == null){
            filter = new TaskFilter();
        }

        return filter;
    }

    private class TaskFilter extends android.widget.Filter{

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            FilterResults filterResults = new FilterResults();

            if(charSequence != null && charSequence.length() > 0){
                ArrayList<Task> filterdTask = new ArrayList<Task>();

                for(int i = 0; i < allTasks.size(); i++){
                    String taskName = allTasks.get(i).getTaskName().toLowerCase();

                    if(taskName.contains(charSequence.toString().toLowerCase())){
                        filterdTask.add(allTasks.get(i));
                    }
                }

                filterResults.count = filterdTask.size();
                filterResults.values = filterdTask;
            } else{
                filterResults.count = allTasks.size();
                filterResults.values = allTasks;
            }

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            filteredList = (ArrayList<Task>) filterResults.values;
            notifyDataSetChanged();
        }
    }

}
