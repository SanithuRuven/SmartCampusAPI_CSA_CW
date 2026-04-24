//Name: Sanithu Ruven Gunasena
//UOW ID: W212301
//IIT ID: 20241102

package uk.ac.westminster.filters;

//Imports
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.logging.Logger;

//Task 5.5
@Provider
public class AppLoggingFilter implements ContainerRequestFilter, ContainerResponseFilter {

    //Using the mandated java.util.logging.Logger
    private static final Logger LOGGER = Logger.getLogger(AppLoggingFilter.class.getName());

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String method = requestContext.getMethod();
        String uri = requestContext.getUriInfo().getRequestUri().toString();
        
        LOGGER.info("INCOMING REQUEST: " + method + " " + uri);
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        int status = responseContext.getStatus();
        
        LOGGER.info("OUTGOING RESPONSE Status: " + status);
    }
}