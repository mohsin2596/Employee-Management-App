package com.filmfactory.ffem.pojo;

import java.util.HashMap;

/**
 * Created by Mohsin on 11/11/2017.
 */

public class JuniorEmployee extends Employee {
    HashMap<String,String> tasks;

    public JuniorEmployee(){
        super();
        tasks = new HashMap<String,String>();
        tasks.put("default","false");
    }

    public JuniorEmployee(String _email, String _name, String _role){
        super(_email,_name,_role);
        tasks = new HashMap<String,String>();
        tasks.put("default","false");
    }

    public HashMap<String,String> getTasks(){
        return tasks;
    }

    public void setTasks(HashMap<String,String> taskList) {tasks = taskList;}

    public void addTask(String _task){
        tasks.put(_task,"true");
    }
}
