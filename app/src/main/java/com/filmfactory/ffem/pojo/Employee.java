package com.filmfactory.ffem.pojo;

/**
 * Created by Mohsin on 11/11/2017.
 */

public class Employee extends User {

    public Employee(){
        super();
    }

    public Employee(String _email, String _name, String _role){
        super(_email,_name,_role);
    }
}
