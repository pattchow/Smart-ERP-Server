package com.eatnote.repository;

import com.eatnote.entity.User;

import java.util.List;

public interface UserRepository {
    boolean save(User user);

    void update(User user);

    User getById(String id);

    User getByUsername(String username);

    List<User> search(String search);

    List<User> findByOrgId(String orgId);

    boolean deleteById(String id);
}
