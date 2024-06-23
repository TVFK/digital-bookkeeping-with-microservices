package ru.taf.eureka.security;

import jakarta.annotation.Priority;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    @Priority(0)
    public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
        return http
                .securityMatcher("/eureka/apps", "/eureka/apps/**")
                .authorizeHttpRequests(custom -> custom
                                .anyRequest().hasAuthority("SCOPE_discovery"))
                .sessionManagement(custom -> custom.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(CsrfConfigurer::disable)
                .oauth2ResourceServer(custom -> custom.jwt(Customizer.withDefaults()))
                .build();
    }

    @Bean
    @Priority(1)
    public SecurityFilterChain clientsFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(custom -> custom.anyRequest().authenticated())
                .oauth2Login(Customizer.withDefaults())
                .build();
    }
}
