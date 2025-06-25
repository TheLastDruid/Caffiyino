package com.coffeeshop.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Generic DAO interface defining common CRUD operations
 * 
 * @param <T> Entity type
 * @param <ID> Primary key type
 */
public interface GenericDAO<T, ID> {
    
    /**
     * Save or update an entity
     * @param entity Entity to save
     * @return Saved entity with generated ID
     * @throws SQLException if database error occurs
     */
    T save(T entity) throws SQLException;
    
    /**
     * Find entity by ID
     * @param id Primary key
     * @return Optional containing entity if found
     * @throws SQLException if database error occurs
     */
    Optional<T> findById(ID id) throws SQLException;
    
    /**
     * Find all entities
     * @return List of all entities
     * @throws SQLException if database error occurs
     */
    List<T> findAll() throws SQLException;
    
    /**
     * Update an existing entity
     * @param entity Entity to update
     * @throws SQLException if database error occurs
     */
    void update(T entity) throws SQLException;
    
    /**
     * Delete entity by ID
     * @param id Primary key
     * @throws SQLException if database error occurs
     */
    void deleteById(ID id) throws SQLException;
    
    /**
     * Check if entity exists by ID
     * @param id Primary key
     * @return true if exists, false otherwise
     * @throws SQLException if database error occurs
     */
    boolean existsById(ID id) throws SQLException;
    
    /**
     * Count total number of entities
     * @return Total count
     * @throws SQLException if database error occurs
     */
    long count() throws SQLException;
}
