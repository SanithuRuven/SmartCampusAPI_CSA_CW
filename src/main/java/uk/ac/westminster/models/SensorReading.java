//Name: Sanithu Ruven Gunasena
//UOW ID: W212301
//IIT ID: 20241102

package uk.ac.westminster.models;

public class SensorReading {
    
    //Variables
    private String id; // Unique reading event ID (UUID recommended)
    private long timestamp; // Epoch time (ms) when the reading was captured
    private double value;  // The actual metric value recorded by the hardware
    
    //Default Constructor
    public SensorReading() {
    }
    
    //Overload Constructor
    public SensorReading(String id, long timestamp, double value) {
        this.id = id;
        this.timestamp = timestamp;
        this.value = value;
    }
    
    //Getters
    public String getId() {
        return id;
    }
    
    public long getTimestamp() {
        return timestamp;
    }
    
    public double getValue() {
        return value;
    }
    
    //Setters
    public void setId(String id) {
        this.id = id;
    }
    
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    
    public void setValue(double value) {
        this.value = value;
    }
    
}
