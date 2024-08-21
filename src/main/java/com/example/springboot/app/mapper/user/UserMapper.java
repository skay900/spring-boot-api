package com.example.springboot.app.mapper.user;

import com.example.springboot.app.dto.user.UserBaseDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    UserBaseDto insertUser(UserBaseDto userBaseDto);

    UserBaseDto selectUser(UserBaseDto userBaseDto);

}
