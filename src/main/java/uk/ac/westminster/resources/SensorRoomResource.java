//Name: Sanithu Ruven Gunasena
//UOW ID: W212301
//IIT ID: 20241102

package uk.ac.westminster.resources;

import uk.ac.westminster.models.Room;
import uk.ac.westminster.services.DataStore;
import uk.ac.westminster.exceptions.RoomNotEmptyException;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import uk.ac.westminster.models.Sensor;

//Task 2.1
@Path("/rooms") //The /api/v1/rooms path
public class SensorRoomResource {
    
    //GET /api/v1/rooms
    //Provide a comprehensive list of all rooms
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Room> getAllRooms() {        
        //Returns the values from our in-memory HashMap
        return new ArrayList<>(DataStore.rooms.values());
    }
    
    //POST /api/v1/rooms
    //Enables the creation of new rooms with feedback
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createRoom(Room room, @Context javax.ws.rs.core.UriInfo uriInfo) {
        //Storing the new room in the data structure 
        DataStore.rooms.put(room.getId(), room);
        
        //Bbuilding the URL for the new room (e.g., .../api/v1/rooms/L1)
        java.net.URI location = uriInfo.getAbsolutePathBuilder()
                                  .path(room.getId())
                                  .build();
        
        //Returns 201 Created status and the created object as feedback 
        return Response.status(Response.Status.CREATED).entity(room).location(location).build();
    }
    
    //GET /api/v1/rooms/{roomId}
    //Fetches the detailed metadata for a specific room
    @GET
    @Path("/{roomId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRoomById(@PathParam("roomId") String roomId) {
        Room room = DataStore.rooms.get(roomId);
        
        if (room == null) {
            //Returns 404 if the room identifier does not exist
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        
        //Returns 200 OK with the room details 
        return Response.ok(room).build();
    }
    
    //Task 2.2
    //DELETE /api/v1/rooms/{roomId}
    //Decommissions a room only if it has no active sensors
    @DELETE
    @Path("/{roomId}")
    public Response deleteRoom(@PathParam("roomId") String roomId) {
        //Retrieves the perticular room from the data store
        Room room = DataStore.rooms.get(roomId);
        
        //Checkingh whether the room exists
        if (room == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        
        //Checking for linked sensors
        boolean hasSensors = false;
        for (Sensor s : DataStore.sensors.values()) {
            if (s.getRoomId().equals(roomId)) {
                hasSensors = true;
                break;
            }
        }
        
        //Blocking the deletion of the room if sensors are present 
        if (hasSensors) {
            throw new RoomNotEmptyException("The room is currently occupied by active hardware.");
        }
        
        //Removing the room if there are no sensors present
        DataStore.rooms.remove(roomId);
        
        //Return 204 No Content for a successful deletion
        return Response.noContent().build();
        
    }
} 
