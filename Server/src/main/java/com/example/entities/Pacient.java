package com.example.entities;

public class Pacient {
    public int id;
    public String f;
    public String i;
    public String o;
    public String polis;
    public String pass;
    public String email;
    public Pacient(int id, String f, String i, String o, String polis, String pass, String email){
        this.id =id;
        this.f = f;
        this.i = i;
        this.o = o;
        this.polis = polis;
        this.pass = pass;
        this.email = email;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public void setF(String f) {
        this.f = f;
    }
    public String getF() {
        return f;
    }

    public void setI(String i) {
        this.i = i;
    }
    public String getI() {
        return i;
    }

    public void setO(String o) {
        this.o = o;
    }
    public String getO() {
        return o;
    }

    public void setPolis(String polis) {
        this.polis = polis;
    }
    public String getPolis() {
        return polis;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
    public String getPass() {
        return pass;
    }

    public void setEmail(String email) {this.email = email;}
    public String getEmail() {return email;}

    @Override
    public String toString(){
        return String.format("ID:%s | F:%s | I:%s | O:%s | polis:%s",
                this.id,this.f,this.i,this.o,this.polis);
    }
}
