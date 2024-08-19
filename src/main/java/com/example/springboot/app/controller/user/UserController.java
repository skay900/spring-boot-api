package com.example.springboot.app.controller.user;

import com.example.springboot.app.dto.user.UserBaseDto;
import com.example.springboot.app.service.user.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/signIn")
    public ResponseEntity<UserBaseDto> login(@RequestBody UserBaseDto userBaseDto) throws Exception {
        return new ResponseEntity<>(userService.selectUser(userBaseDto), HttpStatus.OK);
    }

}