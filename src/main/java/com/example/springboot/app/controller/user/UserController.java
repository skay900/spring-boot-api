package com.example.springboot.app.controller.user;

import com.example.springboot.app.dto.user.UserBaseDto;
import com.example.springboot.app.service.user.UserService;
import com.example.springboot.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "유저 API", description = "유저 관련 API")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/signUp")
    public ApiResponse<Object> registration(@RequestBody UserBaseDto userBaseDto) {
        return ApiResponse.createSuccess(userService.insertUser(userBaseDto));
    }

    @PostMapping("/signIn")
    public ApiResponse<Object> login(@RequestBody UserBaseDto userBaseDto) {
        return ApiResponse.createSuccess(userService.selectUser(userBaseDto));
    }

}