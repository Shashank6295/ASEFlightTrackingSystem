package utils;

import core.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DataExtractor {

    public static List<Airport> airports;
    public static List<Flight> flights;
    public static List<Aeroplane> aeroplanes;
    public static List<Airline> airlines;
    
    public DataExtractor() throws IOException {
    	airports = this.getAirports();
    	flights = this.getFlights();
    	aeroplanes = this.getAeroplane();
    	airlines = this.getAirlines();
    }

    public List<Flight> getFlights() throws IOException {
        List<Flight> flights = new ArrayList<>();
        String str;
        FileReader fileReader = new FileReader("FlightsFile.txt");
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        while ((str = bufferedReader.readLine()) != null) {
            String[] line = str.split("; ");
            Flight flight = new Flight();
            flight.setId(line[0]);
            List<Aeroplane> aeroplanes = getAeroplane();
            Optional<Aeroplane> optionalAeroplane = aeroplanes.stream()
                    .filter(aeroplane -> aeroplane.getModel() != null)
                    .filter(aeroplane -> aeroplane.getModel().trim().equalsIgnoreCase(line[1]))
                    .findFirst();
            if (optionalAeroplane.isPresent()) {
                Aeroplane aeroplane = optionalAeroplane.get();
                flight.setPlaneType(aeroplane);
            }
            List<Airport> airports = getAirports();
            Optional<Airport> optionalAirport = airports.stream()
                    .filter(airport -> airport.getCode() != null)
                    .filter(airport -> airport.getCode().equalsIgnoreCase(line[2]))
                    .findFirst();
            if (optionalAirport.isPresent()){
                Airport airport = optionalAirport.get();
                flight.setairportDeparture(airport);
            }

            Optional<Airport> airportOptional = airports.stream()
                    .filter(airport -> airport.getCode() != null)
                    .filter(airport -> airport.getCode().equalsIgnoreCase(line[3]))
                    .findFirst();
            if (airportOptional.isPresent()){
                Airport airport = airportOptional.get();
                flight.setDestination(airport);
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM:dd:yyyy HH:mm");
            LocalDateTime localDateTime = LocalDateTime
                    .parse(line[4]+" "+line[5], formatter)
                    .atZone(ZoneId.of("CET")).toLocalDateTime();
            flight.setDepartureDate(localDateTime.toLocalDate());
            flight.setDepartureTime(localDateTime.toLocalTime());
            List<String> airportList = new ArrayList<>();
            for (int i = 6; i < line.length; i++){
                if (line[i] != null){
                    airportList.add(line[i]);
                }
            }
            List<Airport> airports1 = airports.stream()
                    .filter(airport -> airport.getCode() != null)
                    .filter(airport -> airportList.contains(airport.getCode()))
                    .collect(Collectors.toList());
            FlightPlan flightPlan = new FlightPlan(airports1);
            flight.setFlightPlan(flightPlan);
            flights.add(flight);
        }
        bufferedReader.close();
        return flights;
    }

    public List<Aeroplane> getAeroplane() throws IOException {
        List<Aeroplane> aeroplanes = new ArrayList<>();
        String str;
        FileReader fileReader = new FileReader("PlanesFile.txt");
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        while ((str = bufferedReader.readLine()) != null) {
            String[] line = str.split("; ");
            Aeroplane aeroplane = new Aeroplane();
            aeroplane.setModel(line[0]);
            aeroplane.setManufacturer(line[1]);
            aeroplane.setSpeed(Double.parseDouble(line[2]));
            aeroplane.setFuelConsumption(Double.parseDouble(line[3]));
            aeroplanes.add(aeroplane);
        }
        bufferedReader.close();
        return aeroplanes;
    }

    public List<Airport> getAirports() throws IOException {
        List<Airport> airports = new ArrayList<>();
        String str;
        FileReader fileReader = new FileReader("AirportsFile.txt");
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        while ((str = bufferedReader.readLine()) != null) {
            String[] line = str.split("; ");
            Airport airport = new Airport();
            airport.setCode(line[0]);
            airport.setName(line[1]);
            airport.setControlTower(new ControlTower(new GPSCoordinate(line[2], line[3])));
            airports.add(airport);
        }
        bufferedReader.close();
        return airports;
       
    }

    public List<Airline> getAirlines() throws IOException {
        List<Airline> airlines = new ArrayList<>();
        String str;
        FileReader fileReader = new FileReader("AirlinesFile.txt");
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        while ((str = bufferedReader.readLine()) != null) {
            String[] line = str.split("; ");
            airlines.add(new Airline(line[0], line[1]));
        }
        bufferedReader.close();
        return airlines;
    }
}
