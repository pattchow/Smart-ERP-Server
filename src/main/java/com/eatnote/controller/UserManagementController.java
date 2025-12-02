package com.eatnote.controller;

import com.eatnote.common.Result;
import com.eatnote.dataobject.UserDTO;
import com.eatnote.service.UserManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class UserManagementController {

    @Autowired
    private UserManagementService userManagementService;

    @GetMapping("/users")
    public Result<List<UserDTO>> listUsers(@RequestParam(required = false) String search) {
        return Result.success(userManagementService.listUsers(search));
    }

    @GetMapping("/users/{userId}")
    public Result<UserDTO> getUser(@PathVariable String userId) {
        return Result.success(userManagementService.getUser(userId));
    }

    @PutMapping("/users/{userId}")
    public Result<Void> updateUser(@PathVariable String userId, @RequestBody UserDTO userDTO) {
        userDTO.setId(userId);
        userManagementService.updateUser(userDTO);
        return Result.success();
    }

    @DeleteMapping("/users/{userId}")
    public Result<Void> deleteUser(@PathVariable String userId) {
        userManagementService.deleteUser(userId);

        return Result.success();
    }

    @PostMapping("/users")
    public Result<Void> createUser(@RequestBody UserDTO userDTO) {
        userManagementService.createUser(userDTO);
        return Result.success();
    }

}
