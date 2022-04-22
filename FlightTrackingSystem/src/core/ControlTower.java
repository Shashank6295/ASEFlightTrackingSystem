package core;

import GUI.Main;
import exception.DataNotFoundException;
import utils.DataExtractor;
import utils.Log;

public class ControlTower extends Thread {
    private GPSCoordinate gpsCoordinate;

    public ControlTower() {
    }

    public ControlTower(GPSCoordinate gpsCoordinate) {
        this.gpsCoordinate = gpsCoordinate;
    }

    public GPSCoordinate getGpsCoordinate() {
        return gpsCoordinate;
    }

    public void setGpsCoordinate(GPSCoordinate gpsCoordinate) {
        this.gpsCoordinate = gpsCoordinate;
    }

    public double distanceBetween(ControlTower controlTower) throws DataNotFoundException {
        GPSCoordinate gpsCoordinate = this.gpsCoordinate;
        double latInRadian = Math.toRadians(gpsCoordinate.getLatInDegree());
        double lngInRadian = Math.toRadians(gpsCoordinate.getLngInDegree());
        GPSCoordinate gpsCoordinate1 = controlTower.gpsCoordinate;
        if (gpsCoordinate1 == null){
            throw new DataNotFoundException("GPS coordinates not found.");
        }
        double otherLatInRadian = Math.toRadians(gpsCoordinate1.getLatInDegree());
        double otherLngInRadian = Math.toRadians(gpsCoordinate1.getLngInDegree());
        double deltaLng = otherLngInRadian - lngInRadian;
        double deltaLat = otherLatInRadian - latInRadian;
        double trig = Math.pow(Math.sin(deltaLat / 2), 2.0) + Math.cos(latInRadian)
                * Math.cos(otherLatInRadian) + Math.pow(Math.sin(deltaLng / 2), 2.0);

        double sqrt = Math.sqrt(trig);

        if(sqrt >= 1) {
            sqrt -=1;
        }

        return  2 * 6371.00 * Math.asin(sqrt);
    }
    
	@Override
	public void run() {
		while(!Main.pause.get()) {
		}
		while(!Main.end.get()) {
			try {
				Main.control.acquire();
				Log.getLog().writeMessage("Monitoring Flights and Updating GUI");
				Main.app.updateInterface();
				Main.control.release();
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				Log.getLog().writeMessage(e.getMessage());
			}
		}
	}

}
