//Name: Sanithu Ruven Gunasena
//UOW ID: W212301
//IIT ID: 20241102

package uk.ac.westminster.exceptions;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.Map;

//Task 5.2
@Provider
public class LinkedResourceNotFoundMapper implements ExceptionMapper<LinkedResourceNotFoundException> {

    @Override
    public Response toResponse(LinkedResourceNotFoundException exception) {
        Map<String, String> errorBody = new HashMap<>();
        errorBody.put("error", "Unprocessable Entity");
        errorBody.put("status", "422");
        errorBody.put("message", exception.getMessage());

        //Returning HTTP 422
        return Response.status(422)
                .entity(errorBody)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
