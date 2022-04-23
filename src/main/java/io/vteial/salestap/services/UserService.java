package io.vteial.salestap.services;

import io.vteial.salestap.models.User;

public interface UserService {

    User findById(Long id);

    User findByUserId(String userId);

    User create(User user);

    User update(User user);

//    void delete(Long userId);
}
