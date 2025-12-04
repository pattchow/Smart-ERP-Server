package com.eatnote.repository.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.eatnote.entity.Objective;
import com.eatnote.repository.ObjectiveRepository;
import com.eatnote.repository.mapper.ObjectiveMapper;
import org.springframework.stereotype.Service;

@Service
public class ObjectiveRepositoryImpl extends ServiceImpl<ObjectiveMapper, Objective> implements ObjectiveRepository {
}