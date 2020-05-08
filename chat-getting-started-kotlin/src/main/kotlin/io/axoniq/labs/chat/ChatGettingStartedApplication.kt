package io.axoniq.labs.chat

import com.google.common.base.Predicates
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
                .apis(Predicates.not(RequestHandlerSelectors.basePackage("org.springframework")))
                .paths(PathSelectors.any())
                .build()
    }

}

fun main(args: Array<String>) {
    runApplication<ChatGettingStartedApplication>(*args)
}