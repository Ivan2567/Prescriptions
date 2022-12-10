package com.example.entities;

public class Preparat {
    public int id;
    public int kolvo;
    public String sppr;
    public String edizm;

    public Preparat(int id, int kolvo, String sppr, String edizm){
        this.id =id;
        this.kolvo = kolvo;
        this.sppr = sppr;
        this.edizm = edizm;

    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public void setKolvo(int kolvo) {
        this.kolvo = kolvo;
    }
    public int getKolvo() {
        return kolvo;
    }

    public void setSppr(String sppr) {
        this.sppr = sppr;
    }
    public String getSppr() {
        return sppr;
    }

    public void setEdizm(String edizm) {
        this.edizm = edizm;
    }
    public String getEdizm() {
        return edizm;
    }


    @Override
    public String toString(){
        return String.format("ID:%s | F:%s | I:%s | O:%s | ecp:%s",
                this.id,this.kolvo,this.sppr,this.edizm);
    }
}
