package com.ninegold.ninegoldapi.services.springdata;

import com.ninegold.ninegoldapi.entities.ForgotPassword;
import com.ninegold.ninegoldapi.entities.NewPassword;
import com.ninegold.ninegoldapi.entities.User;
import com.ninegold.ninegoldapi.entities.UserSearchCriteria;
import com.ninegold.ninegoldapi.exceptions.ConfigurationException;
import com.ninegold.ninegoldapi.exceptions.EntityNotFoundException;
import com.ninegold.ninegoldapi.exceptions.NineGoldException;
import com.ninegold.ninegoldapi.services.UserService;
import com.ninegold.ninegoldapi.utils.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.UUID;

/**
 * The Spring Data JPA implementation of UserService,
 * extends BaseService<User,UserSearchCriteria>. Effectively thread safe after configuration.
 */
@Service
public class UserServiceImpl extends BaseService<User, UserSearchCriteria> implements UserService {
    /**
     * The expiration time in millis.
     */
    @Value("${forgotPassword.expirationTimeInMillis}")
    private long forgotPasswordExpirationTimeInMillis;

    /**
     * The maxTimes to send forgot password request for single user.
     */
    @Value("${forgotPassword.maxTimes}")
    private long forgotPasswordMaxTimes;

    /**
     * The token handler.
     */
    //private final TokenHandler tokenHandler;

    /**
     * The forgot password repository for CRUD operations. Should be non-null after injection.
     */
    @Autowired
    private ForgotPasswordRepository forgotPasswordRepository;

    /**
     * The forgot password repository for CRUD operations. Should be non-null after injection.
     */
    @Autowired
    private UserRepository userRepository;

    /**
     * Check if all required fields are initialized properly.
     *
     * @throws ConfigurationException if any required field is not initialized properly.
     */
    @PostConstruct
    protected void checkConfiguration() {
        super.checkConfiguration();
        Helper.checkConfigNotNull(forgotPasswordRepository, "forgotPasswordRepository");
        Helper.checkConfigNotNull(userRepository, "userRepository");
        Helper.checkConfigPositive(forgotPasswordExpirationTimeInMillis,
                "forgotPasswordExpirationTimeInMillis");
        Helper.checkConfigPositive(forgotPasswordMaxTimes, "forgotPasswordMaxTimes");
    }

    /**
     * This method is used to get the specification.
     *
     * @param criteria the search criteria
     * @return the specification
     * @throws NineGoldException if any other error occurred during operation
     */
    protected Specification<User> getSpecification(UserSearchCriteria criteria) throws NineGoldException {
        return new UserSpecification(criteria);
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
    public User create(User entity) throws NineGoldException {
        Helper.encodePassword(entity, false);
        return super.create(entity);
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
    public User update(long id, User entity) throws NineGoldException {
        User existing = super.checkUpdate(id, entity);
        // Apart from below fields, rest of the fields are auto-generated
        // and hence shouldn't be allowed for update
        if (entity.getEmail() != null) {
            existing.setEmail(entity.getEmail());
        }
        if (entity.getFirstName() != null) {
            existing.setFirstName(entity.getFirstName());
        }
        if (entity.getLastName() != null) {
            existing.setLastName(entity.getLastName());
        }
        if (entity.getPassword() != null) {
            existing.setPassword(entity.getPassword());
        }
        if (entity.getPaymentToken() != null) {
            existing.setPaymentToken(entity.getPaymentToken());
        }
        return getRepository().save(existing);
    }

    /**
     * This method is used to create the forgot password entity for the given user.
     *
     * @param userId the user id.
     * @return the created forgot password entity
     * @throws IllegalArgumentException if id is not positive
     * @throws EntityNotFoundException if the entity does not exist
     * @throws NineGoldException if any other error occurred during operation
     */
    @Transactional
    public ForgotPassword forgotPassword(long userId) throws NineGoldException {
        Helper.checkPositive(userId, "userId");
        ForgotPassword forgotPassword = new ForgotPassword();
        String token = UUID.randomUUID().toString();
        Date expiredOn = new Date(System.currentTimeMillis() + forgotPasswordExpirationTimeInMillis);
        forgotPassword.setUserId(userId);
        forgotPassword.setToken(token);
        forgotPassword.setExpiredOn(expiredOn);
        return forgotPasswordRepository.save(forgotPassword);
    }

    /**
     * This method is used to update the forgot password entity for the given token.
     *
     * @param newPassword the newPassword request.
     * @return true if update the password successfully otherwise false
     * @throws IllegalArgumentException if newPassword is null or invalid
     * @throws NineGoldException if any other error occurred during operation
     */
    @Transactional
    public boolean resetPassword(NewPassword newPassword) throws NineGoldException {
        Helper.checkNull(newPassword, "newPassword");
        String token = newPassword.getToken();
        String newPass = newPassword.getNewPassword();
        Helper.checkNullOrEmpty(token, "newPassword.token");
        Helper.checkNullOrEmpty(newPass, "newPassword.newPassword");
        ForgotPassword forgotPassword = forgotPasswordRepository.findByToken(token);
        if (forgotPassword != null) {
            Date currentDate = new Date();
            if (currentDate.before(forgotPassword.getExpiredOn())) {
                User user = get(forgotPassword.getUserId());
                user.setPassword(newPass);
                Helper.encodePassword(user, false);
                getRepository().save(user);
                forgotPasswordRepository.deleteByUserId(forgotPassword.getUserId());
                return true;
            }
        } else {
            throw new IllegalArgumentException("Token is not correct");
        }
        return false;
    }


    @Override
    public User getMe() throws NineGoldException {
        // get the user from the tokens
        User user = Helper.getAuthUser();
        if (user == null) {
            return null;
        }

        // update the user info in db
        user = super.get(user.getId());

        return user;
    }

    @Transactional
    public void delete(long id) throws NineGoldException {
        User user = this.get(id);
        user.setTerminated(true);
        this.update(id, user);
    }

    @Override
    public User findByCustomerId(String customerId) throws NineGoldException {
        return userRepository.findByCustomerId(customerId);
    }
}

