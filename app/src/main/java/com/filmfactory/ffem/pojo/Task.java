package com.filmfactory.ffem.pojo;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Mohsin on 11/11/2017.
 */

public class Task {
    String seniorEmployee;
    HashMap<String,String> juniorEmployees;

    String taskID;
    String taskName, taskDesc, taskStatus;
    String taskAddedOn;
    String durationInDays;

    public Task(){

    }

    public Task(String name, String desc, String status, String stringDate, String duration, String senior, HashMap<String,String> juniors){
        taskName = name;
        taskDesc = desc;
        taskStatus = status;
        taskAddedOn = stringDate;
        durationInDays = duration;
        seniorEmployee = senior;
        juniorEmployees = juniors;
    }

    public void setTaskID(String id){
        taskID = id;
    }

    public String getTaskID(){
        return taskID;
    }

    public void setTaskName(String name){
        taskName = name;
    }

    public String getTaskName(){
        return taskName;
    }

    public void setTaskDesc(String desc){
        taskDesc = desc;
    }

    public String getTaskDesc(){
        return taskDesc;
    }

    public void setTaskStatus(String status){
        taskStatus = status;
    }

    public String getTaskStatus(){
        return taskStatus;
    }

    public void setTaskAddedOn(String stringDate){
        taskAddedOn = stringDate;
    }

    public String getTaskAddedOn(){
        return taskAddedOn;
    }

    public void setDurationInDays(String duration) {
        durationInDays = duration;
    }

    public String getDurationInDays(){
        return durationInDays;
    }

    public void setSeniorEmployee (String senior){
        seniorEmployee = senior;
    }

    public String getSeniorEmployee(){
        return seniorEmployee;
    }

    public void setJuniorEmployees(HashMap<String,String> juniors){
        juniorEmployees = juniors;
    }

    public HashMap<String,String> getJuniorEmployees(){
        return juniorEmployees;
    }

    public void addJuniorEmployee(String junior){
        juniorEmployees.put(junior,"true");
    }

}
