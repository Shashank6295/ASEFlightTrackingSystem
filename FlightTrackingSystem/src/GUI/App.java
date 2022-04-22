package GUI;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import com.sun.glass.ui.Application;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import core.Aeroplane;
import core.Airport;
import core.Flight;
import core.FlightPlan;
import core.Loader;
import utils.DataExtractor;
import utils.Log;

public class App extends JFrame implements ActionListener {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel panel;
    private JTable flightTable;
    private JTable flightPlanTable;
    private JScrollPane scrollPane;
    private JTable addFlightTable;
    private JTable addFlightPlanTable;
    private int width;
    private int height;
    private Loader loader;
    private String[][] data;
    public static ImageIcon trueIcon = new ImageIcon(new ImageIcon("true.png").getImage().getScaledInstance(30, 15, Image.SCALE_DEFAULT));
    public static ImageIcon planeIcon = new ImageIcon(new ImageIcon("plane.png").getImage().getScaledInstance(30, 15, Image.SCALE_DEFAULT));

    public App() {
        super("Flights Tracking System");
        loader = new Loader();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        width = screenSize.width;
        height = screenSize.height;
        setSize(width, height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JPanel jPanel = new JPanel();
        data = loader.getFlights();

        flightTable = new JTable(data, loader.getColumnNames());

        scrollPane = new JScrollPane(flightTable);
        scrollPane.setPreferredSize(new Dimension((width * 69)/100, (height * 45)/100));
        flightTable.setFillsViewportHeight(true);

        panel = new JPanel(new GridBagLayout());
        panel.add(scrollPane);
        panel.setPreferredSize(new Dimension((width * 70)/100, height/2));
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(),
                "Flights", TitledBorder.CENTER, TitledBorder.TOP,
                new Font("times new roman", Font.PLAIN, 20)));
        jPanel.add(panel);

        updateFlightPlanTable(data[0][0], jPanel);
        JLabel distanceLabel = new JLabel("Distance (km) :");
        JLabel timeLabel = new JLabel("Time (hr):");
        JLabel fuelConsumptionLabel = new JLabel("Fuel Consumption (litre):");
        JLabel coLabel = new JLabel("CO2 (kg):");

        JTextArea distanceTextArea = new JTextArea(1,5);
        distanceTextArea.setFont(new Font("times new roman", Font.PLAIN, 18));
        distanceTextArea.setEditable(false);
        distanceTextArea.setText(loader.getDistance(loader.getFlights()[0][0]));
        JTextArea timeTextArea = new JTextArea(1,5);
        timeTextArea.setFont(new Font("times new roman", Font.PLAIN, 18));
        timeTextArea.setEditable(false);
        timeTextArea.setText(loader.getTime(loader.getFlights()[0][0]));
        JTextArea fuelTextArea = new JTextArea(1,5);
        fuelTextArea.setFont(new Font("times new roman", Font.PLAIN, 18));
        fuelTextArea.setEditable(false);
        fuelTextArea.setText(loader.getFuelConsumption(loader.getFlights()[0][0]));
        JTextArea coTextArea = new JTextArea(1,5);
        coTextArea.setFont(new Font("times new roman", Font.PLAIN, 18));
        coTextArea.setEditable(false);
        coTextArea.setText(loader.getCO2Emession(loader.getFlights()[0][0]));

        JPanel jPanel1 = new JPanel(new GridLayout(8, 2));
        jPanel1.setPreferredSize(new Dimension((width * 11)/100, (height * 45)/100));
        jPanel1.add(distanceLabel);
        jPanel1.add(distanceTextArea);
        jPanel1.add(timeLabel);
        jPanel1.add(timeTextArea);
        jPanel1.add(fuelConsumptionLabel);
        jPanel1.add(fuelTextArea);
        jPanel1.add(coLabel);
        jPanel1.add(coTextArea);
        panel = new JPanel(new GridBagLayout());
        panel.add(jPanel1);
        panel.setPreferredSize(new Dimension((width * 12)/100, height/2));
        jPanel.add(panel);

        addFlightTable(jPanel);
        
        addFlightPlanTable(jPanel);
        
        JButton addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String[] flightData = new String[addFlightTable.getModel().getColumnCount()];
				String[] flightPlanData = new String[addFlightPlanTable.getModel().getColumnCount()];
				for(int i=0; i< addFlightTable.getModel().getColumnCount(); i++) {
					flightData[i] = (String) addFlightTable.getModel().getValueAt(0, i);
					System.out.println(flightData[i]);
				}
				for(int i=0; i< addFlightPlanTable.getModel().getColumnCount(); i++)
					flightPlanData[i] = (String) addFlightPlanTable.getModel().getValueAt(0, i);
				Flight flight = new Flight();
	            flight.setId(flightData[1]);
	            Optional<Aeroplane> optionalAeroplane = DataExtractor.aeroplanes.stream()
	                    .filter(aeroplane -> aeroplane.getModel() != null)
	                    .filter(aeroplane -> aeroplane.getModel().trim().equalsIgnoreCase(flightData[2]))
	                    .findFirst();
	            if (optionalAeroplane.isPresent()) {
	                Aeroplane aeroplane = optionalAeroplane.get();
	                flight.setPlaneType(aeroplane);
	            }
	            Optional<Airport> optionalAirport = DataExtractor.airports.stream()
	                    .filter(airport -> airport.getCode() != null)
	                    .filter(airport -> airport.getCode().equalsIgnoreCase(flightData[3]))
	                    .findFirst();
	            if (optionalAirport.isPresent()){
	                Airport airport = optionalAirport.get();
	                flight.setairportDeparture(airport);
	            }

	            Optional<Airport> airportOptional = DataExtractor.airports.stream()
	                    .filter(airport -> airport.getCode() != null)
	                    .filter(airport -> airport.getCode().equalsIgnoreCase(flightData[4]))
	                    .findFirst();
	            if (airportOptional.isPresent()){
	                Airport airport = airportOptional.get();
	                flight.setDestination(airport);
	            }
	            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM:dd:yyyy HH:mm");
	            LocalDateTime localDateTime = LocalDateTime
	                    .parse(flightData[5]+" "+flightData[6], formatter)
	                    .atZone(ZoneId.of("CET")).toLocalDateTime();
	            flight.setDepartureDate(localDateTime.toLocalDate());
	            flight.setDepartureTime(localDateTime.toLocalTime());
	            List<String> airportList = new ArrayList<>();
	            for (int i = 0; i < flightPlanData.length; i++){
	                if (flightPlanData[i] != null){
	                    airportList.add(flightPlanData[i]);
	                }
	            }
	            List<Airport> airports1 = DataExtractor.airports.stream()
	                    .filter(airport -> airport.getCode() != null)
	                    .filter(airport -> airportList.contains(airport.getCode()))
	                    .collect(Collectors.toList());
	            FlightPlan flightPlan = new FlightPlan(airports1);
	            flight.setFlightPlan(flightPlan);
	            try {
					Main.flightControl.acquire();
				} catch (InterruptedException e1) {
					Log.getLog().writeMessage(e1.getMessage());					
				}
	            DataExtractor.flights.add(flight);
	            Main.flightControl.release();
	            flightTable.setModel(new DefaultTableModel(loader.getFlights(), loader.getColumnNames()));
				Log.getLog().writeMessage("Added a new Flight " + flight.getFlightId());
			}
        	
        });
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				addFlightTable.setModel(new DefaultTableModel(new String[][] {{"", "", "", "", "", "", ""}}, loader.getFlightColumns()));
		    	addFlightPlanTable.setModel(new DefaultTableModel(new String[][] {{"", "", "", "", "", "", ""}}, new String[] {"", "", "", "", "", "", ""}));

			}
        	
        });
        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Main.end.set(true);
				System.exit(0);
			}
        	
        });
        addButton.setPreferredSize(addButton.getSize());
        panel = new JPanel(new GridLayout(1, 3));
        panel.setPreferredSize(new Dimension((width * 30)/100, (height * 6)/100));
        panel.add(addButton);
        panel.add(cancelButton);
        panel.add(exitButton);
        jPanel.add(panel);
        
        add(jPanel);

        ListSelectionModel model = flightTable.getSelectionModel();

        model.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!model.getValueIsAdjusting()){
                	if(flightTable.getSelectedRow() > -1 && flightTable.getSelectedRow() < flightTable.getRowCount()) {
                        String flightCode = (String) flightTable.getValueAt(flightTable.getSelectedRow(), 0);
                    	DefaultTableModel model = new DefaultTableModel(loader.getFlightPlan(flightCode), new String[]{"", ""})
                        {
                            /**
                			 * 
                			 */
                			private static final long serialVersionUID = 1L;
                			//  Returning the Class of each column will allow different
                            //  renderers to be used based on Class
                            public Class getColumnClass(int column)
                            {
                                return getValueAt(1, column).getClass();
                            }
                        };
                        flightPlanTable.setModel(model);
                        distanceTextArea.setText(loader.getDistance(flightCode));
                        timeTextArea.setText(loader.getTime(flightCode));
                        coTextArea.setText(loader.getCO2Emession(flightCode));
                        fuelTextArea.setText(loader.getFuelConsumption(flightCode));                		
                	}
                }
            }
        });
        setVisible(true);
        Main.pause.set(true);
    }

    private void updateFlightPlanTable(String flightCode, JPanel jPanel){
    	DefaultTableModel model = new DefaultTableModel(loader.getFlightPlan(flightCode), new String[]{"", ""})
        {
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			//  Returning the Class of each column will allow different
            //  renderers to be used based on Class
            public Class getColumnClass(int column)
            {
                return getValueAt(1, column).getClass();
            }
        };
        flightPlanTable = new JTable(model);
        flightPlanTable.repaint();
        scrollPane = new JScrollPane(flightPlanTable);
        scrollPane.setPreferredSize(new Dimension((width * 14)/100, (height * 45)/100));
        flightPlanTable.setFillsViewportHeight(true);
        panel = new JPanel(new GridBagLayout());
        panel.add(scrollPane);
        panel.setPreferredSize(new Dimension((width * 15)/100, height/2));
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(),
                "Flight Plan", TitledBorder.CENTER, TitledBorder.TOP,
                new Font("times new roman", Font.PLAIN, 20)));
        jPanel.add(panel);
    }
    
    private void addFlightTable(JPanel jPanel) {
    	addFlightTable = new JTable(new String[][] {{"", "", "", "", "", "", ""}}, loader.getFlightColumns());
    	addFlightTable.repaint();
        scrollPane = new JScrollPane(addFlightTable);
        scrollPane.setPreferredSize(new Dimension((width * 45)/100, (height * 6)/100));
        addFlightTable.setFillsViewportHeight(true);
        panel = new JPanel(new GridBagLayout());
        panel.add(scrollPane);
        panel.setPreferredSize(new Dimension((width * 70)/100, height * 12/100));
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(),
                "Add Flight", TitledBorder.CENTER, TitledBorder.TOP,
                new Font("times new roman", Font.PLAIN, 20)));
        jPanel.add(panel);
    }
    
    private void addFlightPlanTable(JPanel jPanel) {
    	addFlightPlanTable = new JTable(new Object[][] {{"", "", "", "", "", "", ""}}, new String[] {"", "", "", "", "", "", ""});
    	addFlightPlanTable.repaint();
        scrollPane = new JScrollPane(addFlightPlanTable);
        scrollPane.setPreferredSize(new Dimension((width * 45)/100, (height * 4)/100));
        addFlightPlanTable.setFillsViewportHeight(true);
        panel = new JPanel(new GridBagLayout());
        panel.add(scrollPane);
        panel.setPreferredSize(new Dimension((width * 70)/100, height * 10/100));
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(),
                "Flight Plan", TitledBorder.CENTER, TitledBorder.TOP,
                new Font("times new roman", Font.PLAIN, 20)));
        jPanel.add(panel);
    }
    
    public  void updateInterface() {
        flightTable.setModel(new DefaultTableModel(loader.getFlights(), loader.getColumnNames()));
		Log.getLog().writeMessage("Updating GUI on Control Tower call");
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
