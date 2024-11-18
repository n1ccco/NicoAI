package org.bohdanzhuvak.nicoai.features.users.service;

import lombok.RequiredArgsConstructor;
import org.bohdanzhuvak.nicoai.features.users.UserMapper;
import org.bohdanzhuvak.nicoai.features.users.dto.UserDto;
import org.bohdanzhuvak.nicoai.features.users.model.User;
import org.bohdanzhuvak.nicoai.features.users.repository.UserRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;
  private final UserMapper userMapper;

  public List<UserDto> getUsers(){
    List<User> users =  userRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    return users.stream().map(userMapper::toUserDto).toList();
  }

  public void deleteUser(Long id){
    userRepository.deleteById(id);
  }
}
