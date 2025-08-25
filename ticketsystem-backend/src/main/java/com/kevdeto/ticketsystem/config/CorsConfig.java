package com.kevdeto.ticketsystem.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

//@Configuration
public class CorsConfig {
//
//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//
//        // 🌍 Orígenes permitidos (frontend)
//        configuration.setAllowedOrigins(List.of("http://localhost:3000")); // Dev
//        // En prod: List.of("https://mi-frontend.com")
//
//        // Métodos permitidos
//        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//
//        // Headers permitidos
//        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
//
//        // Cookies habilitadas
//        configuration.setAllowCredentials(true);
//
//        // Tiempo de cacheo de la config (opcional)
//        configuration.setMaxAge(3600L);
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }
}
