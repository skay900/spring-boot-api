package com.example.springboot.config;

import com.example.springboot.config.filter.AccessDomainFilter;
import com.example.springboot.config.jwt.JwtAuthenticationEntryPoint;
import com.example.springboot.config.jwt.JwtFilter;
import com.example.springboot.config.properties.AllowedEndpointsProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final List<String> endpoint;

    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final AccessDomainFilter accessDomainFilter;
    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter, AccessDomainFilter accessDomainFilter, AllowedEndpointsProperties allowedEndpointsProperties) {
        this.accessDomainFilter = accessDomainFilter;
        this.jwtFilter = jwtFilter;
        this.endpoint = allowedEndpointsProperties.getEndpoints().stream()
                .map(endpoint -> endpoint.replaceAll("/$", ""))
                .collect(Collectors.toList());
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(endpoint.toArray(new String[0])).permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                )
                .addFilterBefore(accessDomainFilter, UsernamePasswordAuthenticationFilter.class) // 지정된 도메인만 허용
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class); // accessToken 검증

        return http.build();
    }

}