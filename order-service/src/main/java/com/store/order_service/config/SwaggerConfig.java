package com.store.order_service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {

        return new OpenAPI().info(new Info()
                .title("FlowOrder")
                .version("1.0.0")
                .description("Asynchronous order processing system based on a microservices architecture, using RabbitMQ" +
                        " for event-driven communication." +
                        "<br> </br>The flow starts in the order service, which persists orders and publishes events." +
                        "The payment service processes these events and emits new ones, which are then consumed by the" +
                        " notification service."));

    }
}
