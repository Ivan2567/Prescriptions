package com.example.entities;

public class Doctor {
    public int id;
    public String f;
    public String i;
    public String o;
    public String ecp;

    public Doctor(int id, String f, String i, String o, String ecp){
        this.id =id;
        this.f = f;
        this.i = i;
        this.o = o;
        this.ecp = ecp;
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

    public void setEcp(String ecp) {
        this.ecp = ecp;
    }
    public String getEcp() {
        return ecp;
    }

    @Override
    public String toString(){
        return String.format("ID:%s | F:%s | I:%s | O:%s | ecp:%s",
                this.id,this.f,this.i,this.o,this.ecp);
    }
}
