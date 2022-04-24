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
    public User create(User item) {
        item.prePersist(null, new Date());
        item.correctData();
        log.info("{}", item);
        userRepository.persist(item);
        return item;
    }

    @Override
    public User update(User item) {
        User eitem = userRepository.findById(item.getId());
        // ToDo: copy relevant fields from item to eitem;
        return eitem;
    }

//    @Override
//    public void delete(Long id) {
//        userRepository.deleteById(id);
//    }
}
