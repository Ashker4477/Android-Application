package com.example.loginapp;

public class UserProfile {
    public String userEmail;
    public String userName;
    public String userPassword;
    public String phone;

    public UserProfile(){



    }



    public UserProfile(String userEmail, String userName,String userPassword, String phone) {
        this.userEmail = userEmail;
        this.userName = userName;
        this.userPassword=userPassword;
        this.phone = phone;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword(){ return userPassword; }

    public void setUserPassword(String userPassword){ this.userPassword=userPassword; }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
