package com.ninegold.ninegoldapi.services.springdata;

import com.ninegold.ninegoldapi.entities.UserSubscription;
import com.ninegold.ninegoldapi.entities.UserSubscriptionSearchCriteria;
import com.ninegold.ninegoldapi.utils.Helper;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * The specification used to query UserSubscription by criteria.
 */
@AllArgsConstructor
public class UserSubscriptionSpecification implements Specification<UserSubscription> {
    /**
     * The criteria. Final.
     */
    private final UserSubscriptionSearchCriteria criteria;


    /**
     * Creates a WHERE clause for a query of the referenced entity
     * in form of a Predicate for the given Root and CriteriaQuery.
     * @param root the root
     * @param query the criteria query
     * @param cb the query builder
     * @return the predicate
     */
    public Predicate toPredicate(Root<UserSubscription> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        Predicate pd = cb.and();
        pd = Helper.buildEqualPredicate(criteria.getUserId(), pd, root.get("userId"), cb);
        return pd;
    }
}

