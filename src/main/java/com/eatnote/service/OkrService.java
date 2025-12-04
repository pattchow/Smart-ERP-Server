package com.eatnote.service;

import com.eatnote.entity.KeyResult;
import com.eatnote.entity.Objective;

import java.util.List;

public interface OkrService {
    /**
     * Create a new objective
     * @param objective The objective to create
     * @return The created objective
     */
    Objective createObjective(Objective objective);

    /**
     * Update an existing objective
     * @param objective The objective with updated information
     * @return The updated objective
     */
    Objective updateObjective(Objective objective);

    /**
     * Get an objective by ID
     * @param id The ID of the objective
     * @return The objective with the given ID
     */
    Objective getObjectiveById(String id);

    /**
     * Get all objectives
     * @return List of all objectives
     */
    List<Objective> listObjectives();

    /**
     * Get all objectives by owner ID
     * @param ownerId The ID of the owner
     * @return List of objectives for the given owner
     */
    List<Objective> listObjectivesByOwnerId(String ownerId);

    /**
     * Delete an objective by ID
     * @param id The ID of the objective to delete
     */
    void deleteObjective(String id);

    /**
     * Create a new key result
     * @param keyResult The key result to create
     * @return The created key result
     */
    KeyResult createKeyResult(KeyResult keyResult);

    /**
     * Update an existing key result
     * @param keyResult The key result with updated information
     * @return The updated key result
     */
    KeyResult updateKeyResult(KeyResult keyResult);

    /**
     * Get a key result by ID
     * @param id The ID of the key result
     * @return The key result with the given ID
     */
    KeyResult getKeyResultById(String id);

    /**
     * Get all key results for an objective
     * @param objectiveId The ID of the objective
     * @return List of key results for the given objective
     */
    List<KeyResult> listKeyResultsByObjectiveId(String objectiveId);

    /**
     * Get all key results by owner ID
     * @param ownerId The ID of the owner
     * @return List of key results for the given owner
     */
    List<KeyResult> listKeyResultsByOwnerId(String ownerId);

    /**
     * Delete a key result by ID
     * @param id The ID of the key result to delete
     */
    void deleteKeyResult(String id);
}