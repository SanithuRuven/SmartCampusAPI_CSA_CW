//Name: Sanithu Ruven Gunasena
//UOW ID: W212301
//IIT ID: 20241102

package uk.ac.westminster.exceptions;

//Imports
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.Map;

//Task 5.1
@Provider
public class RoomNotEmptyMapper implements ExceptionMapper<RoomNotEmptyException> {
    
    @Override
    public Response toResponse(RoomNotEmptyException exception) {
        //Creating a JSON-friendly Map for the response body
        Map<String, String> errorData = new HashMap<>();
        errorData.put("error", "Conflict");
        errorData.put("status", "409");
        //Mandated message: Explaining room is occupied by active hardware
        errorData.put("message", exception.getMessage());

        return Response.status(Response.Status.CONFLICT)
                .entity(errorData)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
    
}
