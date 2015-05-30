package com.tecpro.buseslep.search_scheludes.schedule;

/**
 * Created by agustin on 26/05/15.
 */
public class Schedule {
    private String departDate, departTIme, arrivDate, arrivTime, status;

    public Schedule(String departDate, String departTIme, String arrivDate, String status, String arrivTime) {
        this.departDate = departDate;
        this.departTIme = departTIme;
        this.arrivDate = arrivDate;
        this.status = status;
        this.arrivTime = arrivTime;
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
}
