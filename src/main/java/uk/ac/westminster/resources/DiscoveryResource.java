//Name: Sanithu Ruven Gunasena
//UOW ID: W212301
//IIT ID: 20241102

package uk.ac.westminster.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

//Task 1.2
//Discovery endpoint which provides essential API metadata: versioning info, administrative contact details, and a mapofprimary resource collections
@Path("/")
public class DiscoveryResource {
    
    @GET
    @Produces(MediaType.APPLICATION_JSON) //Makes sure the response is formatted as JSON
    public Response getDiscovery() {
        
        //The root metadata object
        Map<String, Object> discoveryData = new HashMap<>();
        
        //Versioning info
        discoveryData.put("version", "1.0.0-Stable");
        
        //Administrative contact details
        discoveryData.put("adminContact", "admin@westminster.ac.uk");
        
        //Map of primary resource collections
        Map<String, String> links = new HashMap<>();
        
        links.put("rooms", "/api/v1/rooms");
        links.put("sensors", "/api/v1/sensors");
        
        discoveryData.put("links", links);
        
        //Returns 200 ok response
        return Response.ok(discoveryData).build();
    }
}
