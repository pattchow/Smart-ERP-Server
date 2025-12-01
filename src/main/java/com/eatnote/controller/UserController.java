package com.eatnote.controller;

import com.eatnote.common.Result;
import com.eatnote.dataobject.UserDTO;
import com.eatnote.service.UserService;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public Result<List<UserDTO>> listUsers(@RequestParam(required = false) String search) {

        return Result.success(userService.listUsers(search));


    }

    @GetMapping("/users/{userId}")
    public Result<UserDTO> getUser(@PathVariable String userId) {
        return Result.success(userService.getUser(userId));
    }

    @PutMapping("/users/{userId}")
    public Result<Void> updateUser(@PathVariable String userId, @RequestBody UserDTO userDTO) {
        userDTO.setId(userId);
        userService.updateUser(userDTO);
        return Result.success();
    }

    @DeleteMapping("/users/{userId}")
    public Result<Void> deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        return Result.success();
    }

    @PostMapping("/users")
    public Result<Void> createUser(@RequestBody UserDTO userDTO) {
        userService.createUser(userDTO);
        return Result.success();
    }

}
