package com.example.springboot.app.controller.auth;

import com.example.springboot.config.jwt.JwtUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "권한 API", description = "권한 관련 API")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private JwtUtils jwtUtils;

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
    public ResponseEntity<Map<String, String>> getAccessToken(@Parameter(description = "토큰을 발급 받을 사용자 이름") @RequestParam String username) {
        String accessToken = jwtUtils.generateAccessToken(username);
        String refreshToken = jwtUtils.generateRefreshToken(username);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshAccessToken", refreshToken);

        return new ResponseEntity<>(tokens, HttpStatus.OK);
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
    public Map<String, Object> getDecodeAccessToken(@Parameter(description = "Decode 할 토큰") @RequestParam String accessToken) {
        return jwtUtils.decodeAccessToken(accessToken);
    }

}