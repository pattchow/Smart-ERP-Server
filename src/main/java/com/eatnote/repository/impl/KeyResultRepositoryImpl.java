package com.eatnote.repository.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.eatnote.entity.KeyResult;
import com.eatnote.repository.KeyResultRepository;
import com.eatnote.repository.mapper.KeyResultMapper;
import org.springframework.stereotype.Service;

@Service
public class KeyResultRepositoryImpl extends ServiceImpl<KeyResultMapper, KeyResult> implements KeyResultRepository {
}