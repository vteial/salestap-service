package io.vteial.salestap.repos;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.vteial.salestap.models.User;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserRepository implements PanacheRepository<User>  {

    public User findByUserId(String userId) {
        return this.find("userId", userId).firstResult();
    }
}
