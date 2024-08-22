package com.example.springboot.config;

import com.example.springboot.config.filter.AccessDomainFilter;
import com.example.springboot.config.jwt.JwtAuthenticationEntryPoint;
import com.example.springboot.config.jwt.JwtFilter;
import com.example.springboot.config.properties.AllowedEndpointsProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    private final AccessDomainFilter accessDomainFilter;
    private final JwtFilter jwtFilter;
    private final List<String> endpoint;

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
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
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

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList("http://localhost:3000")); // 허용할 도메인
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE")); // 허용할 HTTP 메소드
        config.setAllowedHeaders(List.of("Authorization", "Content-Type")); // 허용할 요청 헤더
        config.setAllowCredentials(true); // 자격 증명 허용 (예: 쿠키, 인증 정보)

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); // 모든 경로에 대해 CORS 설정 적용
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}