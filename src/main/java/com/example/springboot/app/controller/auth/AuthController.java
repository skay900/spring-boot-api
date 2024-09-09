package com.example.springboot.app.controller.auth;

import com.example.springboot.app.dto.user.UserBaseDto;
import com.example.springboot.app.service.user.UserService;
import com.example.springboot.common.utils.HttpRequestUtil;
import com.example.springboot.config.jwt.JwtUtils;
import com.example.springboot.config.properties.GoogleLoginProperties;
import com.example.springboot.config.properties.NaverLoginProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Tag(name = "권한 API", description = "권한 관련 API")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserService userService;

    @Autowired
    private NaverLoginProperties naverLogin;

    @Autowired
    private GoogleLoginProperties googleLogin;

    @Value("${front.login.redirection-uri}")
    private String FRONT_REDIRECT_URI;

    @Operation(summary = "AccessToken 발행 API", description = "Name 입력 후 요청하면 AccessToken 이 발행됩니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully generated tokens",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = "{\n" +
                                    "  \"refreshAccessToken\": \"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiLjhYfjhYHjhLTjhYciLCJyb2xlcyI6InVzZXIiLCJpc3MiOiJzcHJpbmctYm9vdC1hcGkiLCJleHAiOjE3MjMwMTg5NjAsImlhdCI6MTcyMjkzMjU2MCwidXNlcm5hbWUiOiLjhYfjhYHjhLTjhYcifQ.m0wpf7JDlomqxHF6pVkII_q8aVgrLa1_1YHwRnXBIaw\",\n" +
                                    "  \"accessToken\": \"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiLjhYfjhYHjhLTjhYciLCJyb2xlcyI6InVzZXIiLCJpc3MiOiJzcHJpbmctYm9vdC1hcGkiLCJleHAiOjE3MjI5MzYxNjAsImlhdCI6MTcyMjkzMjU2MCwidXNlcm5hbWUiOiLjhYfjhYHjhLTjhYcifQ.7L-fbUsYPi-LG5taAVlHnO6Kk0uMrwQZP6DyBU9obxs\"\n" +
                                    "}"))),
            @ApiResponse(responseCode = "400", description = "Invalid username supplied",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping("/accessToken")
    public com.example.springboot.common.dto.ApiResponse<Object> getAccessToken(@Parameter(description = "토큰을 발급 받을 사용자 이름") @RequestParam String username) {
        String accessToken = jwtUtils.generateAccessToken(username);
        String refreshToken = jwtUtils.generateRefreshToken(username);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshAccessToken", refreshToken);

        return com.example.springboot.common.dto.ApiResponse.createSuccess(tokens);
    }

    @Operation(summary = "AccessToken Decode API", description = "발급 받은 AccessToken 입력 후 요청하면 AccessToken 이 Decode 됩니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully decoded token",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = "{\n" +
                                    "  \"subject\": \"ㅇㅁㄴㅇ\",\n" +
                                    "  \"roles\": \"user\",\n" +
                                    "  \"expiration\": \"2024-08-07T08:22:40.000+00:00\",\n" +
                                    "  \"issuedAt\": \"2024-08-06T08:22:40.000+00:00\",\n" +
                                    "  \"issuer\": \"spring-boot-api\",\n" +
                                    "  \"username\": \"test\"\n" +
                                    "}"))),
            @ApiResponse(responseCode = "400", description = "Invalid username supplied",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping("/decodeAccessToken")
    public com.example.springboot.common.dto.ApiResponse<Object> getDecodeAccessToken(@Parameter(description = "Decode 할 토큰") @RequestParam String accessToken) {
        return com.example.springboot.common.dto.ApiResponse.createSuccess(jwtUtils.decodeAccessToken(accessToken));
    }

    @GetMapping("/naver")
    public ResponseEntity<Void> redirectToNaverLogin(HttpServletRequest request) {
        String state = UUID.randomUUID().toString(); // CSRF 보호를 위한 state 값 생성

        // 세션에 state 값 저장
        HttpSession session = request.getSession();
        session.setAttribute("oauthState", state);

        String loginUrl = String.format(
                naverLogin.getAuthorizationUri(), naverLogin.getClientId(), naverLogin.getRedirectUri(), state
        );
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(loginUrl)).build();
    }

    @GetMapping("/naver/callback")
    public ResponseEntity<Void> naverLoginCallback(@RequestParam String code, @RequestParam String state
            , HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException {

        // 세션에서 저장된 state 값을 가져옴
        HttpSession session = request.getSession();
        String storedState = (String) session.getAttribute("oauthState");

        // 저장된 state와 네이버에서 받은 state 값이 일치하는지 확인
        if (storedState == null || !storedState.equals(state)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // state 값이 일치하지 않으면 에러 처리
        }

        ObjectMapper objectMapper = new ObjectMapper();

        // 네이버 접근 토큰 발급
        String accessTokenURI = String.format(
                naverLogin.getAccessTokenUri(), naverLogin.getClientId(), naverLogin.getClientSecret(), code, state
        );
        String tokenResponse = HttpRequestUtil.get(accessTokenURI, null);
        JsonNode tokenNode = objectMapper.readTree(tokenResponse);

        // 네이버 회원 프로필 조회
        String header = "Bearer " + tokenNode.get("access_token").asText();
        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Authorization", header);
        String responseBody = HttpRequestUtil.get(naverLogin.getMemberProfileUri(), requestHeaders);
        JsonNode memberNode = objectMapper.readTree(responseBody);

        if ("00".equals(memberNode.get("resultcode").asText())) { // "00" 성공 코드
            JsonNode respNode = memberNode.get("response");
            String email = respNode.get("email").asText();

            UserBaseDto userBaseDto = new UserBaseDto();
            userBaseDto.setEmail(email);
            userBaseDto.setName(respNode.get("name").asText());
            userBaseDto.setPhone(respNode.get("mobile").asText());
            userBaseDto.setSocial("NAVER");

            // 이메일이 존재하지 않을 시 회원가입
            if (ObjectUtils.isEmpty(userService.selectUserInfo(userBaseDto))) {
                userService.signUp(userBaseDto);
            }

            // 로그인 엑세스 토큰 발급
            String accessToken = jwtUtils.generateAccessToken(email);
            
            // 토큰 쿠키 생성
            Cookie tokenCookie = new Cookie("accessToken", accessToken);
            tokenCookie.setPath("/");  // 애플리케이션 전체 경로에서 접근 가능
            tokenCookie.setMaxAge(60 * 60);  // 쿠키 유효 시간 설정 (1시간)

            // email 쿠키 생성
            Cookie emailCookie = new Cookie("email", email);
            emailCookie.setPath("/");  // 애플리케이션 전체 경로에서 접근 가능
            emailCookie.setMaxAge(60 * 60);  // 쿠키 유효 시간 설정 (1시간)

            // 쿠키 저장
            response.addCookie(tokenCookie);
            response.addCookie(emailCookie);

        } else {
            throw new AccessDeniedException("네이버 로그인 실패");
        }

        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(FRONT_REDIRECT_URI)).build();
    }

    @GetMapping("/google")
    public ResponseEntity<Void> redirectToGoogleLogin(HttpServletRequest request) {
        String state = UUID.randomUUID().toString(); // CSRF 보호를 위한 state 값 생성

        // 세션에 state 값 저장
        HttpSession session = request.getSession();
        session.setAttribute("oauthState", state);

        String loginUrl = String.format(
                googleLogin.getAuthorizationUri(), googleLogin.getClientId(), googleLogin.getRedirectUri(), state
        );
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(loginUrl)).build();
    }

}