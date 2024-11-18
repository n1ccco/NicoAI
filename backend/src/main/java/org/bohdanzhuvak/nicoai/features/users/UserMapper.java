package org.bohdanzhuvak.nicoai.features.users;

import org.bohdanzhuvak.nicoai.features.users.dto.UserDto;
import org.bohdanzhuvak.nicoai.features.users.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

  public UserDto toUserDto(User user) {
    return UserDto.builder()
        .Id(user.getId())
        .username(user.getUsername())
        .role(user.getRoles().getFirst().replaceFirst("ROLE_", ""))
        .createdAt(user.getCreatedAt())
        .build();
  }
}