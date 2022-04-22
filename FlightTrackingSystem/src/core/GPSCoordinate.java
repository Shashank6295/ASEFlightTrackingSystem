package core;

import utils.Utils;

import java.util.regex.Matcher;

public class GPSCoordinate {

    private String latitude;
    private String longitude;

    public GPSCoordinate(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public GPSCoordinate() {
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public Double getLngInDegree(){
        Matcher matcher = Utils.DMSLNGPATTERN.matcher(this.longitude.trim());
        if (matcher.matches()){
            double longitude = convertToDouble(matcher);
            if ((Math.abs(longitude) > 180)) {
                throw new NumberFormatException("Invalid longitude");
            }
            return longitude;
        }else {
            throw new NumberFormatException("Malformed DMS coordiniates");
        }
    }

    public Double getLatInDegree(){
    	//System.out.println(this.latitude.trim());
        Matcher matcher = Utils.DMSLATPATTERN.matcher(this.latitude.trim());
       // System.out.println(matcher.matches());
        if (matcher.matches()){

            double latitude = convertToDouble(matcher);
            if ((Math.abs(latitude) > 180)) {
                throw new NumberFormatException("Invalid latitude");
            }
            return latitude;
        }else {
            throw new NumberFormatException("Malformed DMS coordiniates");
        }
    }

    private Double convertToDouble(Matcher matcher){
        int sign = "".equals(matcher.group(1)) ? 1 : -1;
        double degrees = Double.parseDouble(matcher.group(2));
        double minutes = Double.parseDouble(matcher.group(3));
        double seconds = Double.parseDouble(matcher.group(4));
        int direction = "NE".contains(matcher.group(5)) ? 1 : -1;

        return sign * direction * (degrees + minutes / 60 + seconds / 3600 );
    }
}
