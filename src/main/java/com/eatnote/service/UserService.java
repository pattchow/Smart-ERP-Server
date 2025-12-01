package com.eatnote.service;

import com.eatnote.dataobject.UserDTO;

import java.util.List;

public interface UserService {
    UserDTO createUser(UserDTO userDTO);

    void updateUser(UserDTO userDTO);

    List<UserDTO> listUsers(String search);

    UserDTO getUser(String id);

    void deleteUser(String id);
}
