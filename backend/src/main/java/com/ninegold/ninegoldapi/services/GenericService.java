package com.ninegold.ninegoldapi.services;

import com.ninegold.ninegoldapi.entities.IdentifiableEntity;
import com.ninegold.ninegoldapi.exceptions.EntityNotFoundException;
import com.ninegold.ninegoldapi.exceptions.NineGoldException;

import java.util.List;

/**
 * The generic service. Served as a base interface for CRUD operations.
 * @param <T> the entity
 * @param <S> the search criteria
 */
public interface GenericService<T extends IdentifiableEntity, S> {
    /**
     * This method is used to retrieve an entity.
     *
     * @param id the id of the entity to retrieve
     * @return the match entity
     * @throws IllegalArgumentException if id is not positive
     * @throws EntityNotFoundException if the entity does not exist
     * @throws NineGoldException if any other error occurred during operation
     */
    T get(long id) throws NineGoldException;

    /**
     * This method is used to create an entity.
     *
     * @param entity the entity to create
     * @return the created entity
     * @throws IllegalArgumentException if entity is null or not valid
     * @throws NineGoldException if any other error occurred during operation
     */
     T create(T entity) throws NineGoldException;

    /**
     * This method is used to update an entity.
     *
     * @param id the id of the entity to update
     * @param entity the entity to update
     * @return the updated entity
     * @throws IllegalArgumentException if id is not positive or entity is null or id of entity is not positive
     *                                  or id of  entity not match id or entity is invalid
     * @throws EntityNotFoundException if the entity does not exist
     * @throws NineGoldException if any other error occurred during operation
     */
    T update(long id, T entity) throws NineGoldException;

    /**
     * This method is used to delete an entity.
     *
     * @param id the id of the entity to delete
     * @throws IllegalArgumentException if id is not positive
     * @throws EntityNotFoundException if the entity does not exist
     * @throws NineGoldException if any other error occurred during operation
     */
     void delete(long id)throws NineGoldException;

    /**
     * This method is used to search for entities by criteria.
     *
     * @param criteria the search criteria
     * @return the search result
     * @throws NineGoldException if any other error occurred during operation
     */
     List<T> search(S criteria) throws NineGoldException;

    /**
     * This method is used to get total count for search results of entities with criteria.
     *
     * @param criteria the search criteria
     * @return the total count of search result
     * @throws IllegalArgumentException if pageSize is not positive or pageNumber is negative
     * @throws NineGoldException if any other error occurred during operation
     */
    long count(S criteria) throws NineGoldException;
}

