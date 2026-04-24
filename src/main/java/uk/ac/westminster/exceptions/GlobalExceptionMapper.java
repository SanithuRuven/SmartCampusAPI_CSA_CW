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
import java.util.logging.Logger;

//Task 5.4
@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {

    //The logger for observability
    private static final Logger LOGGER = Logger.getLogger(GlobalExceptionMapper.class.getName());

    @Override
    public Response toResponse(Throwable exception) {
        
        //Making sure page not found error wont fall under 500 internal server error
        if (exception instanceof javax.ws.rs.WebApplicationException) {
            return ((javax.ws.rs.WebApplicationException) exception).getResponse();
        }
        
        //Logng the actual stack trace on the server side for the developer
        LOGGER.severe("CRITICAL ERROR: " + exception.getMessage());
        exception.printStackTrace(); 

        //Creating a generic and safe JSON body for the client
        Map<String, String> errorBody = new HashMap<>();
        errorBody.put("error", "Internal Server Error");
        errorBody.put("status", "500");
        errorBody.put("message", "An unexpected error occurred. Please contact the system administrator.");

        //Returning HTTP 500 without leaking any Java class names or line numbers
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(errorBody)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}