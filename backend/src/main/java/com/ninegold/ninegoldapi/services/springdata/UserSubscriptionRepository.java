package com.ninegold.ninegoldapi.services.springdata;

import com.ninegold.ninegoldapi.entities.UserSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * Created by abhinav on 2/8/17.
 */
public interface UserSubscriptionRepository extends JpaRepository<UserSubscription, Long>, JpaSpecificationExecutor<UserSubscription> {

    List<UserSubscription> findByUserId(long userId);
}
