# ChatGettingStarted

How to start the ChatGettingStarted application
---

1. Run `mvn clean verify` to build your application
1. Run the liquibase migrate with `java -jar target/chat-getting-started-dropwizard-0.0.1-SNAPSHOT.jar db migrate config.yml`
1. Start application with `java -jar target/chat-getting-started-dropwizard-0.0.1-SNAPSHOT.jar server config.yml`
1. To check that your application is running, check [http://localhost:9090/swagger-ui.html](http://localhost:9090/swagger-ui.html)

<b>Note</b>: The Swagger UI does not support the 'Subscription Query' further on in the assignment,
 as Swagger does not support streaming results. 
Issuing a regular `curl` operation, or something along those lines, is the recommended way to check the Subscription Query.

<b>Note 2</b>: If you are on Intellij IDEA, you can also use the `command-request.http`
 and `query-request.http` files in this project to send requests directly from your IDE.
Several defaults have been provided, but feel free to play around here!

### H2 Console ###
The application has the 'H2 Console' configured, so you can peek into the database's contents.

Visit: [http://localhost:9090/h2-console](http://localhost:9090/h2-console)  
Enter JDBC URL: `jdbc:h2:mem:testdb`  
Leave other values to defaults and click 'connect'.

Health Check
---

To see your applications health, visit [http://localhost:9091/healthcheck](http://localhost:9091/healthcheck)
