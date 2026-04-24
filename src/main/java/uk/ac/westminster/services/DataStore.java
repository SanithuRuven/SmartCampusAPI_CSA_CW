//Name: Sanithu Ruven Gunasena
//UOW ID: W212301
//IIT ID: 20241102

package uk.ac.westminster.services;

import uk.ac.westminster.models.Room;
import uk.ac.westminster.models.Sensor;
import uk.ac.westminster.models.SensorReading;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

//In memory database
public class DataStore {
    
    //Map of RoomID
    public static Map<String, Room> rooms = new ConcurrentHashMap<>();
    
    //Map of SensorID
    public static Map<String, Sensor> sensors = new ConcurrentHashMap<>();
    
    //Map of SensorID
    public static Map<String, List<SensorReading>> readings = new ConcurrentHashMap<>();
}