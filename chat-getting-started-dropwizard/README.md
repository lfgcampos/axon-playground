# ChatGettingStarted

How to start the ChatGettingStarted application
---

1. Run `mvn clean install` to build your application
1. Run the liquibase migrate with `java -jar target/chat-getting-started-dropwizard-0.0.1-SNAPSHOT.jar db migrate config.yml`
1. Start application with `java -jar target/chat-getting-started-dropwizard-0.0.1-SNAPSHOT.jar server config.yml`
1. To check that your application is running enter url `http://localhost:9090`

Health Check
---

To see your applications health enter url `http://localhost:9091/healthcheck`
