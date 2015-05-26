package com.tecpro.buseslep;

/**
 * Created by agustin on 26/05/15.
 */
public class Search {
    private String departure, arrival;
    private float departureDate, arrivalDate;

    public Search(String dep, String arr, float depDate , float arrDate ){
        this.departure = dep;
        this.arrival = arr;
        this.departureDate = depDate;
        this.arrivalDate = arrDate;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getArrival() {
        return arrival;
    }

    public void setArrival(String arrival) {
        this.arrival = arrival;
    }

    public float getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(float departureDate) {
        this.departureDate = departureDate;
    }

    public float getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(float arrivalDate) {
        this.arrivalDate = arrivalDate;
    }
}
