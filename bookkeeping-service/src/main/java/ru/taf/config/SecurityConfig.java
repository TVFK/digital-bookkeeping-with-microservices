package ru.taf.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(authorizationManagerRequest -> {
                    authorizationManagerRequest
                            .requestMatchers(HttpMethod.GET).hasAuthority("SCOPE_view_library")
                            .requestMatchers(HttpMethod.POST).hasAuthority("SCOPE_edit_library")
                            .requestMatchers(HttpMethod.PATCH).hasAuthority("SCOPE_edit_library")
                            .requestMatchers(HttpMethod.DELETE).hasAuthority("SCOPE_edit_library")
                            .anyRequest().denyAll();
                })
                .csrf(CsrfConfigurer::disable)
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer(oauth2ResourceServer -> oauth2ResourceServer
                        .jwt(Customizer.withDefaults()))
                .build();
    }
}
