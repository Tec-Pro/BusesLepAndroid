package com.tecpro.buseslep.search_scheludes.schedule;

/**
 * Created by agustin on 26/05/15.
 */
public class Schedule {
    private String departDate, departTIme, arrivDate, arrivTime, status,code,idEnterprise, idDestino;

    public Schedule(String departDate, String departTIme, String arrivDate, String status, String arrivTime,String code, String idEnterprise, String id_destino) {
        this.departDate = departDate;
        this.departTIme = departTIme;
        this.arrivDate = arrivDate;
        this.status = status;
        this.arrivTime = arrivTime;
        this.code= code;
        this.idEnterprise = idEnterprise;
        this.idDestino = id_destino;
    }

    public String getDepartDate() {
        return departDate;
    }

    public String getDepartTIme() {
        return departTIme;
    }

    public String getArrivDate() {
        return arrivDate;
    }

    public String getArrivTime() {
        return arrivTime;
    }

    public String getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }

    public void setDepartDate(String departDate) {
        this.departDate = departDate;
    }

    public void setDepartTIme(String departTIme) {
        this.departTIme = departTIme;
    }

    public void setArrivDate(String arrivDate) {
        this.arrivDate = arrivDate;
    }

    public void setArrivTime(String arrivTime) {
        this.arrivTime = arrivTime;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getIdEnterprise() {
        return idEnterprise;
    }

    public void setIdEnterprise(String idEnterprise) {
        this.idEnterprise = idEnterprise;
    }

    public String getIdDestino() {
        return idDestino;
    }
}
