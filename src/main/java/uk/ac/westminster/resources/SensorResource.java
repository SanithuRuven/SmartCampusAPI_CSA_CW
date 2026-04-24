//Name: Sanithu Ruven Gunasena
//UOW ID: W212301
//IIT ID: 20241102

package uk.ac.westminster.resources;

import java.util.ArrayList;
import uk.ac.westminster.models.Sensor;
import uk.ac.westminster.models.Room;
import uk.ac.westminster.services.DataStore;
import uk.ac.westminster.exceptions.LinkedResourceNotFoundException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

//Task 3.1
@Path("/sensors") //The /api/v1/sensors path 
public class SensorResource {

    //POST /api/v1/sensors
    //Registers a new sensor and verifies whether the perticular Room ID exists 
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerSensor(Sensor sensor) {
        //Verifying that the roomId specified in the body actually exists
        Room parentRoom = DataStore.rooms.get(sensor.getRoomId());
        
        if (parentRoom == null) {
            //If the room dont exist then blocking the request 
            throw new LinkedResourceNotFoundException("Validation Failed: Room ID " 
                    + sensor.getRoomId() + " does not exist.");
        }

        //Saving the sensor to our data store if the room exists
        DataStore.sensors.put(sensor.getId(), sensor);
        
        //Updating the Room object to include the new sensor ID
        parentRoom.addSensorId(sensor.getId());

        //Returning 201 Created status 
        return Response.status(Response.Status.CREATED).entity(sensor).build();
    }
    
    //Task 3.2
    //GET /api/v1/sensors
    //Supporting optional filtering by sensor type using a query parameter
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Sensor> getSensors(@QueryParam("type") String type) {
        //Getting all sensors from our DataStore and storing them as a list
        List<Sensor> allSensors = new ArrayList<>(DataStore.sensors.values());

        //If no type is provided then returning everything immediately
        if (type == null || type.isEmpty()) {
            return allSensors;
        }

        //Creating a new list to hold the filtered results
        List<Sensor> filteredSensors = new ArrayList<>();
        
        for (Sensor s : allSensors) {
            //Checking if the sensor type matches the query parameter
            if (s.getType().equalsIgnoreCase(type)) {
                filteredSensors.add(s);
            }
        }

        //Returning the filtered list
        return filteredSensors;
    }
    
    //Task 4.1
    //Sub resource locator for Sensor Readings which matches the path /api/v1/sensors/{sensorId}/readings
    @Path("/{sensorId}/readings")
    public SensorReadingResource getSensorReadingResource(@PathParam("sensorId") String sensorId) {
        return new SensorReadingResource(sensorId);
    }
}