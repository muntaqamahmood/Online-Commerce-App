package com.example.b07project;

public class StoreOwner extends Account {

    public StoreOwner() {
    }

    public StoreOwner(String username, String password){
        this.username = username;
        this.password = password;
    }

    public String getUsername(){return username;}

    public String getPassword(){return password;}

    public void setUsername(String username){this.username = username;}

    public void setPassword(String password){this.password = password;}

}
