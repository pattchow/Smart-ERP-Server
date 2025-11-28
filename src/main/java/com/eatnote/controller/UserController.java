package com.eatnote.controller;

import com.eatnote.common.Result;
import com.eatnote.dataobject.UserDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    @GetMapping("/users")
    public Result<List<UserDTO>> listUsers(@RequestParam String search) {
        if (search != null && search.equals("howard")) {
            return Result.success(List.of(
                    new UserDTO("1", "howard", "Howard Chow", "howard@admin.com")
                    , new UserDTO("2", "howard2", "Howard2 Chow", "howard2@admin.com")
            ));
        } else {
            return Result.error("not found");
        }
    }

    @GetMapping("/users/{userId}")
    public Result<UserDTO> getUser(@PathVariable String userId) {
        if (userId != null && userId.equals("1")) {
            return Result.success(new UserDTO("1", "howard", "Howard Chow", "howard@admin.com"));
        } else {
            return Result.error("not found");
        }
    }

    @PutMapping("/users/{userId}")
    public Result<Void> updateUser(@PathVariable String userId, @RequestBody UserDTO userDTO) {
        if (userId != null && userId.equals("1")) {
            return Result.success();
        } else {
            return Result.error("not found");
        }
    }

    @DeleteMapping("/users/{userId}")
    public Result<Void> deleteUser(@PathVariable String userId) {
        if (userId != null && userId.equals("1")) {
            return Result.success();
        } else {
            return Result.error("not found");
        }
    }

    @PostMapping("/users")
    public Result<Void> createUser(@RequestBody UserDTO userDTO) {
        if (userDTO != null) {
            return Result.success();
        } else {
            return Result.error("not found");
        }
    }

}
