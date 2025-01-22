package com.sam.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(title = "Log Ingestor and Query Interface",
                version = "1.0.0",
                description = "A log ingestor system that can efficiently handle vast volumes of log data," +
                        "and a simple interface for querying this data using full-text search or specific " +
                        "field filters.",
                contact = @Contact(
                        name = "Samarth Verma",
                        email = "samarthverma2804@gmail.com"
                )
        ),
        servers = {
                @Server(description = "Local Environment",
                        url = "http://localhost:3000")
        }
)
public class OpenAIConfig {

}
