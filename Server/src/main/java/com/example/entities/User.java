package com.example.entities;

public class User {
    public int id;
    public String login;
    public String pass;
    public String role;
    public User(int id, String login, String pass, String role){
        this.id =id;
        this.login = login;
        this.pass = pass;
        this.role = role;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public void setLogin(String login) {
        this.login = login;
    }
    public String getLogin() {
        return login;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
    public String getPass() {
        return pass;
    }

    public void setRole(String role) {
        this.role = role;
    }
    public String getRole() {
        return role;
    }

    @Override
    public String toString(){
        return String.format("ID:%s | login:%s | pass:%s | role:%s ",
                this.id,this.login,this.pass,this.role);
    }
}
