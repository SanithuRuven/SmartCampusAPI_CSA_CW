//Name: Sanithu Ruven Gunasena
//UOW ID: W212301
//IIT ID: 20241102

package uk.ac.westminster.resources;

import javax.ws.rs.PathParam;import uk.ac.westminster.models.Sensor;
import uk.ac.westminster.models.SensorReading;
import uk.ac.westminster.services.DataStore;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import uk.ac.westminster.exceptions.SensorUnavailableException;

//Task 4.1
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorReadingResource {
    
    private String sensorId;

    // Constructor
    public SensorReadingResource(String sensorId) {
        this.sensorId = sensorId;
    }

    //Task 4.2
    //GET /api/v1/sensors/{sensorId}/readings
    //Fetching the historical log of readings for this specific sensor
    @GET
    public List<SensorReading> getReadingHistory() {
        
        //Retrieving the list of readings from the DataStore for this sensor
        List<SensorReading> history = DataStore.readings.get(sensorId);
        
        //If no readings exist as of now it will return an empty list instead of null
        if (history == null) {
            return new ArrayList<SensorReading>();
        }
        return history;
    }

    //POST /api/v1/sensors/{sensorId}/readings
    //Appends a new reading and updates the parent sensors current value
    @POST
    public Response addReading(SensorReading newReading) {
        
        //Ensuring that the parent sensor exists in our DataStore
        Sensor parentSensor = DataStore.sensors.get(sensorId);
        
        if (parentSensor == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        
        //Maintainance status
        if ("MAINTENANCE".equalsIgnoreCase(parentSensor.getStatus())) {
            throw new SensorUnavailableException("Reading Refused: Sensor " 
                    + this.sensorId + " is currently in MAINTENANCE mode.");
        }
        

        //Updating the currentValue on the parent Sensor object
        parentSensor.setCurrentValue(newReading.getValue());

        //Appending the reading to the historical log in the DataStore
        
        //If this is the first reading of the sensonot the initialize a new list
        if (!DataStore.readings.containsKey(sensorId)) {
            DataStore.readings.put(sensorId, new ArrayList<SensorReading>());
        }
        DataStore.readings.get(sensorId).add(newReading);

        //Returning 201 Created status [cite: 39]
        return Response.status(Response.Status.CREATED).entity(newReading).build();
    }
    
}