package com.eatnote.controller;

import com.eatnote.common.Result;
import com.eatnote.entity.KeyResult;
import com.eatnote.entity.Objective;
import com.eatnote.service.OkrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class OkrManagementController {

    @Autowired
    private OkrService okrService;

    // Objective related endpoints

    @PostMapping("/objectives")
    public Result<Objective> createObjective(@RequestBody Objective objective) {
        return Result.success(okrService.createObjective(objective));
    }

    @PutMapping("/objectives/{id}")
    public Result<Objective> updateObjective(@PathVariable String id, @RequestBody Objective objective) {
        objective.setId(id);
        return Result.success(okrService.updateObjective(objective));
    }

    @GetMapping("/objectives/{id}")
    public Result<Objective> getObjectiveById(@PathVariable String id) {
        return Result.success(okrService.getObjectiveById(id));
    }

    @GetMapping("/objectives")
    public Result<List<Objective>> listObjectives() {
        return Result.success(okrService.listObjectives());
    }

    @GetMapping("/objectives/users/{ownerId}")
    public Result<List<Objective>> listObjectivesByOwnerId(@PathVariable String ownerId) {
        return Result.success(okrService.listObjectivesByOwnerId(ownerId));
    }

    @DeleteMapping("/objectives/{id}")
    public Result<Void> deleteObjective(@PathVariable String id) {
        okrService.deleteObjective(id);
        return Result.success();
    }

    // KeyResult related endpoints

    @PostMapping("/objectives/{objectiveId}/key-results")
    public Result<KeyResult> createKeyResult(@PathVariable String objectiveId, @RequestBody KeyResult keyResult) {
        keyResult.setObjectiveId(objectiveId);
        return Result.success(okrService.createKeyResult(keyResult));
    }

    @PutMapping("/objectives/{objectiveId}/key-results/{id}")
    public Result<KeyResult> updateKeyResult(@PathVariable String objectiveId, @PathVariable String id, @RequestBody KeyResult keyResult) {
        keyResult.setId(id);
        keyResult.setObjectiveId(objectiveId);
        return Result.success(okrService.updateKeyResult(keyResult));
    }

    @GetMapping("/objectives/{objectiveId}/key-results/{id}")
    public Result<KeyResult> getKeyResultById(@PathVariable String objectiveId, @PathVariable String id) {
        return Result.success(okrService.getKeyResultById(id));
    }

    @GetMapping("/objectives/{objectiveId}/key-results")
    public Result<List<KeyResult>> listKeyResultsByObjectiveId(@PathVariable String objectiveId) {
        return Result.success(okrService.listKeyResultsByObjectiveId(objectiveId));
    }

    @GetMapping("/key-results/users/{ownerId}")
    public Result<List<KeyResult>> listKeyResultsByOwnerId(@PathVariable String ownerId) {
        return Result.success(okrService.listKeyResultsByOwnerId(ownerId));
    }

    @DeleteMapping("/objectives/{objectiveId}/key-results/{id}")
    public Result<Void> deleteKeyResult(@PathVariable String objectiveId, @PathVariable String id) {
        okrService.deleteKeyResult(id);
        return Result.success();
    }
}