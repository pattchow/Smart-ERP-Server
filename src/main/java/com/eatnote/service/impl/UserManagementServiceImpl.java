package com.eatnote.service.impl;

import com.eatnote.dataMapper.UserMapper;
import com.eatnote.dataobject.UserDTO;
import com.eatnote.entity.User;
import com.eatnote.repository.UserRepository;
import com.eatnote.service.UserManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class UserManagementServiceImpl implements UserManagementService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        String id = UUID.randomUUID().toString();
        userDTO.setId(id);

        User user = userMapper.dtoToEntity(userDTO);
        user.setCreatedTime(LocalDateTime.now());
        user.setUpdatedTime(LocalDateTime.now());
        userRepository.save(user);
        return userDTO;
    }

    @Override
    public void updateUser(UserDTO userDTO) {
        User user = userMapper.dtoToEntity(userDTO);
        user.setUpdatedTime(LocalDateTime.now());
        userRepository.update(user);
    }

    @Override
    public List<UserDTO> listUsers(String search) {
        List<User> list = userRepository.search(search);
        return list.stream().map(p -> userMapper.entityToDTO(p)).toList();
    }

    @Override
    public List<UserDTO> listUsersByOrgId(String orgId) {
        List<User> list = userRepository.findByOrgId(orgId);
        return list.stream().map(p -> userMapper.entityToDTO(p)).toList();
    }


    @Override
    public UserDTO getUser(String id) {
        User user = userRepository.getById(id);
        return userMapper.entityToDTO(user);
    }

    @Override
    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }


}
