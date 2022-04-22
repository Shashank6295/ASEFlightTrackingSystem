package GUI;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.SwingUtilities;

import core.Airport;
import core.Flight;
import core.Status;
import utils.DataExtractor;
import utils.Log;


public class Main {
	public static App app;
    public static AtomicBoolean end;
    public static AtomicBoolean pause;
    public static Semaphore control;
    public static Semaphore flightControl;
    
    public static void main(String[] args) throws IOException {
        new DataExtractor();
        end = new AtomicBoolean(false);
        pause = new AtomicBoolean(false);
        control = new Semaphore(1, true);
        flightControl = new Semaphore(1, true);
        new Thread(new Runnable() {
			@Override
			public void run() {
				while(!end.get()) {
					try {
						flightControl.acquire();
						Flight flight = checkFlight(DataExtractor.flights);
						if(flight != null) {
							if(!flight.isAlive())
								if(flight.status == Status.None)
									flight.start();
						}
						flightControl.release();
					} catch (InterruptedException e) {
						Log.getLog().writeMessage(e.getMessage());
					}
				}
			}    		
    	}).start();
    	
    	SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
		        app = new App();
		        app.setVisible(true);
			}
		});
    	
        for(Airport airport : DataExtractor.airports) {
        	airport.getControlTower().start();
        }

    	
    }
    public static Flight checkFlight(List<Flight> flights) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM:dd:yyyy HH:mm");
        for(Flight flight : flights) {
        	String flightDate = flight.getDepartureDate().atTime(flight.getDepartureTime()).format(formatter);
        	String now = LocalDateTime.now().format(formatter);
//    		System.out.println(flightDate + " " + now);
        	if(flightDate.equals(now)) {
//        		System.out.println("Flight Started");
        		return flight;        		
        	}
        }    	
		return null;
    }
}
