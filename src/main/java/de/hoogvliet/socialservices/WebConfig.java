package de.hoogvliet.socialservices;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration public class WebConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Gilt für alle Endpunkte
                        .allowedOrigins("http://localhost:8080") // Erlaubt nur diese Origin
                        .allowedMethods("GET", "POST", "PUT", "DELETE") // Erlaubte Methoden
                        .allowedHeaders("*") // Erlaubt alle Header
                        .allowCredentials(true); // Erlaubt Cookies/Authentifizierung
            }
        };
    }

}
