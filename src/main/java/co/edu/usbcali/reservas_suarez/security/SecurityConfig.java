package co.edu.usbcali.reservas_suarez.security;

import lombok.AllArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@AllArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    // BCrypt Bean global

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

    // CORS CONFIG

    @Bean
    public CorsConfigurationSource
    corsConfigurationSource() {

        CorsConfiguration configuration =
                new CorsConfiguration();

        configuration.addAllowedOrigin(
                "http://localhost:5173"
        );

        configuration.addAllowedMethod("*");

        configuration.addAllowedHeader("*");

        configuration.setAllowCredentials(
                true
        );

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration(
                "/**",
                configuration
        );

        return source;
    }

    // SECURITY FILTER

    @Bean
    public SecurityFilterChain
    securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        http

                // ENABLE CORS
                .cors(cors -> {})

                // DISABLE CSRF
                .csrf(csrf -> csrf.disable())

                // STATELESS JWT
                .sessionManagement(session ->

                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                // ENDPOINTS
                .authorizeHttpRequests(auth -> auth

                        // SWAGGER
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll()

                        // AUTH
                        .requestMatchers(
                                "/auth/**"
                        ).permitAll()

                        // COURTS PUBLIC
                        .requestMatchers(
                                "/courts/all",
                                "/courts/*"
                        ).permitAll()

                        // RESERVATIONS PUBLIC
                        .requestMatchers(
                                "/reservations/public",
                                "/reservations/create",
                                "/reservations/cancel/**",
                                "/reservations/update-client/**"
                        ).permitAll()

                        // OWNER ONLY
                        .anyRequest()
                        .hasAuthority(
                                "ROLE_OWNER"
                        )
                )

                // JWT FILTER
                .addFilterBefore(
                        jwtFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}