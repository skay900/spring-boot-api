package com.example.springboot.config;

import com.example.springboot.filter.AccessDomainFilter;
import com.example.springboot.jwt.JwtFilter;
import com.example.springboot.jwt.JwtAuthenticationEntryPoint;
import com.example.springboot.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    private final AccessDomainFilter accessDomainFilter;
    private final JwtFilter jwtFilter;
    private final JwtUtils jwtUtils;

    public SecurityConfig(JwtFilter jwtFilter, JwtUtils jwtUtils, AccessDomainFilter accessDomainFilter) {
        this.accessDomainFilter = accessDomainFilter;
        this.jwtFilter = jwtFilter;
        this.jwtUtils = jwtUtils;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api-docs/**", "/swagger-ui/**", "/auth/accessToken").permitAll()
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