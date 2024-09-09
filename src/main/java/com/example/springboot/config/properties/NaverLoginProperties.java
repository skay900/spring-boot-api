package com.example.springboot.config.properties;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class NaverLoginProperties {

    @Value("${key.naver.client-id}")
    private String clientId;

    @Value("${key.naver.client-secret}")
    private String clientSecret;

    @Value("${auth.login.naver.authorization-uri}")
    private String authorizationUri;

    @Value("${auth.login.naver.redirection-uri}")
    private String redirectUri;

    @Value("${auth.login.naver.access-token-uri}")
    private String accessTokenUri;

    @Value("${auth.login.naver.member-profile-uri}")
    private String memberProfileUri;

    public String getAuthorizationUri(String state) {
        return String.format(this.authorizationUri, this.clientId, this.redirectUri, state);
    }

    public String getAccessTokenUri(String code, String state) {
        return String.format(this.accessTokenUri, this.clientId, this.clientSecret, code, state);
    }

}
