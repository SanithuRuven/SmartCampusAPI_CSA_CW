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

//Task 5.3
@Provider
public class SensorUnavailableMapper implements ExceptionMapper<SensorUnavailableException> {

    @Override
    public Response toResponse(SensorUnavailableException exception) {
        Map<String, String> errorBody = new HashMap<>();
        errorBody.put("error", "Forbidden");
        errorBody.put("status", "403");
        errorBody.put("message", exception.getMessage());

        return Response.status(Response.Status.FORBIDDEN)
                .entity(errorBody)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}