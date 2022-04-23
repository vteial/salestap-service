package io.vteial.salestap.services.impl;

import io.vteial.salestap.models.User;
import io.vteial.salestap.repos.UserRepository;
import io.vteial.salestap.services.UserService;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Date;

@Slf4j
@ApplicationScoped
public class DefaultUserService implements UserService {

    @Inject
    UserRepository userRepository;

    @Override
    public User findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public User findByUserId(String userId) {
        return userRepository.findByUserId(userId);
    }

    @Override
    public User create(User user) {
        user.prePersist(null, new Date());
        user.correctData();
        userRepository.persist(user);
        return user;
    }

    @Override
    public User update(User user) {
        User euser = userRepository.findById(user.getId());
        // ToDo: copy relevant fields from user to euser;
        return euser;
    }

//    @Override
//    public void delete(Long id) {
//        userRepository.deleteById(id);
//    }
}
