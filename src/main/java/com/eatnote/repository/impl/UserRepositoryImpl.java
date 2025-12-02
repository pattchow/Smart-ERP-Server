package com.eatnote.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.eatnote.entity.User;
import com.eatnote.repository.UserRepository;
import com.eatnote.repository.mapper.UserMapper;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UserRepositoryImpl extends ServiceImpl<UserMapper, User> implements UserRepository {


    @Override
    public void update(User user) {
        super.saveOrUpdate(user);
    }

    @Override
    public User getById(String id) {
        return super.getById(id);
    }

    @Override
    public User getByUsername(String username) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        return getOne(wrapper);
    }

    @Override
    public List<User> search(String search) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(User::getUsername, search);
        wrapper.like(User::getFullName, search);
        wrapper.like(User::getEmail, search);
        return list(wrapper);
    }

    @Override
    public List<User> findByOrgId(String orgId) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getOrgId, orgId);
        return list(wrapper);
    }

    @Override
    public boolean deleteById(String id) {
        return super.removeById(id);
    }

    @Override
    public boolean save(User entity) {
        return super.saveOrUpdate(entity);
    }
}
