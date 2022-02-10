package com.shcollege.bfitadmin;

public class User {
    public String firstname,lastname,emailRegister,password;
    public User(){

    }
    public User(String firstname,String lastname,String emailRegister,String password){
        this.firstname = firstname;
        this.lastname = lastname;
        this.emailRegister = emailRegister;
        this.password = password;
    }
}
