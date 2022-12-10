package com.example.entities;

public class Receptpreparat {
    public int id;
    public String sppr;
    public String edizm;
    public int vipicano;
    public int doza;
    public int kolvodoz;
    public int kolvokurs;
    public String kurs;
    public int idrec;
    public int idpre;
    public String prep_name;

    public Receptpreparat(int id, String sppr, String edizm, int vipicano, int doza, int kolvodoz, int kolvokurs, String kurs, String prep_name
//            , int idrec, int idpre
    ){
        this.id =id;
        this.sppr = sppr;
        this.edizm = edizm;
        this.vipicano = vipicano;
        this.doza = doza;
        this.kolvodoz = kolvodoz;
        this.kolvokurs = kolvokurs;
        this.kurs = kurs;
        this.prep_name = prep_name;
//        this.idrec = idrec;
//        this.idpre = idpre;

    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
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

    public void setVipicano(int vipicano) {
        this.vipicano = vipicano;
    }
    public int getVipicano() {
        return vipicano;
    }

    public void setDoza(int doza) {
        this.doza = doza;
    }
    public int getDoza() {
        return doza;
    }

    public void setKolvodoz(int kolvodoz) {
        this.kolvodoz = kolvodoz;
    }
    public int getKolvodoz() {
        return kolvodoz;
    }

    public void setKolvokurs(int kolvokurs) {
        this.kolvokurs = kolvokurs;
    }
    public int getKolvokurs() {
        return kolvokurs;
    }

    public void setKurs(String kurs) {
        this.kurs = kurs;
    }
    public String getKurs() {
        return kurs;
    }

    public void setPrep_name(String kurs) {
        this.prep_name = prep_name;
    }
    public String getPrep_name() {
        return prep_name;
    }

//    public void setIdrec(int idrec) {
//        this.idrec = idrec;
//    }
//    public int getIdrec() {
//        return idrec;
//    }
//
//    public void setIdpre(int idpre) {
//        this.idpre = idpre;
//    }
//    public int getIdpre() {
//        return idpre;
//    }

    @Override
    public String toString(){
        return String.format("ID:%s | F:%s | I:%s | O:%s | ecp:%s",
                this.id,this.vipicano,this.doza,this.kolvodoz,this.kolvokurs,this.kurs,this.idrec,this.idpre);
    }
}
