package utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class Log {
	private static Log obj=new Log();
	String file = "Log.log";
	File logFile;
    FileWriter logWriter;
	private Log(){
		try {
		      logFile = new File(file);
		      if (logFile.createNewFile()) {
		        System.out.println("File created: " + logFile.getName());
		      } else {
		        System.out.println("File already exists.");
		      }
		      logWriter = new FileWriter(file);
	    } catch (IOException e) {
	      System.out.println("An error occurred.");
	    }
	}  
	   
	public static Log getLog(){  
		return obj;  
	}  
	
    public void writeMessage(String message) {
    	System.out.println(LocalDateTime.now() + ": " + message);
    	try {
			logWriter.write(LocalDateTime.now() + ": " + message + "\n");
		} catch (IOException e) {
	    	System.out.println(e.getMessage());			
		}

    }
}
