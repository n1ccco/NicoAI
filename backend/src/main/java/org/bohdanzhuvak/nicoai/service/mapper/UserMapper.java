package org.bohdanzhuvak.nicoai.service.mapper;

import org.bohdanzhuvak.nicoai.dto.user.UserDto;
import org.bohdanzhuvak.nicoai.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

  public UserDto toUserDto(User user) {
    return UserDto.builder()
        .Id(user.getId())
        .username(user.getUsername())
        .roles(user.getRoles())
        .build();
  }
}