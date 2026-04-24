API Design Overview

The Smart Campus Management API is a REST-based system that enables tracking and management of environmental parameters within university premises. The system offers a standardized data structure that can be used to connect campus infrastructure and IoT devices along with telemetry data.

System Features

    ● Facility Map: Segregates the campus into easily manageable Room Resources that help the administrators keep track of facility capacity and names.
    ● Hardware Register: Keeps an up-to-date register of sensors like Temperature, CO2,Humidity, and their operational status either ACTIVE or MAINTENANCE.
    ● Live Telemetry: Records and stores environmental telemetry data for audit trails as well as real-time telemetry information.
    
Architectural Characteristics:

    ● HATEOAS Discovery: All main resource collections can be dynamically discovered from the root URL, providing self-discovery capabilities.
    ● Sub-Resource Location: In keeping with RESTful principles, reading resources will always have a sensor ID before them, showing the physical dependency between them.
    ● Data Consistency (Side Effect): The completion of the POST operation on the reading resource updates the value of its parent Sensor's currentValue property.
    ● Leak-Free Security: All unanticipated exceptions (such as NullPointerExceptions) are intercepted by a GlobalExceptionMapper that converts any Java stack trace into a JSON response.
    ● Observability: Server logs are maintained using a JAX-RS Filter implementation that records each HTTP request and its response status code.

-------------------------------------------------------------------------------------------------------------------------------------------------------------------

Build and Launch Instructions

Prerequisites,

    ● Java JDK 8 or higher.
    ● Apache Tomcat 9.0.x.
    ● Maven for dependency management.
    ● An IDE (IntelliJ IDEA, Netbeans, Eclipse Enterprise etc…).

Deployment Steps,

    1) Clone/Import Project: Import the project into your IDE as a Maven project.
    2) Resolve Dependencies: Ensure Maven downloads the required JAX-RS (Jersey) and JSON (Jackson) libraries.
    3) Configure Tomcat:
        ● Create a Tomcat Run Configuration.
        ● Deploy the SmartCampusAPI:war exploded artifact.
        ● Set the Application Context to /SmartCampusAPI.
    4) Launch: Start the server. The API entry point will be:
        http://localhost:8080/SmartCampusAPI/api/v1

Note: If port 8080 is already in use by another service, please reconfigure the port in your
Tomcat Server settings to 8081 or 8082 and update the base URL accordingly.

-------------------------------------------------------------------------------------------------------------------------------------------------------------------

Sample cURL Commands
1) Service Discovery.

curl -X GET http://localhost:8080/SmartCampusAPI/api/v1

2) Create a Room
   
curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/sensors \
-H "Content-Type: application/json" \
-d '{"id": "TS1", "type": "TEMPERATURE", "roomId": "L1", "status": "ACTIVE"}'

3) Register a Sensor
   
curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/sensors \
-H "Content-Type: application/json" \
-d '{"id": "TS1", "type": "TEMPERATURE", "roomId": "L1", "status": "ACTIVE"}'

4) Adding a Reading
   
curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/sensors/TS1/readings \
-H "Content-Type: application/json" \
-d '{"id": "R101", "timestamp": 1713916800, "value": 24.5}'

5) Verify Side Effect
    
curl -X GET http://localhost:8080/SmartCampusAPI/api/v1/sensors/TS1

6) Filter Sensor by Type
curl -X GET "http://localhost:8080/SmartCampusAPI/api/v1/sensors?type=TEMPERATURE"

7) Room Deletation
    
curl -X DELETE http://localhost:8080/SmartCampusAPI/api/v1/rooms/L1

8) List All Rooms
    
curl -X GET http://localhost:8080/SmartCampusAPI/api/v1/rooms

9) List All Sensors
    
curl -X GET http://localhost:8080/SmartCampusAPI/api/v1/sensors

10) Access Perticular Room
    
curl -X GET http://localhost:8080/SmartCampusAPI/api/v1/rooms/L1

-------------------------------------------------------------------------------------------------------------------------------------------------------------------

Postman Test Video Demonstration

Google Drive Link - https://drive.google.com/file/d/1x_BbzVawfAZy9kBIjZKuiSOpEP0M29cC/view?usp=sharing

-------------------------------------------------------------------------------------------------------------------------------------------------------------------

REPORT

Google Drive Link For PDF Form - https://drive.google.com/file/d/1Kml00TdsveN2r2BYBrso0Y8rXKw5LFNJ/view?usp=sharing

Task 1.1

In the JAX-RS approach the default lifecycle for a resource class is a Per-Request, which means that a fresh instance is created in the runtime for all the incoming HTTP requests, rather than treating the class as a singleton. Once the requests are processed and the response has been sent back to the client, the instance is discarded and becomes eligible and available for garbage collection. 

This architectural decision has a huge impact on how the data is managed because these short-lived instances are stateless and they cannot store long term application data in the normal instance variables. Hence in my implementation I have managed it by centralizing all the data into a separate DataStore class using static members. This ensures that though the resource classes are constantly recreated that they all point to exact same shared memory location. To avoid any risk of data loss or race condition problems occurring when several instances are accessing the common resources concurrently, I have made sure that there is synchronization of the code with the help of concurrent collection classes such as ConcurrentHashMap and CopyOnWriteArrayList. These are internally synchronized and thus do not require manual synchronization.


Task 1.2

The use of Hypermedia links is one of the main characteristics of HATEOAS, which is the highest form of RESTful architecture. Instead of just sending out plain data, the server sends will send links, telling the client what they could do next. It creates a self-discoverable API, in which the developer would be able to browse through the entire API starting from just one link, such as the /api/v1 discovery link which is very similar to how people browse webpages.

Using this approach is much better for the developers than using static documentation because this eliminates any need to hardcode the URLs. If a server-side path changes it would not break the clients code as it dynamically follows the links that’s provided in the response than using fixed addresses. While the static documentation would provide and show every possible action Hypermedia provides contextual guidance here it only shows the links that’s are valid for the resources current state which greatly simplifies the developers job and makes the application much more flexible and easier to use.

Task 2.1

When returning the list of rooms, the choice between either to provide only IDs or the entire full room objects involves a trade-off between the network efficiency and the client’s performance. Where returning only the IDs would minimize the network bandwidth consumption because the payload size is very small compared to returning the full object, which is hugely beneficial for the mobile users and the users who might be on slower connections. But this would create a huge burden on the client-side processing as if the client needs to display room names or capacities the client will have to perform multiple separate HTTP requests for every single ID which would lead to a high latency and a "chatty" API that feels slow to the user.

On the other had in the returning full room objects increase the initial bandwidth usage of the application but it hugely improves the client experience on the other hand by sending all the details and the metadata in a single response back to the client which allows the client to render a complete dashboard or a list immediately without have to make any further requests. When implementing an application on a college level where the amount of room information is small, the advantage of minimizing the number of trips to the server more than compensates for the slight increase in the size of data transmitted.


Task 2.2

The DELETE operation in my implementation is not idempotent. It is not idempotent because the response from the server is not the same every time and it changes depending on how many time the particular request has been sent. For a REST operation to be considered idempotent, multiple identical request should always return the same response. In my implementation  if the client sends the first DELETE request for an empty room then the server successfully removes that particular room and returns a 204 No Content status. But if the client mistakenly send the exact same request a second time then the server would look for that particular room again and realize that room does not exist anymore and triggers the if (room == null) validation check which would return a 404 Not Found response to the client.

Though the final state of the server where the room is no longer there will remain the same the change in the HTTP status code from success (204) to an error (404) means that our operation does not produce the exact same result every time the same request is made hence the implementation is not idempotent. Furthermore our implementation according to the business logic would return error (404) response if the room to be deleted has sensor during the first request.


Task 3.1

The usage of the @Consumes(MediaType.APPLICATION_JSON) annotation is a very important check on for what type of data can be accepted. As soon as the client tries to send data in any other format like text/plain or application/xml then the JAX-RS runtime engine (Jersey) would blocks the call from being executed. The reason behind the blockage is the following, at the very beginning of the request processing, the JAX-RS runtime checks the value of the Content-Type HTTP header, and in a  where it is not set as application/json, no further data mapping to your Java Sensor class will take place.

In the case of a non-matching data type, the JAX-RS runtime engine will automatically responds to the client developer with the 415 HTTP code (Unsupported Media Type). Such error code means that the server cannot accept or process the provided data since it is physically impossible. That built-in verification mechanism is vital for the security purposes of the application as without it you would get any data (e.g., plain text or xml), which the Java application simply could not interpret.


Task 3.2

The using of a @QueryParam is superior because it follows the rule that the URL path should name the resource (the sensors), while the query string should filter the results. These path parameters are intended to identify any specific items which will be used for filters like /sensors/type/CO2 making our API rigid as the “type” becomes a mandatory part of the address. In contrast the query parameters are optional and it allows one single endpoint to handle both a full list as well as a filtered search without having to have any extra code.

Also this approach alslows us to combine multiple parameter easily (e.g., /sensors?type=CO2&status=ACTIVE) incase a user wanted to search using multiple filters and using path parameters in a instance such as this would make things much complicated as it would require us to create specific URL paths for all the possible combination whouch would be a huge amount of work that takes a large amount of time. By using query parameters we are able to keep the API clean, scalable and very easy for the developers to navigate similar to search engine hence query parameter approach is generally considered superior for filtering and searching collections. 


Task 4.1

One of the main key benefits of the Sub-Resource Locator pattern is in terms of separation of concerns which is achieved by using a dedicated SensorReadingResource, due to this the parent resource of SensorResource does not need to care about any logic of historical data as it will be handled by the sub-resource. Using the separation of concerns will prevent you from creating a Fat Controller, which is a single huge class with hundreds of methods which would be extremely hard to debug and manage as the API gets bigger.

Using smaller classes in the API will enhance its code quality and scalability and ease work for other developers working on the same project. The modularity feature of the Sub-Resource Locator pattern enables different people to work on different aspects of an API without having issues with debugging and merging code written by other people working in the same domain. 


Task 5.2

Using the HTTP 422 Unprocessable Entity is considered to be more semantically accurate than the 404 Not Found because it has the ability to distinguish between a missing URL and a missing internal reference. The 404 error traditionally just indicates that the specific we address (URI) typed in the browser does not exist on the server. However the URL endpoint itself is a perfectly valid and active endpoint and the actual failure occurs due to the data inside the JSON body refers to a source that is not in the database.

Using the HTTP 422 response tells the client that the server understood the request and the request format was also correct but the issue is that it cannot process the instruction due to semantic errors. If we were to return a 404 error the developer might mistakenly think that the issue they have is a typo in the API URL. Due to this by returning a 422 response we explicitly tell that the request reached the right place but the business logic failed because of a missing dependency. This clarity in the response makes the debugging much easier for the client side developer because it is separating the routing issued from the data integrity issues. 


Task 5.4

Revealing the internal stack traces of Java to the outside world is a very serious security problem mainly because of Information Disclosure that’s caused by it where information regarding to the server’s environment is exposed. The stack trace provides a complete blueprint of how the system works internally which is something that should not be exposed. This means that it exposes the version numbers of the frameworks used (Jersey, Tomcat, etc…) and the version of the Java Runtime Environment used.

Other than the above the stack traces will also reveal physical file directory paths on the server as well as class names and method names used internally within the application. Such knowledge makes it easy to discover any vulnerabilities in terms of validation and authentication in order to exploit them at a later point. For a database-driven application, stack traces may even include table names and parts of SQL queries which is all the information a hacker or an attacker need in order to start an SQL Injection attack. The best way to avoid this kind of scenario is to use a global exception mapper to respond with an HTTP 500 error. 


Task 5.5

Using a JAX-RS filter is much better because it centralizes the cross cutting concerns, which allows us to write the logging logic once instead of repeating the same logging logic in every single method. This follows the DRY (Don’t Repeat Yourself) principle which keeps the resource classes clean and strictly focused on the all important business logic rather than the infostructure.

Using this approach also ensures that there is guaranteed observability across the entire API. Manual logging is very prone to human error and developer might forget to add a new endpoint where a filter would automatically capture every request and response. Hence by using the filters, we ensure that metadata such as the HTTP methods and the status codes are recorded consistently which provide reliable audit trail that is much easier to maintain and understand as the system grows.



