package com.example.springboot.app.service.user;

import com.example.springboot.app.dto.user.UserBaseDto;
import com.example.springboot.app.mapper.user.UserMapper;
import com.example.springboot.common.utils.MessageService;
import com.example.springboot.config.jwt.JwtUtils;
import com.example.springboot.exception.DuplicateEmailException;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserMapper userMapper;

    @Transactional
    public UserBaseDto signUp(UserBaseDto userBaseDto) {
        if (ObjectUtils.isNotEmpty(userMapper.selectUser(userBaseDto))) {
            throw new DuplicateEmailException(messageService.getMessage("error.user.duplicated.email"));
        }

        if (StringUtils.isNoneBlank(userBaseDto.getPassword())) {
            userBaseDto.setPassword(passwordEncoder.encode(userBaseDto.getPassword()));
        }

        return userMapper.insertUser(userBaseDto);
    }

    public UserBaseDto signIn(UserBaseDto userBaseDto) {
        UserBaseDto returnDto = userMapper.selectUser(userBaseDto);

        if (returnDto == null || !passwordEncoder.matches(userBaseDto.getPassword(), returnDto.getPassword())) {
            throw new AccessDeniedException(messageService.getMessage("error.user.inconsistency.email.password"));
        }

        returnDto.setPassword(null);
        String userEmail = returnDto.getEmail();
        returnDto.setAccessToken(jwtUtils.generateAccessToken(userEmail));
        returnDto.setRefreshAccessToken(jwtUtils.generateRefreshToken(userEmail));

        return returnDto;
    }

    public UserBaseDto selectUserInfo(UserBaseDto userBaseDto) {
        userBaseDto = userMapper.selectUser(userBaseDto);
        if (ObjectUtils.isNotEmpty(userBaseDto)) {
            userBaseDto.setPassword(null);
        }
        return userBaseDto;
    }

}
