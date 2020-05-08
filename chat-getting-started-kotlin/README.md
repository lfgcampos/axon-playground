# ChatGettingStarted

How to start the ChatGettingStarted application
---

1. Run `./gradlew build` to build your application
1. Start application with `./gradlew bootRun`
1. To check that your application is running, check [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

<b>Note</b>: The Swagger UI does not support the 'Subscription Query' further on in the assignment,
 as Swagger does not support streaming results. 
Issuing a regular `curl` operation, or something along those lines, is recommended to check the Subscription Query.

<b>Note 2</b>: If you are on Intellij IDEA, you can also use the `command-request.http`
 and `query-request.http` files in this project to send requests directly from your IDE.
Several defaults have been provided, but feel free to play around here!

### H2 Console ###
The application has the 'H2 Console' configured, so you can peek into the database's contents.

Visit: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)  
Enter JDBC URL: `jdbc:h2:mem:testdb`  
Leave other values to defaults and click 'connect'.
