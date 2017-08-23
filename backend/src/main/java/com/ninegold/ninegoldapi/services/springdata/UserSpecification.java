package com.ninegold.ninegoldapi.services.springdata;

import com.ninegold.ninegoldapi.entities.User;
import com.ninegold.ninegoldapi.entities.UserSearchCriteria;
import com.ninegold.ninegoldapi.utils.Helper;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * The specification used to query User by criteria.
 */
@AllArgsConstructor
public class UserSpecification implements Specification<User> {
    /**
     * The criteria. Final.
     */
    private final UserSearchCriteria criteria;


    /**
     * Creates a WHERE clause for a query of the referenced entity
     * in form of a Predicate for the given Root and CriteriaQuery.
     * @param root the root
     * @param query the criteria query
     * @param cb the query builder
     * @return the predicate
     */
    public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        Predicate pd = cb.and();
        pd = Helper.buildLikePredicate(criteria.getEmail(), pd, root.get("email"), cb);
        return pd;
    }
}

