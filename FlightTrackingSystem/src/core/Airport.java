package core;

import javax.swing.ImageIcon;

public class Airport {

    private String airportName;
    private String airportCode;
    private ControlTower controlTower;
    private ImageIcon image;
    
    public Airport() {
    	image = new ImageIcon();
    }
	public ImageIcon getImage() {
		return image;
	}

	public void setImage(ImageIcon image) {
		this.image = image;
	}

	public String getName() {
        return airportName;
    }

    public void setName(String name) {
        this.airportName = name;
    }

    public String getCode() {
        return airportCode;
    }

    public void setCode(String code) {
        this.airportCode = code;
    }

    public ControlTower getControlTower() {
        return controlTower;
    }

    public void setControlTower(ControlTower controlTower) {
        this.controlTower = controlTower;
    }
}
