//Name: Sanithu Ruven Gunasena
//UOW ID: W212301
//IIT ID: 20241102

package uk.ac.westminster.exceptions;

//Task 5.3
//The exception thrown when a POST reading is attempted on a sensor currently in MAINTENANCE mode
public class SensorUnavailableException extends RuntimeException {
    public SensorUnavailableException(String message) {
        super(message);
    }
}
