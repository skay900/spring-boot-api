package com.example.springboot.app.controller.user;

import com.example.springboot.app.dto.user.UserBaseDto;
import com.example.springboot.app.service.user.UserService;
import com.example.springboot.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "유저 API", description = "유저 관련 API")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/sign-up")
    public ApiResponse<Object> signUp(@RequestBody UserBaseDto userBaseDto) {
        return ApiResponse.createSuccess(userService.signUp(userBaseDto));
    }

    @PostMapping("/sign-in")
    public ApiResponse<Object> signIn(@RequestBody UserBaseDto userBaseDto) {
        return ApiResponse.createSuccess(userService.signIn(userBaseDto));
    }

    @GetMapping("/user-info")
    public ApiResponse<Object> selectUserInfo(@RequestParam String email) {
        UserBaseDto userBaseDto = new UserBaseDto();
        userBaseDto.setEmail(email);
        return ApiResponse.createSuccess(userService.selectUserInfo(userBaseDto));
    }

}