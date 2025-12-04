package com.eatnote.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.eatnote.entity.KeyResult;
import com.eatnote.entity.Objective;
import com.eatnote.repository.KeyResultRepository;
import com.eatnote.repository.ObjectiveRepository;
import com.eatnote.service.OkrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OkrServiceImpl implements OkrService {

    @Autowired
    private ObjectiveRepository objectiveRepository;

    @Autowired
    private KeyResultRepository keyResultRepository;

    @Override
    public Objective createObjective(Objective objective) {
        objective.setCreatedTime(LocalDateTime.now());
        objective.setUpdatedTime(LocalDateTime.now());
        objectiveRepository.save(objective);
        return objective;
    }

    @Override
    public Objective updateObjective(Objective objective) {
        objective.setUpdatedTime(LocalDateTime.now());
        objectiveRepository.updateById(objective);
        return objective;
    }

    @Override
    public Objective getObjectiveById(String id) {
        return objectiveRepository.getById(id);
    }

    @Override
    public List<Objective> listObjectives() {
        return objectiveRepository.list();
    }

    @Override
    public List<Objective> listObjectivesByOwnerId(String ownerId) {
        LambdaQueryWrapper<Objective> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Objective::getOwnerId, ownerId);
        return objectiveRepository.list(queryWrapper);
    }

    @Override
    public void deleteObjective(String id) {
        objectiveRepository.removeById(id);
    }

    @Override
    public KeyResult createKeyResult(KeyResult keyResult) {
        keyResult.setCreatedTime(LocalDateTime.now());
        keyResult.setUpdatedTime(LocalDateTime.now());
        keyResultRepository.save(keyResult);
        return keyResult;
    }

    @Override
    public KeyResult updateKeyResult(KeyResult keyResult) {
        keyResult.setUpdatedTime(LocalDateTime.now());
        keyResultRepository.updateById(keyResult);
        return keyResult;
    }

    @Override
    public KeyResult getKeyResultById(String id) {
        return keyResultRepository.getById(id);
    }

    @Override
    public List<KeyResult> listKeyResultsByObjectiveId(String objectiveId) {
        LambdaQueryWrapper<KeyResult> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(KeyResult::getObjectiveId, objectiveId);
        return keyResultRepository.list(queryWrapper);
    }

    @Override
    public List<KeyResult> listKeyResultsByOwnerId(String ownerId) {
        LambdaQueryWrapper<KeyResult> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(KeyResult::getOwnerId, ownerId);
        return keyResultRepository.list(queryWrapper);
    }

    @Override
    public void deleteKeyResult(String id) {
        keyResultRepository.removeById(id);
    }
}