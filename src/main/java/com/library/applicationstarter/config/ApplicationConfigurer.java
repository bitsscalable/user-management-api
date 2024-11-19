package com.library.applicationstarter.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ApplicationConfigurer implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(@SuppressWarnings("null") CorsRegistry registry) {
        registry.addMapping("/**")  // Apply CORS settings to all endpoints
                .allowedOrigins("*")  // Specify allowed origin (Angular app running on port 4200)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  // Specify allowed HTTP methods
                .allowedHeaders("*")  // Allow all headers
                .exposedHeaders("Authorization") // Expose headers if you need to send JWT token back to the client
                .allowCredentials(false) // Allow credentials like cookies, authorization headers
                .maxAge(3600);  // Allow credentials
    }

}
