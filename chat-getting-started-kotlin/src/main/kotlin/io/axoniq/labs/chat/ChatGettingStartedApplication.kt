package io.axoniq.labs.chat

import org.axonframework.eventsourcing.eventstore.EventStorageEngine
import org.axonframework.eventsourcing.eventstore.inmemory.InMemoryEventStorageEngine
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

@SpringBootApplication
class ChatGettingStartedApplication {

    @Configuration
    @EnableSwagger2
    class SwaggerConfig {
        @Bean
        fun api(): Docket = Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
    }

//    @Configuration
//    class AxonConfig {
//        @Bean
//        fun storageEngine(): EventStorageEngine = InMemoryEventStorageEngine()
//    }

}

fun main(args: Array<String>) {
    runApplication<ChatGettingStartedApplication>(*args)
}