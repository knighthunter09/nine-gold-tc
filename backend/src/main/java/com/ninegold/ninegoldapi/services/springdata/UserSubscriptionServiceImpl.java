package com.ninegold.ninegoldapi.services.springdata;

import com.ninegold.ninegoldapi.entities.UserSubscription;
import com.ninegold.ninegoldapi.entities.UserSubscriptionSearchCriteria;
import com.ninegold.ninegoldapi.exceptions.NineGoldException;
import com.ninegold.ninegoldapi.services.UserSubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by abhinav on 2/8/17.
 */
@Service
public class UserSubscriptionServiceImpl extends BaseService<UserSubscription, UserSubscriptionSearchCriteria> implements UserSubscriptionService {

    @Autowired
    private UserSubscriptionRepository userSubscriptionRepository;

    /**
     * This method is used to get the specification.
     *
     * @param criteria the search criteria
     * @return the specification
     * @throws NineGoldException if any other error occurred during operation
     */
    protected Specification<UserSubscription> getSpecification(UserSubscriptionSearchCriteria criteria) throws NineGoldException {
        return new UserSubscriptionSpecification(criteria);
    }

    @Override
    public List<UserSubscription> findByUserId(long userId) throws NineGoldException {
        return userSubscriptionRepository.findByUserId(userId);
    }
}
