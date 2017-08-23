package com.ninegold.ninegoldapi.services.springdata;

import com.ninegold.ninegoldapi.entities.AuditableEntity;
import com.ninegold.ninegoldapi.entities.AuditableUserEntity;
import com.ninegold.ninegoldapi.entities.IdentifiableEntity;
import com.ninegold.ninegoldapi.entities.User;
import com.ninegold.ninegoldapi.exceptions.ConfigurationException;
import com.ninegold.ninegoldapi.exceptions.EntityNotFoundException;
import com.ninegold.ninegoldapi.exceptions.NineGoldException;
import com.ninegold.ninegoldapi.utils.Helper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

/**
 * This is a base class for services that provides basic CRUD capabilities.
 *
 * @param <T> the identifiable entity
 * @param <S> the search criteria
 */
@NoArgsConstructor(access = PROTECTED)
public abstract class BaseService<T extends IdentifiableEntity, S> {
    /**
     * The default sort column by id.
     */
    public static final String ID = "id";

    /**
     * The repository for CRUD operations. Should be non-null after injection.
     */
    @Autowired
    @Getter(value = PROTECTED)
    private JpaRepository<T, Long> repository;

    /**
     * The specification executor. Should be non-null after injection.
     */
    @Autowired
    private JpaSpecificationExecutor<T> specificationExecutor;


    /**
     * Check if all required fields are initialized properly.
     *
     * @throws ConfigurationException if any required field is not initialized properly.
     */
    @PostConstruct
    protected void checkConfiguration() {
        Helper.checkConfigNotNull(repository, "repository");
        Helper.checkConfigNotNull(specificationExecutor, "specificationExecutor");
    }

    /**
     * This method is used to retrieve an entity.
     *
     * @param id the id of the entity to retrieve
     * @return the match entity
     * @throws IllegalArgumentException if id is not positive
     * @throws EntityNotFoundException if the entity does not exist
     * @throws NineGoldException if any other error occurred during operation
     */
    public T get(long id) throws NineGoldException {
        return ensureEntityExist(id);
    }

    /**
     * This method is used to create an entity.
     *
     * @param entity the entity to create
     * @return the created entity
     * @throws IllegalArgumentException if entity is null or not valid
     * @throws NineGoldException if any other error occurred during operation
     */
    @Transactional
    public T create(T entity) throws NineGoldException {
        Helper.checkNull(entity, "entity");
        if (entity instanceof AuditableEntity) {
            if (entity instanceof AuditableUserEntity) {
                Helper.audit((AuditableUserEntity) entity);
            } else {
                AuditableEntity auditableEntity = (AuditableEntity) entity;
                Date now = new Date();
                auditableEntity.setCreatedOn(new Date());
                auditableEntity.setLastModifiedOn(now);
            }
        }
        return repository.save(entity);
    }

    /**
     * This method is used to delete an entity.
     *
     * @param id the id of the entity to delete
     * @throws IllegalArgumentException if id is not positive
     * @throws EntityNotFoundException if the entity does not exist
     * @throws NineGoldException if any other error occurred during operation
     */
    @Transactional
    public void delete(long id) throws NineGoldException {
        Helper.checkPositive(id, "id");
        ensureEntityExist(id);
        repository.delete(id);
    }


    /**
     * This method is used to update an entity.
     *
     * @param id the id of the entity to update
     * @param entity the entity to update
     * @return the updated entity
     * @throws IllegalArgumentException if id is not positive or entity is null or id of entity is not positive
     * or id of  entity not match id or entity is invalid
     * @throws EntityNotFoundException if the entity does not exist
     * @throws NineGoldException if any other error occurred during operation
     */
    @Transactional
    public T update(long id, T entity) throws NineGoldException {
        T existing = checkUpdate(id, entity);
        if (entity instanceof AuditableEntity) {
            AuditableEntity auditableEntity = (AuditableEntity) entity;
            auditableEntity.setCreatedOn(((AuditableEntity) existing).getCreatedOn());
            auditableEntity.setLastModifiedOn(new Date());
            if (entity instanceof AuditableUserEntity) {
                AuditableUserEntity newEntity = (AuditableUserEntity) entity;
                AuditableUserEntity existingEntity = (AuditableUserEntity) existing;
                newEntity.setCreatedBy(existingEntity.getCreatedBy());
                User authUser = Helper.getAuthUser();
                if (authUser != null) {
                    newEntity.setLastModifiedBy(authUser.getId());
                }
            }
        }
        return repository.save(entity);
    }

    /**
     * Check id and entity for update method.
     *
     * @param id the id of the entity to update
     * @param entity the entity to update
     * @return the existing entity
     * @throws IllegalArgumentException if id is not positive or entity is null or id of entity is not positive
     * or id of  entity not match id
     * @throws EntityNotFoundException if the entity does not exist
     */
    protected T checkUpdate(long id, T entity) throws EntityNotFoundException {
        Helper.checkUpdate(id, entity);
        return ensureEntityExist(id);
    }


    /**
     * This method is used to search for entities by criteria.
     *
     * @param criteria the search criteria
     * @return the search result
     * @throws NineGoldException if any other error occurred during operation
     */
    public List<T> search(S criteria) throws NineGoldException {
        return specificationExecutor.findAll(getSpecification(criteria));
    }

    /**
     * This method is used to get total count for search results of entities with criteria.
     *
     * @param criteria the search criteria
     * @return the total count of search result
     * @throws IllegalArgumentException if pageSize is not positive or pageNumber is negative
     * @throws NineGoldException if any other error occurred during operation
     */
    public long count(S criteria) throws NineGoldException {
        return specificationExecutor.count(getSpecification(criteria));
    }


    /**
     * This method is used to get the Specification<T>.
     *
     * @param criteria the criteria
     * @return the specification
     * @throws IllegalArgumentException if pageSize is not positive or pageNumber is negative
     * @throws NineGoldException if any other error occurred during operation
     */
    protected abstract Specification<T> getSpecification(S criteria) throws NineGoldException;


    /**
     * Check whether an identifiable entity with a given id exists.
     *
     * @param id the id of entity
     * @return the match entity
     * @throws IllegalArgumentException if id is not positive
     * @throws EntityNotFoundException if the match entity can not be found in DB
     */
    private T ensureEntityExist(long id) throws EntityNotFoundException {
        Helper.checkPositive(id, "id");
        T entity = repository.findOne(id);
        if (entity == null) {
            throw new EntityNotFoundException("Entity with ID=" + id + " can not be found");
        }
        return entity;
    }
}

