package com.eatnote.service;

import com.eatnote.dataobject.UserDTO;

import java.util.List;

public interface UserManagementService {
    UserDTO createUser(UserDTO userDTO);

    void updateUser(UserDTO userDTO);

    List<UserDTO> listUsers(String search);

    List<UserDTO> listUsersByOrgId(String orgId);

    UserDTO getUser(String id);

    void deleteUser(String id);
}
