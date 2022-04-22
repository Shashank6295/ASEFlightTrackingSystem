package core;

import java.util.List;

public class FlightPlan {

    private List<Airport> airports;

    public FlightPlan() {
    }

    public FlightPlan(List<Airport> airports) {
        this.airports = airports;
    }

    public List<Airport> getAirports() {
        return airports;
    }

    public void setAirports(List<Airport> airports) {
        this.airports = airports;
    }
}
