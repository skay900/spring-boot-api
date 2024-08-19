package com.example.springboot.app.service.user;

import com.example.springboot.app.dto.user.UserBaseDto;
import com.example.springboot.app.mapper.user.UserMapper;
import com.example.springboot.config.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserMapper userMapper;

    public UserBaseDto selectUser(UserBaseDto userBaseDto) {
        UserBaseDto returnDto = userMapper.selectUser(userBaseDto);

        if (returnDto == null) {
            throw new AccessDeniedException("아이디(Email) 또는 비밀번호가 일치하지 않습니다.");
        }

        String userEmail = returnDto.getEmail();
        returnDto.setAccessToken(jwtUtils.generateAccessToken(userEmail));
        returnDto.setRefreshAccessToken(jwtUtils.generateRefreshToken(userEmail));

        return returnDto;
    }

}
