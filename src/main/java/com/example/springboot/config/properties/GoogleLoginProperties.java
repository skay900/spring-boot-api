package com.example.springboot.config.properties;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class GoogleLoginProperties {

    @Value("${key.google.client-id}")
    private String clientId;

    @Value("${key.google.client-secret}")
    private String clientSecret;

    @Value("${auth.login.google.authorization-uri}")
    private String authorizationUri;

    @Value("${auth.login.google.redirection-uri}")
    private String redirectUri;

    @Value("${auth.login.google.access-token-uri}")
    private String accessTokenUri;

    @Value("${auth.login.google.member-profile-uri}")
    private String memberProfileUri;

}
