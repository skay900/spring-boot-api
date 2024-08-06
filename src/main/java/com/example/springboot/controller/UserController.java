package com.example.springboot.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "유저 API", description = "유저 관련 API")
@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping("/users")
    public String getUsers() {
        return "Access granted";
    }

}