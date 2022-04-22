package core;

public class Airline {

    private String airlineCode;
    private String airlineName;

    public Airline() {
    }

    public Airline(String airlineCode, String airlineName) {
        this.airlineCode = airlineCode;
        this.airlineName = airlineName;
    }

    public String getairlineName() {
        return airlineName;
    }
public void setairlineName(String airlineName) {
        this.airlineName = airlineName;
    }

    public String getairlineCode() {
        return airlineCode;
    }

    public void setairlineCode(String airlineCode) {
        this.airlineCode = airlineCode;
    }
}
