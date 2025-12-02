package com.eatnote.service.impl;

import com.eatnote.dataobject.UserDTO;
import com.eatnote.exception.NotFoundException;
import com.eatnote.service.UserManagementService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserManagementServiceImpl implements UserManagementService {

    private Map<String, UserDTO> userDB = new ConcurrentHashMap<String, UserDTO>();


    @Override
    public UserDTO createUser(UserDTO userDTO) {
        String id = UUID.randomUUID().toString();
        userDTO.setId(id);
        getUserDB().put(userDTO.getId(), userDTO);
        return userDTO;
    }

    @Override
    public void updateUser(UserDTO userDTO) {
        UserDTO oldOne = getUserDB().get(userDTO.getId());
        if (oldOne == null) {
            throw new NotFoundException();
        } else {
            getUserDB().put(userDTO.getId(), userDTO);
        }

    }

    @Override
    public List<UserDTO> listUsers(String search) {
        List<UserDTO> list = null;
        if (StringUtils.isEmpty(search)) {
            list = getUserDB().values().stream().toList();
        } else {
            list = new ArrayList<>();
            for (UserDTO userDTO : getUserDB().values()) {
                if (userDTO.getUsername().contains(search) || userDTO.getFullName().contains(search)) {
                    list.add(userDTO);
                    continue;
                }
            }
        }
        return list;
    }

    @Override
    public UserDTO getUser(String id) {
        return getUserDB().get(id);
    }

    @Override
    public void deleteUser(String id) {
        getUserDB().remove(id);
    }


    private Map<String, UserDTO> getUserDB() {
        if (CollectionUtils.isEmpty(userDB)) {
            UserDTO user1 = new UserDTO("1", "howard", "Howard Chow", "howard@admin.com");
            UserDTO user2 = new UserDTO("2", "howard2", "Howard2 Chow", "howard2@admin.com");
            userDB.put(user1.getId(), user1);
            userDB.put(user2.getId(), user2);
        }
        return userDB;
    }
}
