package core;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;


import exception.DataNotFoundException;
import utils.DataExtractor;

public class Loader {
    private String[][] data;
    public String[][] getFlights() {
        List<Flight> flights = DataExtractor.flights;
		Flight[] flights1 = new Flight[flights.size()];
		data = new String[flights.size()][];
		flights.toArray(flights1);
		for (int i = 0; i < flights1.length; i++) {
		    String[] flightData = new String[]{
		            flights1[i].getFlightId(),
		            flights1[i].getPlaneType().getModel(),
		            flights1[i].getDeparture().getCode(),
		            flights1[i].getairportDestination().getCode(),
		            flights1[i].getDepartureDate().toString(),
		            flights1[i].getDepartureTime().toString(),
		            flights1[i].nextAirport.getControlTower().getGpsCoordinate().getLatitude(),
		            flights1[i].nextAirport.getControlTower().getGpsCoordinate().getLongitude(),
		            flights1[i].status.name()
		    };
		    data[i] = flightData;
		}
        return data;
    }

    public String[] getColumnNames() {
        return new String[]{
                "Flight",
                "Plane",
                "Departure",
                "Destination",
                "Date",
                "Time",
                "Latitude",
                "Longitude",
                "Status"
        };
    }
    
    public String[] getFlightColumns() {
        return new String[]{
        		"Airline",
                "Number",
                "Plane",
                "Departure",
                "Destination",
                "Date",
                "Time"
        };
    }

    public Object[][] getFlightPlan(String flightCode) {
        Flight flight = getFlight(flightCode);
        List<Airport> airports = flight.getFlightPlan().getAirports();
        Object[][] data = new Object[airports.size()][];
        Airport[] ports = new Airport[airports.size()];
        airports.toArray(ports);
        for (int i = 0; i < ports.length; i++) {
            Object[] airportData = new Object[]{
                    ports[i].getCode(), ports[i].getImage()
            };
            data[i] = airportData;
        }
//        System.out.println(Arrays.deepToString(data));
        return data;
    }

    public Flight getFlight(String code){
        Optional<Flight> optionalFlight;
        Flight flight = null;
        try {
            optionalFlight = DataExtractor.flights
                    .stream()
                    .filter(flight1 -> flight1.getFlightId().equalsIgnoreCase(code))
                    .findFirst();
            if (!optionalFlight.isPresent())
                throw new DataNotFoundException("Flight not found.");
            flight = optionalFlight.get();
        } catch (DataNotFoundException e) {
            e.printStackTrace();
        }
        return flight;
    }

    public String getDistance(String flightCode) {
        String num = null;
        try {
            Flight flight = getFlight(flightCode);
            DecimalFormat df = new DecimalFormat("###.##");
            num = df.format(flight.calculateDistance());
        } catch (DataNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return num;
    }

    public String getTime(String flightCode) {
        String num = null;
        try {
            Flight flight = getFlight(flightCode);
            DecimalFormat df = new DecimalFormat("###.##");
            num = df.format(flight.timeTaken());
        } catch (DataNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return num;
    }

    public String getCO2Emession(String flightCode){
        String num = null;
        try {
            Flight flight = getFlight(flightCode);
            DecimalFormat df = new DecimalFormat("###.##");
            num = df.format(flight.CO2_emission());
        } catch (DataNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return num;
    }

    public String getFuelConsumption(String flightCode){
        String num = null;
        try {
            Flight flight = getFlight(flightCode);
            DecimalFormat df = new DecimalFormat("###.##");
            num = df.format(flight.fuelConsumption());
        } catch (DataNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return num;
    }
    
    
    
}
