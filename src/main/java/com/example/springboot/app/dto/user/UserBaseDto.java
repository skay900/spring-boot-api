package com.example.springboot.app.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "User 정보")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserBaseDto {

    @Schema(description = "사용자 이메일(아이디)", example = "test@example.com")
    private String email;

    @Schema(description = "사용자 비밀번호", example = "password1!")
    private String password;

    @Schema(description = "사용자 이름", example = "테스터")
    private String name;

    @Schema(description = "사용자 전화번호", example = "01012345678")
    private String phone;

    @Schema(description = "소셜 로그인 타입", example = "NAVER")
    private String social;

    @Schema(description = "Access Token", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiLsnKDripDri5giLCJyb2xlcyI6InVzZXIiLCJpc3MiOiJzcHJpbmctYm9vdC1hcGkiLCJleHAiOjE3MjQwMzI2NDgsImlhdCI6MTcyNDAyOTA0OCwidXNlcm5hbWUiOiLsnKDripDri5gifQ.U85k8tNPV4lT7GQkCJIuyYSGnOcxkF9XPjCcS9kxsI")
    private String accessToken;

    @Schema(description = "Refresh Access Token", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiLsnKDripDri5giLCJyb2xlcyI6InVzZXIiLCJpc3MiOiJzcHJpbmctYm9vdC1hcGkiLCJleHAiOjE3MjQxMTU0NDgsImlhdCI6MTcyNDAyOTA0OCwidXNlcm5hbWUiOiLsnKDripDri5gifQ.ronjNbRt2WdwNqSnnwLDr26KV9n7aU_p8WFpV7uDjhw")
    private String refreshAccessToken;

}
