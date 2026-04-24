//Name: Sanithu Ruven Gunasena
//UOW ID: W212301
//IIT ID: 20241102

package uk.ac.westminster.models;

//Imports
import java.util.ArrayList;
import java.util.List;

public class Room {
    
    //Variables
    private String id; // Unique identifier, e.g., "LIB-301"
    private String name; // Human-readable name, e.g., "Library Quiet Study"
    private int capacity;  // Maximum occupancy for safety regulations
    private List<String> sensorIds = new ArrayList<>(); // Collection of IDs of sensors deployed in this room
    
    //Default Constructor
    public Room() {
    }
    
    //Overload Constructor
    public Room(String id, String name, int capacity) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
    }
    
    //Getters
    public String getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public int getCapacity() {
        return capacity;
    }
    
    //Setters
    public void setId(String id) {
        this.id = id;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
    
    // Helper method to add a single sensor ID
    public void addSensorId(String sensorId) {
        if (!this.sensorIds.contains(sensorId)) {
            this.sensorIds.add(sensorId);
        }
    }
    
}
