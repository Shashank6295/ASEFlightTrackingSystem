package core;

import exception.DataNotFoundException;
import utils.Log;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import GUI.App;
import GUI.Main;

public class Flight extends Thread{
    private String id;
    private Aeroplane planeType;
    private Airport airportDeparture;
    private Airport airportDestination;
    private LocalDate departureDate;
    private LocalTime departureTime;
    private FlightPlan flightPlan;
    private Airline airline;
    public Status status;
    public Airport currentAirport, nextAirport;
    
    public Flight() {
    	status = Status.None;
    }
    public FlightPlan getFlightPlan() {
        return flightPlan;
    }

    public void setFlightPlan(FlightPlan flightPlan) {
        this.flightPlan = flightPlan;
    }

    public Airline getAirline() {
        return airline;
    }

    public void setAirline(Airline airline) {
        this.airline = airline;
    }

    public String getFlightId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Aeroplane getPlaneType() {
        return planeType;
    }

    public void setPlaneType(Aeroplane planeType) {
        this.planeType = planeType;
    }

    public Airport getDeparture() {
        return airportDeparture;
    }

    public void setairportDeparture(Airport airportDeparture) {
        this.airportDeparture = airportDeparture;
    	currentAirport = airportDeparture;
    	nextAirport = airportDeparture;
    }

    public Airport getairportDestination() {
        return airportDestination;
    }

    public void setDestination(Airport airportDestination) {
        this.airportDestination = airportDestination;
    }

    public LocalDate getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(LocalDate departureDate) {
        this.departureDate = departureDate;
    }

    public LocalTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalTime departureTime) {
        this.departureTime = departureTime;
    }

    public Double calculateDistance() throws DataNotFoundException {
        double distance = 0;
        ControlTower controlTower = this.airportDeparture.getControlTower();
        if (controlTower == null) {
            throw new DataNotFoundException("Control tower not found.");
        }
        GPSCoordinate gpsCoord = controlTower.getGpsCoordinate();
        List<ControlTower> controlTowers = this.getFlightPlan()
                .getAirports()
                .stream()
                .map(Airport::getControlTower)
                .collect(Collectors.toList());
        if (controlTowers.isEmpty()) {
            throw new DataNotFoundException("Control towers is empty.");
        }
        double latInRadian = Math.toRadians(gpsCoord.getLatInDegree());
//        System.out.println(latInRadian);
        Double longInRadian = Math.toRadians(gpsCoord.getLngInDegree());
//        System.out.println(longInRadian);

        for (ControlTower cntrlTower : controlTowers) {
            GPSCoordinate gpsCoordinate = cntrlTower.getGpsCoordinate();
            if (gpsCoordinate == null) {
                throw new DataNotFoundException("GPS coordinates not found.");
            }
            double otherLatInRadian = Math.toRadians(gpsCoordinate.getLatInDegree());
            Double otherLngInRadian = Math.toRadians(gpsCoordinate.getLngInDegree());
            double deltaLongitude = otherLngInRadian - longInRadian;
            Double deltaLatitude = otherLatInRadian - latInRadian;
            double trig = Math.pow(Math.sin(deltaLatitude / 2), 2.0) + Math.cos(latInRadian)
                    * Math.cos(otherLatInRadian) + Math.pow(Math.sin(deltaLongitude / 2), 2.0);


            double sqrt = Math.sqrt(trig);

            if(sqrt >= 1) {
                sqrt -=1;
            }

            distance += 2 * 6371.00 * Math.asin(sqrt);

            latInRadian = otherLatInRadian;
            longInRadian = otherLngInRadian;
        }
        return distance;
    }

    public Double timeTaken() throws DataNotFoundException {
        double timeTaken = 0.0;
        Aeroplane aeroplane = this.getPlaneType();
        Double speed = aeroplane.getSpeed();
        FlightPlan flightPlan = this.getFlightPlan();
        ControlTower departureControlTower = this.airportDeparture.getControlTower();
        if (departureControlTower == null){
            throw new DataNotFoundException("Departure airport control tower not found.");
        }
        List<ControlTower> controlTowers = flightPlan.getAirports()
                .stream()
                .map(Airport::getControlTower)
                .collect(Collectors.toList());
        if (controlTowers.isEmpty()) {
            throw new DataNotFoundException("Control towers not found.");
        }
        for (ControlTower controlTower: controlTowers) {
            Double distanceBetweenControlTower = departureControlTower
                    .distanceBetween(controlTower);
            timeTaken += distanceBetweenControlTower / speed;
            departureControlTower = controlTower;
        }
        return timeTaken;
    }

    public Double fuelConsumption() throws DataNotFoundException {
        Aeroplane aeroplane = this.getPlaneType();
        Double fuelConsumption = aeroplane.getFuelConsumption();
        if (fuelConsumption == null){
            throw new DataNotFoundException("Fuel consumption for the selected plane is null.");
        }
        Double distance = this.calculateDistance();
        return distance * fuelConsumption / 100;
    }

    public Double CO2_emission() throws DataNotFoundException{
        return this.fuelConsumption() * 4.98;//Emission factor
    }
    

	@Override
	public void run() {
		this.status = Status.OnFlight;
		int index = 0;
		currentAirport = getFlightPlan().getAirports().get(index);
		index++;
		nextAirport = getFlightPlan().getAirports().get(index);
		index++;
		double distance = 0, currentPosition = 0;
		while(this.status != Status.Arrival) {
			try {
				this.status = Status.OnFlight;
				Log.getLog().writeMessage("Flight " + this.getFlightId() + " is OnFlight");
				currentAirport.setImage(App.planeIcon);
				Thread.sleep(10000);
				distance = currentAirport.getControlTower().distanceBetween(nextAirport.getControlTower());
				while((int) currentPosition != (int) distance) {
					currentPosition++;
				}
				this.status = Status.Landed;
				Log.getLog().writeMessage("Flight " + this.getFlightId() + " is Landed to a airport");
				currentAirport.setImage(App.trueIcon);
				if(getFlightPlan().getAirports().size() == index) {
					this.status = Status.Arrival;					
					Log.getLog().writeMessage("Flight " + this.getFlightId() + " is arrived at the destination");
					nextAirport.setImage(App.trueIcon);
					continue;
				}
				currentAirport = nextAirport;
				nextAirport = getFlightPlan().getAirports().get(index);
				Thread.sleep(10000);
				index++;
			} catch (DataNotFoundException e) {
				Log.getLog().writeMessage(e.getMessage());
			} catch (InterruptedException e) {
				Log.getLog().writeMessage(e.getMessage());
			}
		}
	}
}
