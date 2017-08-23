package com.ninegold.ninegoldapi.services;

import com.ninegold.ninegoldapi.entities.UserSubscription;
import com.ninegold.ninegoldapi.entities.UserSubscriptionSearchCriteria;
import com.ninegold.ninegoldapi.exceptions.NineGoldException;

import java.util.List;

/**
 * Service which deals with user subscriptions
 */
public interface UserSubscriptionService extends GenericService<UserSubscription, UserSubscriptionSearchCriteria> {
    List<UserSubscription> findByUserId(long userId) throws NineGoldException;
}
