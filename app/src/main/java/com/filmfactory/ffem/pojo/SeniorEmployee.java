package com.filmfactory.ffem.pojo;

import java.util.HashMap;

/**
 * Created by Mohsin on 11/11/2017.
 */

public class SeniorEmployee extends Employee {

    HashMap<String,String> tasks;

    public SeniorEmployee(){
        super();
        tasks = new HashMap<String,String>();
    }

    public SeniorEmployee(String _email, String _name, String _role){
        super(_email,_name,_role);
        tasks = new HashMap<String,String>();
    }

    public HashMap<String,String> getTasks(){
        return tasks;
    }

    public void setTasks(HashMap<String,String> list){
        tasks = list;
    }

    public void addTask(String _junior){
        tasks.put(_junior,"true");
    }
}
